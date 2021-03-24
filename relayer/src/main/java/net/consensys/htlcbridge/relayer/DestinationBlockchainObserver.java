package net.consensys.htlcbridge.relayer;

import io.reactivex.Flowable;
import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class DestinationBlockchainObserver {
  private static final Logger LOG = LogManager.getLogger(DestinationBlockchainObserver.class);

  int destConfirmations;

  Erc20HtlcTransfer transferContract;
  Erc20HtlcTransfer receiverContract;
  Web3j sourceWeb3j;
  Web3j destWeb3j;

  long lastBlockChecked = -1;

  public DestinationBlockchainObserver(
      String sourceUri, String transferContractAddress, int sourceBlockPeriod, int sourceConfirmations,
      String sourcePKey, int sourceRetries, long sourceBcId, ContractGasProvider sourceGasProvider,
      String destUri, String receiverContractAddress, int destBlockPeriod, int destConfirmations,
      String destPKey, int destRetries, long destBcId, ContractGasProvider destGasProvider) {
    this.destConfirmations = destConfirmations;
    this.sourceWeb3j = Web3j.build(new HttpService(sourceUri), sourceBlockPeriod, new ScheduledThreadPoolExecutor(5));
    this.destWeb3j = Web3j.build(new HttpService(destUri), destBlockPeriod, new ScheduledThreadPoolExecutor(5));

    TransactionManager empty = null;
    Credentials relayerCredentials = Credentials.create(sourcePKey);
    TransactionManager sourceTm = new RawTransactionManager(this.sourceWeb3j, relayerCredentials, sourceBcId, sourceRetries, sourceBlockPeriod);

    this.transferContract = Erc20HtlcTransfer.load(transferContractAddress, sourceWeb3j, sourceTm, sourceGasProvider);
    this.receiverContract = Erc20HtlcTransfer.load(receiverContractAddress, destWeb3j, empty, null);
  }


  public void checkNewBlock() {
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
          this.receiverContract.destTransferCompletedEventFlowable(startBlock, endBlock);
      transferCompletedEvents.subscribe(new io.reactivex.functions.Consumer<Erc20HtlcTransfer.DestTransferCompletedEventResponse>() {
        @Override
        public void accept(Erc20HtlcTransfer.DestTransferCompletedEventResponse event) throws Exception {
          LOG.info("Receiver: Completed Transfer: Commitment: {}, Preimage: {}", event.commitment, event.preimage);
          // TODO consider adding information to the datastore.
          LOG.info("Finalising transfer at source");

          try {
            TransactionReceipt txr = transferContract.finaliseTransferToOtherBlockchain(event.commitment, event.preimage).send();
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
