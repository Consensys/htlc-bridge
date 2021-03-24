package net.consensys.htlcbridge.admin.commands;

import net.consensys.htlcbridge.admin.Admin;
import net.consensys.htlcbridge.common.KeyPairGen;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class DeployTransferContract {
  private static final Logger LOG = LogManager.getLogger(DeployTransferContract.class);


  public static String deploy(String[] args) throws Exception {
    LOG.info("Deploy Transfer Contract");

    if (args.length != 6) {
      LOG.info("Using defaults while testing!");
      args = new String[]{"ignored",
          "http://127.0.0.1:8400/",
          "40",
          new KeyPairGen().generateKeyPairGetPrivateKey(),
          "2000",
          "86400"}; //24 * 60 * 60 = 86400
//      Admin.showHelp();
//      return;
    }
    String blockchanUri = args[1];
    String blockchainIdStr = args[2];
    String privateKey = args[3];
    String blockPeriod = args[4];
    String timeLockStr = args[5];

    long bcId = Long.parseLong(blockchainIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;
    BigInteger timeLock = new BigInteger(timeLockStr);

    Web3j web3j;
    TransactionManager tm;
    Credentials credentials;
    // A gas provider which indicates no gas is charged for transactions.
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    credentials = Credentials.create(privateKey);

    web3j = Web3j.build(new HttpService(blockchanUri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    tm = new RawTransactionManager(web3j, credentials, bcId, RETRY, pollingInterval);

    try {
      Erc20HtlcTransfer transferContract = Erc20HtlcTransfer.deploy(web3j, tm, freeGasProvider, timeLock).send();

      LOG.info("Successfully deployed transfer contract to address: {}", transferContract.getContractAddress());
      return transferContract.getContractAddress();
    }
    catch (Exception ex) {
      LOG.error("Exception thrown while deploying transfer contract: {}", ex.getMessage());
      throw ex;
    }
  }
}
