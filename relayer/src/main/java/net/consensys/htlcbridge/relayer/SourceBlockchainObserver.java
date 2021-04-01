package net.consensys.htlcbridge.relayer;

import io.reactivex.Flowable;
import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.relayer.data.TransferInformation;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.math.BigInteger;

public class SourceBlockchainObserver extends BlockchainObserver {
  private static final Logger LOG = LogManager.getLogger(SourceBlockchainObserver.class);

  public SourceBlockchainObserver(
      String sourceUri, String transferContractAddress, int sourceBlockPeriod, int sourceConfirmations,
      String sourcePKey, int sourceRetries, long sourceBcId, String sourceGasStrategy,
      String destUri, String receiverContractAddress, int destBlockPeriod, int destConfirmations,
      String destPKey, int destRetries, long destBcId, String destGasStrategy) throws Exception {
    super(sourceUri, transferContractAddress, sourceBlockPeriod, sourceConfirmations,
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

    LOG.info("Source Blockchain Observer checking for new blocks.");
    try {
      EthBlockNumber ethBlockNumber = this.sourceWeb3j.ethBlockNumber().send();
      BigInteger blockNumber = ethBlockNumber.getBlockNumber();
      LOG.info("Current Block Number: {}", blockNumber);
      long currentBlockNumber = blockNumber.longValue();

      // Check for events between last block checked and current block - number of confirmations
      if (this.lastBlockChecked > currentBlockNumber - this.sourceConfirmations) {
        LOG.info("No new blocks to process");
        return;
      }

      BigInteger startBlockNum = BigInteger.valueOf(this.lastBlockChecked + 1);
      DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(startBlockNum);
      long endBlockNumber = currentBlockNumber - this.sourceConfirmations;
      BigInteger endBlockNum = BigInteger.valueOf(endBlockNumber);
      DefaultBlockParameter endBlock = DefaultBlockParameter.valueOf(endBlockNum);
      LOG.info("Requesting events from blocks {} to {}", startBlockNum, endBlockNum);
      Flowable<Erc20HtlcTransfer.SourceTransferInitEventResponse> transferInitEvents =
          this.srcTransferContract.sourceTransferInitEventFlowable(startBlock, endBlock);



      transferInitEvents.subscribe(new io.reactivex.functions.Consumer<Erc20HtlcTransfer.SourceTransferInitEventResponse>() {
        @Override
        public void accept(Erc20HtlcTransfer.SourceTransferInitEventResponse event) throws Exception {
          LOG.info("Sender: {} , Token Contract: {}, Amount: {}, TimeLock: {}", event.sender, event.tokenContract, event.amount, event.timeLock);
          // TODO pass in direction.
          TransferInformation info = new TransferInformation(
              true, event.commitment, event.sender, event.tokenContract, event.amount, event.timeLock);
          // TODO check that the commitment doesn't exist in database. TODO what to do if it does?
          dataStore.addOrReplace(info);

          // Check whether another relayer has already submitted this transfer.
          if (destTransferContract.destTransferExists(event.commitment).send()) {
            LOG.info(" Transfer commitment already communicated to deastination");
            return;
          }

          // TODO check that amount > 0
          // TODO check that timelock hasn't expired.
          try {
            TransactionReceipt txr = destTransferContract.newTransferFromOtherBlockchain(event.tokenContract, event.sender, event.amount, event.commitment).send();
            if (!txr.isStatusOK()) {
              LOG.error("receiver.newTransferFromOtherBlockchain transaction failed");
            }
          } catch (TransactionException ex) {
            LOG.error("receiver.newTransferFromOtherBlockchain: Revert Reason: {}", RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
          }
        }
      });

      this.lastBlockChecked = endBlockNumber;

    } catch (Exception ex) {
      throw new Error(ex);
    }

  }
}
