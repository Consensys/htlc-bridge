package net.consensys.htlcbridge.relayer;

import io.reactivex.Flowable;
import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.transfer.TransferState;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.math.BigInteger;

public class DestinationBlockchainObserver extends BlockchainObserver {
  private static final Logger LOG = LogManager.getLogger(DestinationBlockchainObserver.class);

  public DestinationBlockchainObserver(
      String sourceUri, String transferContractAddress, int sourceBlockPeriod, int sourceConfirmations,
      String sourcePKey, int sourceRetries, long sourceBcId, String sourceGasStrategy,
      String destUri, String receiverContractAddress, int destBlockPeriod, int destConfirmations,
      String destPKey, int destRetries, long destBcId, String destGasStrategy) throws Exception {
    super(sourceUri, transferContractAddress, sourceBlockPeriod, sourceConfirmations,
        sourcePKey, sourceRetries, sourceBcId, sourceGasStrategy,
        destUri, receiverContractAddress, destBlockPeriod, destConfirmations,
        destPKey, destRetries, destBcId, destGasStrategy);
    setLackBlockChecked(destBlockPeriod);
  }

  private void setLackBlockChecked(int destBlockPeriod) throws Exception {
    BigInteger timeLockPeriod = this.destTransferContract.destTimeLockPeriod().send();
    BigInteger currentBlockNumber = this.destWeb3j.ethBlockNumber().send().getBlockNumber();
    long earliestBlockToCheck = currentBlockNumber.longValue() - (timeLockPeriod.longValue() * 1000 / destBlockPeriod);
    if (earliestBlockToCheck < 0) {
      this.lastBlockChecked = -1;
    }
    else {
      this.lastBlockChecked = earliestBlockToCheck;
    }
    LOG.info("Destination Observer: Initial Last Block Checked: Current: {}, TimeLock: {} Period: {}, Earliest: {}, Last: {}",
        currentBlockNumber, timeLockPeriod, destBlockPeriod, earliestBlockToCheck, this.lastBlockChecked);
  }


  public void checkNewBlock() {
//    this.relayCounter++;
//    this.relayCounter = this.relayCounter % this.numRelayers;
//    if (this.relayCounter != this.relayerOffset) {
//      LOG.info("Source Blockchain Observer fairness skipping: {}: {} of {}", this.relayerOffset, this.relayCounter, this.numRelayers);
//      return;
//    }

    LOG.info("Destination Blockchain Observer checking for new blocks.");
    try {
      EthBlockNumber ethBlockNumber = this.destWeb3j.ethBlockNumber().send();
      BigInteger blockNumber = ethBlockNumber.getBlockNumber();
      LOG.info("Current Block Number: {}", blockNumber);
      long currentBlockNumber = blockNumber.longValue();

      // Check for events between last block checked and current block - number of confirmations
      if (this.lastBlockChecked > currentBlockNumber - this.destConfirmations) {
        LOG.info("No new blocks to process");
        return;
      }

      BigInteger startBlockNum = BigInteger.valueOf(this.lastBlockChecked + 1);
      DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(startBlockNum);
      long endBlockNumber = currentBlockNumber - this.destConfirmations;
      BigInteger endBlockNum = BigInteger.valueOf(endBlockNumber);
      DefaultBlockParameter endBlock = DefaultBlockParameter.valueOf(endBlockNum);
      LOG.info("Requesting events from blocks {} to {}", startBlockNum, endBlockNum);
      Flowable<Erc20HtlcTransfer.DestTransferCompletedEventResponse> transferCompletedEvents =
          this.destTransferContract.destTransferCompletedEventFlowable(startBlock, endBlock);
      transferCompletedEvents.subscribe(new io.reactivex.functions.Consumer<Erc20HtlcTransfer.DestTransferCompletedEventResponse>() {
        @Override
        public void accept(Erc20HtlcTransfer.DestTransferCompletedEventResponse event) throws Exception {
          LOG.info("Receiver: Completed Transfer: Commitment: {}, Preimage: {}", event.commitment, event.preimage);
          // TODO consider adding information to the datastore.

          // Check whether another relayer has already submitted this transfer.
          BigInteger state = srcTransferContract.sourceTransferState(event.commitment).send();
          if (!TransferState.OPEN.equals(state)) {
            LOG.info(" Transfer state on source is: {}", TransferState.create(state));
            return;
          }

          LOG.info("Finalising transfer at source");

          try {
            TransactionReceipt txr = srcTransferContract.finaliseTransferToOtherBlockchain(event.commitment, event.preimage).send();
            if (!txr.isStatusOK()) {
              LOG.error("receiver.finaliseTransferToOtherBlockchain transaction failed");
            }
          } catch (TransactionException ex) {
            LOG.error("receiver.finaliseTransferToOtherBlockchain: Revert Reason: {}", RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
          }
        }
      });

      this.lastBlockChecked = endBlockNumber;

    } catch (Exception ex) {
      throw new Error(ex);
    }

  }
}
