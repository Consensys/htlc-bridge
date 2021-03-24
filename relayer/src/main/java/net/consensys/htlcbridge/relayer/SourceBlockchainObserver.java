package net.consensys.htlcbridge.relayer;

import io.reactivex.Flowable;
import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.relayer.data.DataStore;
import net.consensys.htlcbridge.relayer.data.TransferInformation;
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

public class SourceBlockchainObserver {
  private static final Logger LOG = LogManager.getLogger(SourceBlockchainObserver.class);

  int sourceConfirmations;

  Erc20HtlcTransfer transferContract;
  Erc20HtlcTransfer receiverContract;
  Web3j sourceWeb3j;
  Web3j destWeb3j;

  DataStore dataStore = DataStore.getInstance();

  long lastBlockChecked = -1;

  public SourceBlockchainObserver(
      String sourceUri, String transferContractAddress, int sourceBlockPeriod, int sourceConfirmations,
      String sourcePKey, int sourceRetries, long sourceBcId, ContractGasProvider sourceGasProvider,
      String destUri, String receiverContractAddress, int destBlockPeriod, int destConfirmations, String destPKey,
      int destRetries, long destBcId, ContractGasProvider destGasProvider) {
    this.sourceConfirmations = sourceConfirmations;
    this.sourceWeb3j = Web3j.build(new HttpService(sourceUri), sourceBlockPeriod, new ScheduledThreadPoolExecutor(5));
    this.destWeb3j = Web3j.build(new HttpService(destUri), destBlockPeriod, new ScheduledThreadPoolExecutor(5));

    TransactionManager empty = null;
    Credentials relayerCredentials = Credentials.create(destPKey);
    TransactionManager destTm = new RawTransactionManager(this.destWeb3j, relayerCredentials, destBcId, destRetries, destBlockPeriod);

    this.transferContract = Erc20HtlcTransfer.load(transferContractAddress, sourceWeb3j, empty, null);
    this.receiverContract = Erc20HtlcTransfer.load(receiverContractAddress, destWeb3j, destTm, destGasProvider);
  }


  public void checkNewBlock() {
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
          this.transferContract.sourceTransferInitEventFlowable(startBlock, endBlock);



      transferInitEvents.subscribe(new io.reactivex.functions.Consumer<Erc20HtlcTransfer.SourceTransferInitEventResponse>() {
        @Override
        public void accept(Erc20HtlcTransfer.SourceTransferInitEventResponse event) throws Exception {
          LOG.info("Sender: {} , Token Contract: {}, Amount: {}, TimeLock: {}", event.sender, event.tokenContract, event.amount, event.timeLock);
          // TODO pass in direction.
          TransferInformation info = new TransferInformation(
              true, event.commitment, event.sender, event.tokenContract, event.amount, event.timeLock);
          // TODO check that the commitment doesn't exist in database. TODO what to do if it does?
          dataStore.addOrReplace(info);

          // TODO check that amount > 0
          // TODO check that timelock hasn't expired.
          try {
            TransactionReceipt txr = receiverContract.newTransferFromOtherBlockchain(event.tokenContract, event.sender, event.amount, event.commitment).send();
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
