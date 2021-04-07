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

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import net.consensys.htlcbridge.common.DynamicGasProvider;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BlockchainObserver {
  private static final Logger LOG = LogManager.getLogger(BlockchainObserver.class);

  Vertx vertx;

  protected final int sourceConfirmations;
  protected final int destConfirmations;

  protected final Erc20HtlcTransfer srcTransferContract;
  protected final Erc20HtlcTransfer destTransferContract;
  protected final Web3j sourceWeb3j;
  protected final Web3j destWeb3j;

  // TODO volitile or atomic reference compareAndUpdate
  protected AtomicLong lastBlockChecked;

  protected boolean isSourceObserver;

  public BlockchainObserver(
      String sourceUri, String transferContractAddress, int sourceBlockPeriod, int sourceConfirmations,
      String sourcePKey, int sourceRetries, long sourceBcId, String sourceGasStrategy,
      String destUri, String receiverContractAddress, int destBlockPeriod, int destConfirmations,
      String destPKey, int destRetries, long destBcId, String destGasStrategy) throws Exception {
    this.sourceConfirmations = sourceConfirmations;
    this.destConfirmations = destConfirmations;
    this.sourceWeb3j = Web3j.build(new HttpService(sourceUri), sourceBlockPeriod, new ScheduledThreadPoolExecutor(5));
    ContractGasProvider sourceGasProvider = new DynamicGasProvider(this.sourceWeb3j, sourceUri, sourceGasStrategy);
    this.destWeb3j = Web3j.build(new HttpService(destUri), destBlockPeriod, new ScheduledThreadPoolExecutor(5));
    ContractGasProvider destGasProvider = new DynamicGasProvider(this.destWeb3j, destUri, destGasStrategy);

    Credentials sourceCredentials = Credentials.create(sourcePKey);
    Credentials destCredentials = Credentials.create(destPKey);
    TransactionManager sourceTm = new RawTransactionManager(this.sourceWeb3j, sourceCredentials, sourceBcId, sourceRetries, sourceBlockPeriod);
    TransactionManager destTm = new RawTransactionManager(this.destWeb3j, destCredentials, destBcId, destRetries, destBlockPeriod);

    this.srcTransferContract = Erc20HtlcTransfer.load(transferContractAddress, sourceWeb3j, sourceTm, sourceGasProvider);
    this.destTransferContract = Erc20HtlcTransfer.load(receiverContractAddress, destWeb3j, destTm, destGasProvider);
  }

  public void init(Vertx vertx) {
    this.vertx = vertx;
  }




  public abstract void checkNewBlock();

  protected void checkNewBlock(final Web3j web3j) {
    CompletableFuture<EthBlockNumber> futureEthBlockNumber = web3j.ethBlockNumber().sendAsync();
    Context context = vertx.getOrCreateContext();
    futureEthBlockNumber.handle((ethBlockNumber, th) -> {
      context.runOnContext(event -> {
        if (th == null) {
          processNextBlock(ethBlockNumber);
        } else {
          LOG.error("{}: Get block number failed: Error: {}", observerName(), th.toString());
        }
      });
      return null;
    });
  }

  protected abstract void processNextBlock(final EthBlockNumber ethBlockNumber);

  protected DefaultBlockParameter[] determineIfBlockToProcess(final EthBlockNumber ethBlockNumber, final int confirmations) {
    BigInteger blockNumber = ethBlockNumber.getBlockNumber();
    long currentBlockNumber = blockNumber.longValue();

    // Check for events between last block checked and current block - number of confirmations
    long endBlockNumber = currentBlockNumber - confirmations;
    boolean successful;
    long startBlockNumber;
    do {
      long theLastBlockChecked = this.lastBlockChecked.get();
      startBlockNumber = theLastBlockChecked + 1;
      if (startBlockNumber > endBlockNumber) {
        LOG.info("{}: Current Block: {}. No new blocks to process", observerName(), blockNumber);
        return null;
      }
      // Update lastBlockChecked, indicating that the block(s) lastBlockChecked and before are
      // being of will be processed.
      successful = this.lastBlockChecked.compareAndSet(theLastBlockChecked, endBlockNumber);
    } while (!successful);

    LOG.info("{}: Current Block: {}. Processing blocks {} to {}", observerName(), currentBlockNumber, startBlockNumber, endBlockNumber);
    DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlockNumber));
    DefaultBlockParameter endBlock = DefaultBlockParameter.valueOf(BigInteger.valueOf(endBlockNumber));
    return new DefaultBlockParameter[]{startBlock, endBlock};
  }


  protected void setLackBlockCheckedInitialValue(
      final int blockPeriod, final int confirmations, final Erc20HtlcTransfer transferContract, final Web3j web3j) throws Exception {
    BigInteger timeLockPeriod = transferContract.sourceTimeLockPeriod().send();
    BigInteger currentBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();
    long earliestBlockToCheck = currentBlockNumber.longValue() - (timeLockPeriod.longValue() * 1000 / blockPeriod);
    long lastBlockCheckedL;
    if (earliestBlockToCheck < 0) {
      lastBlockCheckedL = -1;
    }
    else {
      lastBlockCheckedL = earliestBlockToCheck;
    }
    LOG.info("{}: Init: Current: {}, Earliest: {}, Last: {}, TimeLock: {} Period: {}, Confirmations: {}",
        observerName(), currentBlockNumber, earliestBlockToCheck, lastBlockCheckedL, timeLockPeriod, blockPeriod, confirmations);
    this.lastBlockChecked = new AtomicLong(lastBlockCheckedL);
  }

  private String observerName() {
    return this.isSourceObserver ? "Source Observer" : "Dest Observer";
  }
}
