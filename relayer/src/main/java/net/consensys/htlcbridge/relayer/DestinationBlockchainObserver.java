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
import net.consensys.htlcbridge.transfer.TransferState;
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

public class DestinationBlockchainObserver extends BlockchainObserver {
  private static final Logger LOG = LogManager.getLogger(DestinationBlockchainObserver.class);

  public DestinationBlockchainObserver(
      String sourceUri, String transferContractAddress, int sourceBlockPeriod, int sourceConfirmations,
      String sourcePKey, int sourceRetries, long sourceBcId, String sourceGasStrategy,
      String destUri, String receiverContractAddress, int destBlockPeriod, int destConfirmations,
      String destPKey, int destRetries, long destBcId, String destGasStrategy) throws Exception {
    super(
        sourceUri, transferContractAddress, sourceBlockPeriod, sourceConfirmations,
        sourcePKey, sourceRetries, sourceBcId, sourceGasStrategy,
        destUri, receiverContractAddress, destBlockPeriod, destConfirmations,
        destPKey, destRetries, destBcId, destGasStrategy);

    this.isSourceObserver = false;
    setLackBlockCheckedInitialValue(destBlockPeriod, destConfirmations, this.destTransferContract, this.destWeb3j);
  }


  public void checkNewBlock() {
    checkNewBlock(this.destWeb3j);
  }


  protected void processNextBlock(EthBlockNumber ethBlockNumber) {
    DefaultBlockParameter[] result = determineIfBlockToProcess(ethBlockNumber, this.destConfirmations);
    if (result == null) {
      return;
    }
    DefaultBlockParameter startBlock = result[0];
    DefaultBlockParameter endBlock = result[1];

    Flowable<Erc20HtlcTransfer.DestTransferCompletedEventResponse> transferCompletedEvents =
        this.destTransferContract.destTransferCompletedEventFlowable(startBlock, endBlock);
    transferCompletedEvents.subscribe(new io.reactivex.functions.Consumer<Erc20HtlcTransfer.DestTransferCompletedEventResponse>() {
      @Override
      public void accept(Erc20HtlcTransfer.DestTransferCompletedEventResponse txCompleteEvent) {
        byte[] commitment = txCompleteEvent.commitment;
        byte[] preimageSalt = txCompleteEvent.preimage;
        String commitmentS = Bytes.wrap(commitment).toHexString();
        String preimageSaltS = Bytes.wrap(preimageSalt).toHexString();
        LOG.info("Detected Transfer {}: PreimageSalt: {}", commitmentS, preimageSaltS);

        // Check whether another relayer has already submitted this transfer.
        CompletableFuture<BigInteger> futureState = srcTransferContract.sourceTransferState(commitment).sendAsync();
        Context context = vertx.getOrCreateContext();
        futureState.handle((state, th) -> {
          context.runOnContext(event -> {
            if (th == null) {
              if (!TransferState.OPEN.equals(state)) {
                LOG.info(" Transfer {} state is: {}", commitmentS, TransferState.create(state));
              } else {
                finalseTransferAtSource(commitmentS, commitment, preimageSalt);
              }
            } else {
              LOG.error("Transfer {}: Error: {}", commitmentS, th.toString());
            }
          });
          return null;
        });
      }
    });
  }

  private void finalseTransferAtSource(final String commitmentS, final byte[] commitment, final byte[] preimageSalt) {
    LOG.info("Finalising transfer {}", commitmentS);

    CompletableFuture<TransactionReceipt> futureTxr = srcTransferContract.finaliseTransferToOtherBlockchain(commitment, preimageSalt).sendAsync();
    Context context = vertx.getOrCreateContext();
    futureTxr.handle((txr, th) -> {
      context.runOnContext(event -> {
        if (th == null) {
          if (txr.isStatusOK()) {
            LOG.info("Transfer {} finalised", commitmentS);
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
