/*
 * Copyright 2021 ConsenSys Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package net.consensys.htlcbridge.relayer;

import io.reactivex.Flowable;
import io.vertx.core.Context;
import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class SourceBlockchainObserver extends BlockchainObserver {
  private static final Logger LOG = LogManager.getLogger(SourceBlockchainObserver.class);

  public SourceBlockchainObserver(
      String sourceUri, String transferContractAddress, int sourceBlockPeriod, int sourceConfirmations,
      String sourcePKey, int sourceRetries, long sourceBcId, String sourceGasStrategy,
      String destUri, String receiverContractAddress, int destBlockPeriod, int destConfirmations,
      String destPKey, int destRetries, long destBcId, String destGasStrategy) throws Exception {
    super(
        sourceUri, transferContractAddress, sourceBlockPeriod, sourceConfirmations,
        sourcePKey, sourceRetries, sourceBcId, sourceGasStrategy,
        destUri, receiverContractAddress, destBlockPeriod, destConfirmations,
        destPKey, destRetries, destBcId, destGasStrategy);

    this.isSourceObserver = true;
    setLackBlockCheckedInitialValue(sourceBlockPeriod, sourceConfirmations, this.srcTransferContract, this.sourceWeb3j);
  }


  public void checkNewBlock() {
    checkNewBlock(this.sourceWeb3j);
  }

  protected void processNextBlock(EthBlockNumber ethBlockNumber) {
    DefaultBlockParameter[] result = determineIfBlockToProcess(ethBlockNumber, this.sourceConfirmations);
    if (result == null) {
      return;
    }
    DefaultBlockParameter startBlock = result[0];
    DefaultBlockParameter endBlock = result[1];
    Flowable<Erc20HtlcTransfer.SourceTransferInitEventResponse> transferInitEvents =
        this.srcTransferContract.sourceTransferInitEventFlowable(startBlock, endBlock);

    transferInitEvents.subscribe(new io.reactivex.functions.Consumer<Erc20HtlcTransfer.SourceTransferInitEventResponse>() {
      @Override
      public void accept(Erc20HtlcTransfer.SourceTransferInitEventResponse txInitEvent) {
        String commitmentS = Bytes.wrap(txInitEvent.commitment).toHexString();

        if (txInitEvent.amount.compareTo(BigInteger.ZERO) == 0) {
          LOG.info("Ignoring transfer ({}) as amount is 0", commitmentS);
          return;
        }

        // Check whether another relayer has already submitted this transfer.
        CompletableFuture<Boolean> futureTransferExists = destTransferContract.destTransferExists(txInitEvent.commitment).sendAsync();
        Context context = vertx.getOrCreateContext();
        futureTransferExists.handle((transferExists, th) -> {
          context.runOnContext(event -> {
            if (th == null) {
              if (transferExists) {
                LOG.info("Ignoring transfer ({}) already communicated to destination", commitmentS);
              } else {
                postCommitmentToDestination(txInitEvent, commitmentS);
              }
            } else {
              LOG.error("Error processing DestTransferExists: Commitment: {}, Error: {}", commitmentS, th.toString());
            }
          });
          return null;
        });
      }
    });
  }

  private void postCommitmentToDestination(Erc20HtlcTransfer.SourceTransferInitEventResponse txInitEvent, String commitmentS) {
    LOG.info("Submitting transfer: Commitment: {}, Sender: {}, Token Contract: {}, Amount: {}, TimeLock: {}",
        commitmentS, txInitEvent.sender, txInitEvent.tokenContract, txInitEvent.amount, txInitEvent.timeLock);

    CompletableFuture<TransactionReceipt> futureTxr = destTransferContract.newTransferFromOtherBlockchain(txInitEvent.tokenContract, txInitEvent.sender, txInitEvent.amount, txInitEvent.commitment).sendAsync();
    Context context = vertx.getOrCreateContext();
    futureTxr.handle((txr, th) -> {
      context.runOnContext(event -> {
        if (th == null) {
          if (txr.isStatusOK()) {
            LOG.info("Transfer {} commitment posted", commitmentS);
          }
          else {
            LOG.error("Transfer {} failed: {}", commitmentS, txr.getStatus());
          }
        } else {
          if (th instanceof TransactionException) {
            TransactionException ex = (TransactionException) th;
            LOG.error("Transfer {} failed: Revert Reason: {}", commitmentS, RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
          }
          else {
            LOG.error("Transfer {} failed: Error: {}", commitmentS, th.toString());
          }
        }
      });
      return null;
    });
  }


}
