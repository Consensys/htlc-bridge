package net.consensys.htlcbridge.admin.commands;

import net.consensys.htlcbridge.admin.Admin;
import net.consensys.htlcbridge.common.KeyPairGen;
import net.consensys.htlcbridge.openzeppelin.soliditywrappers.ProxyAdmin;
import net.consensys.htlcbridge.openzeppelin.soliditywrappers.TransparentUpgradeableProxy;
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

    if (args.length != 7) {
      throw new Error("here");
//      Admin.showHelp();
//      return;
    }
    String blockchanUri = args[1];
    String blockchainIdStr = args[2];
    String privateKey = args[3];
    String blockPeriod = args[4];
    String sourceTimeLockStr = args[5];
    String destTimeLockStr = args[6];

    long bcId = Long.parseLong(blockchainIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;
    BigInteger sourceTimeLock = new BigInteger(sourceTimeLockStr);
    BigInteger destTimeLock = new BigInteger(destTimeLockStr);

    Web3j web3j;
    TransactionManager tm;
    Credentials credentials;
    // A gas provider which indicates no gas is charged for transactions.
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    credentials = Credentials.create(privateKey);

    web3j = Web3j.build(new HttpService(blockchanUri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    tm = new RawTransactionManager(web3j, credentials, bcId, RETRY, pollingInterval);

    try {
      Erc20HtlcTransfer transferContract = Erc20HtlcTransfer.deploy(web3j, tm, freeGasProvider).send();
      LOG.info(" Successfully deployed transfer contract to address: {}", transferContract.getContractAddress());
      ProxyAdmin proxyAdmin = ProxyAdmin.deploy(web3j, tm, freeGasProvider).send();
      LOG.info(" Successfully deployed proxy admin contract to address: {}", proxyAdmin.getContractAddress());
      TransparentUpgradeableProxy proxy = TransparentUpgradeableProxy.deploy(web3j, tm, freeGasProvider,
          transferContract.getContractAddress(), proxyAdmin.getContractAddress(), new byte[]{}).send();
      LOG.info(" Successfully deployed proxy contract to address: {}", proxy.getContractAddress());
      Erc20HtlcTransfer transferContractProxied = Erc20HtlcTransfer.load(proxy.getContractAddress(), web3j, tm, freeGasProvider);
      transferContractProxied.initialise(sourceTimeLock, destTimeLock).send();
      BigInteger version = transferContractProxied.version().send();
      LOG.info(" Successfully initialised transfer contract. Reporting version: {}", version);
      boolean authorised = transferContractProxied.isAuthorisedRelayer(credentials.getAddress()).send();
      LOG.info(" Relayer {} isAuthorised: {}", credentials.getAddress(), authorised);
      LOG.info(" Transfer contract proxies at address: {}", proxy.getContractAddress());
      return proxy.getContractAddress();
    }
    catch (Exception ex) {
      LOG.error(" Exception thrown while deploying transfer contract: {}", ex.getMessage());
      throw ex;
    }
  }
}
