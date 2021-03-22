package net.consensys.htlcbridge.relayer;

import com.fasterxml.jackson.databind.node.BigIntegerNode;
import io.reactivex.Flowable;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class SourceBlockchainObserver {
  private static final Logger LOG = LogManager.getLogger(SourceBlockchainObserver.class);

  int confirmations;

  Erc20HtlcTransfer transferContract;
  Web3j web3j;

  long lastBlockChecked = -1;

  public SourceBlockchainObserver(String uri, String transferContractAddress, int blockPeriod, int confirmations) {
    this.confirmations = confirmations;
    this.web3j = Web3j.build(new HttpService(uri), blockPeriod, new ScheduledThreadPoolExecutor(5));

    TransactionManager empty = null;
    this.transferContract = Erc20HtlcTransfer.load(transferContractAddress, web3j, empty, null);
  }


  public void checkNewBlock() {
    LOG.info("Source Blockchain Observer checking for new blocks.");
    try {
      EthBlockNumber ethBlockNumber = this.web3j.ethBlockNumber().send();
      BigInteger blockNumber = ethBlockNumber.getBlockNumber();
      LOG.info("Current Block Number: {}", blockNumber);
      long currentBlockNumber = blockNumber.longValue();

      // Check for events between last block checked and current block - number of confirmations
      if (this.lastBlockChecked > currentBlockNumber - this.confirmations) {
        LOG.info("No new blocks to process");
        return;
      }

      BigInteger startBlockNum = BigInteger.valueOf(this.lastBlockChecked + 1);
      DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(startBlockNum);
      BigInteger endBlockNum = BigInteger.valueOf(currentBlockNumber - this.confirmations);
      DefaultBlockParameter endBlock = DefaultBlockParameter.valueOf(endBlockNum);
      LOG.info("Requesting events from blocks {} to {}", startBlockNum, endBlockNum);
      Flowable<Erc20HtlcTransfer.TransferInitEventResponse> transferInitEvents =
          this.transferContract.transferInitEventFlowable(startBlock, endBlock);



      transferInitEvents.subscribe(new io.reactivex.functions.Consumer<Erc20HtlcTransfer.TransferInitEventResponse>() {
        @Override
        public void accept(Erc20HtlcTransfer.TransferInitEventResponse event) throws Exception {
          LOG.info("Sender: {} , Token Contract: {}, Amount: {}, TimeLock: {}", event.sender, event.tokenContract, event.amount, event.timeLock);
//          public byte[] commitment;
        }
      });

    } catch (Exception ex) {
      throw new Error(ex);
    }

  }
}
