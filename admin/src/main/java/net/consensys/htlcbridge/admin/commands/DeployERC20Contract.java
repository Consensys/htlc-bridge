package net.consensys.htlcbridge.admin.commands;

import net.consensys.htlcbridge.admin.Admin;
import net.consensys.htlcbridge.common.KeyPairGen;
import net.consensys.htlcbridge.openzeppelin.soliditywrappers.ERC20PresetFixedSupply;
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

public class DeployERC20Contract {
  private static final Logger LOG = LogManager.getLogger(Admin.class);


  public static String deploy(String[] args) {
    LOG.info("Deploy Transfer Contract");

    if (args.length != 8) {
      Admin.showHelp();
      return null;
    }
    String blockchanUri = args[1];
    String blockchainIdStr = args[2];
    String privateKey = args[3];
    String blockPeriod = args[4];
    String totalSupplyStr = args[5];
    String tokenName = args[6];
    String tokenSymbol = args[7];

    long bcId = Long.parseLong(blockchainIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;
    BigInteger totalSupply = new BigInteger(totalSupplyStr);

    Web3j web3j;
    TransactionManager tm;
    Credentials credentials;
    // A gas provider which indicates no gas is charged for transactions.
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    credentials = Credentials.create(privateKey);

    web3j = Web3j.build(new HttpService(blockchanUri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    tm = new RawTransactionManager(web3j, credentials, bcId, RETRY, pollingInterval);

    String ownerOfSupply = credentials.getAddress();


    try {
      ERC20PresetFixedSupply erc20 = ERC20PresetFixedSupply.deploy(web3j, tm, freeGasProvider,
        tokenName, tokenSymbol, totalSupply, ownerOfSupply).send();
      LOG.info("Successfully deployed ERC20 contract to address: {}", erc20.getContractAddress());
      return erc20.getContractAddress();
    }
    catch (Exception ex) {
      LOG.error("Exception thrown while deploying ERC20 contract: {}", ex.getMessage());
      return null;
    }
  }
}
