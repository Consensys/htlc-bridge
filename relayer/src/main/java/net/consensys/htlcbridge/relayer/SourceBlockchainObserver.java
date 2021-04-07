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

    setLackBlockCheckedInitialValue(sourceBlockPeriod);
  }

  private void setLackBlockCheckedInitialValue(int sourceBlockPeriod) throws Exception {
    BigInteger timeLockPeriod = this.srcTransferContract.sourceTimeLockPeriod().send();
    BigInteger currentBlockNumber = this.sourceWeb3j.ethBlockNumber().send().getBlockNumber();
    long earliestBlockToCheck = currentBlockNumber.longValue() - (timeLockPeriod.longValue() * 1000 / sourceBlockPeriod);
    long lastBlockCheckedL;
    if (earliestBlockToCheck < 0) {
      lastBlockCheckedL = -1;
    }
    else {
      lastBlockCheckedL = earliestBlockToCheck;
    }
    LOG.info("Source Observer: Initial Last Block Checked: Current: {}, TimeLock: {} Period: {}, Earliest: {}, Last: {}",
        currentBlockNumber, timeLockPeriod, sourceBlockPeriod, earliestBlockToCheck, lastBlockCheckedL);
    this.lastBlockChecked = new AtomicLong(lastBlockCheckedL);
  }


  public void checkNewBlock() {
      CompletableFuture<EthBlockNumber> futureEthBlockNumber = this.sourceWeb3j.ethBlockNumber().sendAsync();
      Context context = vertx.getOrCreateContext();
      futureEthBlockNumber.handle((ethBlockNumber, th) -> {
        context.runOnContext(event -> {
          if (th == null) {
            processNextBlock(ethBlockNumber);
          } else {
            LOG.error("Get block number failed: Error: {}", th.toString());
          }
        });
        return null;
      });
  }

  private void processNextBlock(EthBlockNumber ethBlockNumber) {
    BigInteger blockNumber = ethBlockNumber.getBlockNumber();
    long currentBlockNumber = blockNumber.longValue();

    // Check for events between last block checked and current block - number of confirmations
    long endBlockNumber = currentBlockNumber - this.sourceConfirmations;
    boolean successful;
    long startBlockNumber;
    do {
      long theLastBlockChecked = this.lastBlockChecked.get();
      startBlockNumber = theLastBlockChecked + 1;
      if (startBlockNumber > endBlockNumber) {
        LOG.info("Current Block: {}. No new blocks to process", blockNumber);
        return;
      }
      // Update lastBlockChecked, indicating that the block(s) lastBlockChecked and before are
      // being of will be processed.
      successful = this.lastBlockChecked.compareAndSet(theLastBlockChecked, endBlockNumber);
    } while (!successful);

    LOG.info("Current Block: {}. Processing blocks {} to {}", currentBlockNumber, startBlockNumber, endBlockNumber);
    DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlockNumber));
    DefaultBlockParameter endBlock = DefaultBlockParameter.valueOf(BigInteger.valueOf(endBlockNumber));
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
