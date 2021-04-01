package net.consensys.htlcbridge.relayer;

import io.reactivex.Flowable;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.relayer.data.TransferInformation;
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

    setLackBlockChecked(sourceBlockPeriod);
  }

  private void setLackBlockChecked(int sourceBlockPeriod) throws Exception {
    BigInteger timeLockPeriod = this.srcTransferContract.sourceTimeLockPeriod().send();
    BigInteger currentBlockNumber = this.sourceWeb3j.ethBlockNumber().send().getBlockNumber();
    long earliestBlockToCheck = currentBlockNumber.longValue() - (timeLockPeriod.longValue() * 1000 / sourceBlockPeriod);
    if (earliestBlockToCheck < 0) {
      this.lastBlockChecked = -1;
    }
    else {
      this.lastBlockChecked = earliestBlockToCheck;
    }
    LOG.info("Source Observer: Initial Last Block Checked: Current: {}, TimeLock: {} Period: {}, Earliest: {}, Last: {}",
        currentBlockNumber, timeLockPeriod, sourceBlockPeriod, earliestBlockToCheck, this.lastBlockChecked);

  }


  public void checkNewBlock() {
//    long relayCounter = System.currentTimeMillis() / 1000;
//    relayCounter = relayCounter % this.numRelayers;
//    if (relayCounter != this.relayerOffset) {
//      LOG.info("Source Blockchain Observer fairness skipping: {}: {} of {}", this.relayerOffset, relayCounter, this.numRelayers);
//      return;
//    }

    try {
      EthBlockNumber ethBlockNumber = this.sourceWeb3j.ethBlockNumber().send();
      BigInteger blockNumber = ethBlockNumber.getBlockNumber();
      long currentBlockNumber = blockNumber.longValue();

      // Check for events between last block checked and current block - number of confirmations
      long endBlockNumber = currentBlockNumber - this.sourceConfirmations;
      if (this.lastBlockChecked > endBlockNumber) {
        LOG.info("Current Block: {}. No new blocks to process", blockNumber);
        return;
      }

      BigInteger startBlockNum = BigInteger.valueOf(this.lastBlockChecked + 1);
      DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(startBlockNum);
      BigInteger endBlockNum = BigInteger.valueOf(endBlockNumber);
      DefaultBlockParameter endBlock = DefaultBlockParameter.valueOf(endBlockNum);
      LOG.info("Current Block: {}. Requesting events from blocks {} to {}", currentBlockNumber, startBlockNum, endBlockNum);
      Flowable<Erc20HtlcTransfer.SourceTransferInitEventResponse> transferInitEvents =
          this.srcTransferContract.sourceTransferInitEventFlowable(startBlock, endBlock);

      transferInitEvents.subscribe(new io.reactivex.functions.Consumer<Erc20HtlcTransfer.SourceTransferInitEventResponse>() {
        @Override
        public void accept(Erc20HtlcTransfer.SourceTransferInitEventResponse txInitEvent) throws Exception {
          String commitmentS = Bytes.wrap(txInitEvent.commitment).toHexString();

          if (txInitEvent.amount.compareTo(BigInteger.ZERO) == 0) {
            LOG.info("Ignoring transfer ({}) as amount is 0", commitmentS);
          }

          // Check whether another relayer has already submitted this transfer.
          CompletableFuture<Boolean> futureTransferExists = destTransferContract.destTransferExists(txInitEvent.commitment).sendAsync();
          Context context = vertx.getOrCreateContext();
          futureTransferExists.handleAsync((transferExists, th) -> {
            context.runOnContext(event -> {
              if (th == null) {
                if (transferExists) {
                  LOG.info("Ignoring transfer ({}) already communicated to destination", commitmentS);
                } else {
                  postCommitmentToDestination(txInitEvent, commitmentS);
                }
                futureTransferExists.complete(null);
              } else {
                LOG.warn("Error processing DestTransferExists: Commitment: {}, Error: {}", commitmentS, th.toString());
                futureTransferExists.completeExceptionally(th);
              }
            });
            return null;
          });
        }
      });

      this.lastBlockChecked = endBlockNumber;

    } catch (Exception ex) {
      throw new Error(ex);
    }
  }

  private void postCommitmentToDestination(Erc20HtlcTransfer.SourceTransferInitEventResponse txInitEvent, String commitmentS) {
    TransferInformation info = new TransferInformation(
        true, txInitEvent.commitment, txInitEvent.sender, txInitEvent.tokenContract, txInitEvent.amount, txInitEvent.timeLock);
    // TODO check that the commitment doesn't exist in database. TODO what to do if it does?
    dataStore.addOrReplace(info);

    LOG.info("Submit new transfer to dest: Commitment: {}, Sender: {} , Token Contract: {}, Amount: {}, TimeLock: {}",
        commitmentS, txInitEvent.sender, txInitEvent.tokenContract, txInitEvent.amount, txInitEvent.timeLock);

    CompletableFuture<TransactionReceipt> futureTxr = destTransferContract.newTransferFromOtherBlockchain(txInitEvent.tokenContract, txInitEvent.sender, txInitEvent.amount, txInitEvent.commitment).sendAsync();
    Context context = vertx.getOrCreateContext();
    futureTxr.handleAsync((txr, th) -> {
      context.runOnContext(event -> {
        if (th == null) {
          if (txr.isStatusOK()) {
            LOG.info("New transfer to dest OK");
          }
          else {
            LOG.info("New transfer to dest failed: {}", txr.getStatus());
          }
          futureTxr.complete(null);
        } else {
          if (th instanceof TransactionException) {
            TransactionException ex = (TransactionException) th;
            LOG.error("New transfer to dest failed. Commitment: {}, Revert Reason: {}", commitmentS, RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
          }
          else {
            LOG.error("New transfer to dest failed: Commitment: {}, Error: {}", commitmentS, th.toString());
            futureTxr.completeExceptionally(th);
          }

        }
      });
      return null;
    });


  }


}
