package net.consensys.htlcbridge.relayer;

import io.vertx.core.Vertx;
import net.consensys.htlcbridge.common.DynamicGasProvider;
import net.consensys.htlcbridge.relayer.data.DataStore;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public abstract class BlockchainObserver {
  private static final Logger LOG = LogManager.getLogger(BlockchainObserver.class);

  Vertx vertx;

  protected int sourceConfirmations;
  protected int destConfirmations;

  protected Erc20HtlcTransfer srcTransferContract;
  protected Erc20HtlcTransfer destTransferContract;
  protected Web3j sourceWeb3j;
  protected Web3j destWeb3j;

  protected DataStore dataStore = DataStore.getInstance();

  protected long lastBlockChecked;

  protected int numRelayers = 1;
  protected int relayerOffset = 0;
//  int relayCounter = 0;

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


  public void setRelayers(int numRelayers, int relayerOffset) {
    this.numRelayers = numRelayers;
    this.relayerOffset = relayerOffset;
  }

  public int getNumRelayers() {
    return this.numRelayers;
  }

  public int getRelayerOffset() {
    return this.relayerOffset;
  }

//  public boolean fairnessSkip() {
//    //
//
//  }
//
//  /**
//   * Ethereum MainNet has probabilistic finality, whereas the Sidechain has immediate finality.
//   * The block period for Ethereum MainNet is 12 seconds and for the Sidechain is 2 seconds.
//   *
//   *
//   * For the destination:
//   *
//   * Blockchain's Block Period
//   * numRelayers is used as the number of seconds to do the algorithm over.
//   * relayerOffset is the
//   *
//   *
//   * @return
//   */
//  public long determineFairnessWait() {
//
//
//    long relayCounter = System.currentTimeMillis() / 1000;
////    relayCounter = relayCounter % this.numRelayers;
////    if (relayCounter != this.relayerOffset) {
////      LOG.info("Source Blockchain Observer fairness skipping: {}: {} of {}", this.relayerOffset, relayCounter, this.numRelayers);
////      return;
////    }
//
//  }
//

  public abstract void checkNewBlock();
}
