package net.consensys.htlcbridge.itest.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.consensys.htlcbridge.admin.commands.AuthoriseERC20ForReceiver;
import net.consensys.htlcbridge.admin.commands.AuthoriseERC20ForTransfer;
import net.consensys.htlcbridge.admin.commands.DeployERC20Contract;
import net.consensys.htlcbridge.admin.commands.DeployTransferContract;
import net.consensys.htlcbridge.common.DynamicGasProvider;
import net.consensys.htlcbridge.common.KeyPairGen;
import net.consensys.htlcbridge.common.PRNG;
import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.itest.IntegrationTests;
import net.consensys.htlcbridge.openzeppelin.soliditywrappers.ERC20PresetFixedSupply;
import net.consensys.htlcbridge.relayer.Relayer;
import net.consensys.htlcbridge.relayer.RelayerConfig;
import net.consensys.htlcbridge.transfer.CommitmentCalculator;
import net.consensys.htlcbridge.transfer.ReceiverInfo;
import net.consensys.htlcbridge.transfer.TransferState;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.junit.Assert.assertEquals;

public abstract class IntegrationTestBase {
  private static final Logger LOG = LogManager.getLogger(IntegrationTestBase.class);

  public static final String MAINNET_BLOCKCHAIN_URI = "http://127.0.0.1:8400/";
  public static final String MAINNET_BLOCKCHAIN_ID = "40";
  public static final int MAINNET_BLOCK_PERIOD = 4000;
  public static final int MAINNET_CONFIRMATIONS = 3;

  public static final String SIDECHAIN_BLOCKCHAIN_URI = "http://127.0.0.1:8310/";
  public static final String SIDECHAIN_BLOCKCHAIN_ID = "31";
  public static final int SIDECHAIN_BLOCK_PERIOD = 2000;
  public static final int SIDECHAIN_CONFIRMATIONS = 1;

  public static final int API_PORT = 8080;

  public static final long DEFAULT_MAINNET_SOURCE_TIME_LOCK = 24 * 60 * 60; // 24 hours.
  public static final long DEFAULT_SIDECHAIN_DESTINATION_TIME_LOCK = 12 * 60 * 60; // 12 hours.
  public static final long DEFAULT_MAINNET_DESTINATION_TIME_LOCK = 12 * 60 * 60; // 24 hours.
  public static final long DEFAULT_SIDECHAIN_SOURCE_TIME_LOCK = 24 * 60 * 60; // 12 hours.


  public static final String TOK1_NAME = "Token 1";
  public static final String TOK1_SYMBOL = "Tok1";
  public static final long TOK1_TOTAL_SUPPLY = 100000;

  public static final String TOK2_NAME = "Token 2";
  public static final String TOK2_SYMBOL = "Tok2";
  public static final long TOK2_TOTAL_SUPPLY = 100000;

  // Addresses of transfer and receiver contracts on sidechain and MainNet.
  String transferSidechain;
  String transferMainNet;

  String[] userPKeys;
  String[] userAddresses;



  public String createPrivateKey() {
    return new KeyPairGen().generateKeyPairGetPrivateKey();
  }

  public String getAddress(String privateKey) {
    return Credentials.create(privateKey).getAddress();
  }

  public void createUsers(int numUsers) {
    this.userPKeys = new String[numUsers];
    this.userAddresses = new String[numUsers];
    for (int i=0; i<this.userPKeys.length; i++) {
      this.userPKeys[i] = createPrivateKey();
      this.userAddresses[i] = getAddress(this.userPKeys[i]);
    }
  }


  public void createInitialBalances(boolean mainNet, String erc20ContractAddress, String bankerPKey, boolean allSame, int balance) throws Exception {
    String bcName = mainNet ? "MainNet" : "Sidechain";
    for (int i = 0; i < this.userAddresses.length; i++) {
      long startingBalance = allSame ? balance : balance + i;
      LOG.info("Give user {}: {} of {} contract: {}", this.userAddresses[i], startingBalance, bcName, erc20ContractAddress);
      transferErc20Tokens(true, erc20ContractAddress, bankerPKey, this.userAddresses[i], startingBalance);
      BigInteger actualBalance = getBalanceErc20Tokens(true, erc20ContractAddress, this.userPKeys[i]);
      assertEquals(actualBalance.longValue(), startingBalance);
    }
  }

  public void approveTransferOfTokens(boolean mainNet, String erc20ContractAddress) throws Exception {
    for (int i = 0; i < this.userAddresses.length; i++) {
      BigInteger actualBalance = getBalanceErc20Tokens(true, erc20ContractAddress, this.userPKeys[i]);
      approveErc20Tokens(mainNet, erc20ContractAddress, this.userPKeys[i], transferMainNet, actualBalance.longValue());
    }

  }



  public String deployTransferContract(boolean deployOnMainNet, String pKeyOwner, long sourceTimeLock, long destTimeLock) throws Exception {
    String uri = deployOnMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcId = deployOnMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = deployOnMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);
    String sourceTimeLockStr = Long.toString(sourceTimeLock);
    String destTimeLockStr = Long.toString(destTimeLock);

    String[] args = new String[] {
        "ignored",
        uri,
        bcId,       // Chain ID
        pKeyOwner,
        blockPeriod,
        sourceTimeLockStr,
        destTimeLockStr};   // Timelock: 24 * 60 * 60 = 86400

    return DeployTransferContract.deploy(args);
  }

  public String deployErc20Contract(boolean deployOnMainNet, String tokenName, String tokenSymbol, long totalSupply, String pKeyOwner) throws Exception {
    String uri = deployOnMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcId = deployOnMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = deployOnMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);
    String totalSupplyStr = Long.toString(totalSupply);

    String[] args = new String[] {
        "ignored",
        uri,
        bcId,       // Chain ID
        pKeyOwner,
        blockPeriod,
        totalSupplyStr,
        tokenName,
        tokenSymbol};

    return DeployERC20Contract.deploy(args);
  }


  public void transferErc20Tokens(boolean onMainNet, String contractAddress, String fromPKey, String toAddress, long amount) throws Exception {
    String uri = onMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcIdStr = onMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = onMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);

    long bcId = Long.parseLong(bcIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;

    Web3j web3j;
    TransactionManager tm;
    // A gas provider which indicates no gas is charged for transactions.
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    Credentials from = Credentials.create(fromPKey);

    web3j = Web3j.build(new HttpService(uri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    tm = new RawTransactionManager(web3j, from, bcId, RETRY, pollingInterval);
    ERC20PresetFixedSupply erc20 = ERC20PresetFixedSupply.load(contractAddress, web3j, tm, freeGasProvider);
    TransactionReceipt txr = erc20.transfer(toAddress, BigInteger.valueOf(amount)).send();
    if (!txr.isStatusOK()) {
      throw new Exception("ERC 20 transfer failed");
    }
  }

  public BigInteger getBalanceErc20Tokens(boolean onMainNet, String contractAddress, String pKey) throws Exception {
    Credentials user = Credentials.create(pKey);
    return getBalanceErc20Tokens(onMainNet, contractAddress, pKey, user.getAddress());
  }


  public BigInteger getBalanceErc20Tokens(boolean onMainNet, String contractAddress, String pKey, String address) throws Exception {
    String uri = onMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcIdStr = onMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = onMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);

    long bcId = Long.parseLong(bcIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;

    Web3j web3j;
    TransactionManager tm;
    Credentials credentials;
    // A gas provider which indicates no gas is charged for transactions.
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    Credentials user = Credentials.create(pKey);

    web3j = Web3j.build(new HttpService(uri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    tm = new RawTransactionManager(web3j, user, bcId, RETRY, pollingInterval);
    ERC20PresetFixedSupply erc20 = ERC20PresetFixedSupply.load(contractAddress, web3j, tm, freeGasProvider);
    return erc20.balanceOf(address).send();
  }

  public void authoriseErc20TokensOnReceiver(boolean onMainNet, String receiverContractAddress, String pKey, String localErc20, String remoteErc20) throws Exception {
    String uri = onMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcIdStr = onMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = onMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);

    String[] args = new String[] {
        "ignored",
        uri,
        bcIdStr,       // Chain ID
        pKey,
        blockPeriod,
        receiverContractAddress,
        localErc20,
        remoteErc20};

    AuthoriseERC20ForReceiver.authorise(args);
  }

  public void authoriseErc20TokensOnTransfer(boolean onMainNet, String receiverContractAddress, String pKey, String localErc20) throws Exception {
    String uri = onMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcIdStr = onMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = onMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);

    String[] args = new String[] {
        "ignored",
        uri,
        bcIdStr,       // Chain ID
        pKey,
        blockPeriod,
        receiverContractAddress,
        localErc20};

    AuthoriseERC20ForTransfer.authorise(args);
  }

  // Approve of the transfer contract transferring tokens on behalf of the user.
  public void approveErc20Tokens(boolean onMainNet, String contractAddress, String userPKey, String authorisedAddress, long amount) throws Exception {
    String uri = onMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcIdStr = onMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = onMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);

    long bcId = Long.parseLong(bcIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;

    Web3j web3j;
    TransactionManager tm;
    Credentials credentials;
    // A gas provider which indicates no gas is charged for transactions.
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    Credentials user = Credentials.create(userPKey);

    web3j = Web3j.build(new HttpService(uri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    tm = new RawTransactionManager(web3j, user, bcId, RETRY, pollingInterval);
    ERC20PresetFixedSupply erc20 = ERC20PresetFixedSupply.load(contractAddress, web3j, tm, freeGasProvider);
    TransactionReceipt txr = erc20.approve(authorisedAddress, BigInteger.valueOf(amount)).send();
    if (!txr.isStatusOK()) {
      throw new Exception("ERC 20 approve failed");
    }
  }

  public void launchRelayer(String configFileName) throws Exception {
    Relayer.main(new String[]{configFileName});
  }


  public void configureRelayer(String domainOrIP, boolean fromMainNetToSidechain, String relayerPKey, int adminPort, long maxTimeLock) throws Exception {
    RelayerConfig conf = createRelayerConfig(fromMainNetToSidechain, relayerPKey, adminPort, maxTimeLock);
    String serializedConf = new ObjectMapper().writeValueAsString(conf);

    String uriStr = "http://" + domainOrIP + ":" + adminPort + "/conf/all";

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uriStr))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(serializedConf))
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() != 200) {
      LOG.error("Configure relayer failed: {}: {}", response.statusCode(), response.body());
      throw new Exception("Configure Relayer failed");
    }
    LOG.info("Successfully configured relayer: {}:{}", domainOrIP, adminPort);
  }

  public RelayerConfig createRelayerConfig(boolean fromMainNetToSidechain, String relayerPKey, int adminPort, long maxTimeLock) {
    String sourceBcUri = fromMainNetToSidechain ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String sourceTransferContract = fromMainNetToSidechain ? transferMainNet : transferSidechain;
    int sourceBlockPeriod = fromMainNetToSidechain ? MAINNET_BLOCK_PERIOD : SIDECHAIN_BLOCK_PERIOD;
    int sourceConfirmations = fromMainNetToSidechain ? MAINNET_CONFIRMATIONS : SIDECHAIN_CONFIRMATIONS;
    int sourceRetries = 5;
    String sourceGasStrategy = DynamicGasProvider.Strategy.FREE.toString();
    long sourceBcId = fromMainNetToSidechain ? Integer.valueOf(MAINNET_BLOCKCHAIN_ID) : Integer.valueOf(SIDECHAIN_BLOCKCHAIN_ID);

    String destBcUri = fromMainNetToSidechain ? SIDECHAIN_BLOCKCHAIN_URI : MAINNET_BLOCKCHAIN_URI;
    String destReceiverContract = fromMainNetToSidechain ? transferSidechain : transferMainNet;
    int destBlockPeriod = fromMainNetToSidechain ? SIDECHAIN_BLOCK_PERIOD : MAINNET_BLOCK_PERIOD;
    int destConfirmations = fromMainNetToSidechain ? SIDECHAIN_CONFIRMATIONS : MAINNET_CONFIRMATIONS;
    int destRetries = 5;
    String destGasStrategy = DynamicGasProvider.Strategy.FREE.toString();
    long destBcId = fromMainNetToSidechain ? Integer.valueOf(SIDECHAIN_BLOCKCHAIN_ID) : Integer.valueOf(MAINNET_BLOCKCHAIN_ID);

    return new RelayerConfig(
        sourceBcUri, sourceTransferContract, sourceBlockPeriod, sourceConfirmations,
        sourceRetries, sourceBcId, relayerPKey, sourceGasStrategy,
        destBcUri, destReceiverContract, destBlockPeriod, destConfirmations,
        destRetries, destBcId, relayerPKey, destGasStrategy,
        maxTimeLock,
        adminPort
    );
  }




  public byte[][] newTransfer(boolean fromMainNetToSidechain, String userPKey, String tokenContract, BigInteger amountToTransfer) throws Exception {
    String uri = fromMainNetToSidechain ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcIdStr = fromMainNetToSidechain ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = fromMainNetToSidechain ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);
    String transferContractAddress = fromMainNetToSidechain ? transferMainNet : transferSidechain;

    long bcId = Long.parseLong(bcIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;

    Web3j web3j;
    TransactionManager tm;
    // A gas provider which indicates no gas is charged for transactions.
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    Credentials user = Credentials.create(userPKey);
    web3j = Web3j.build(new HttpService(uri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    tm = new RawTransactionManager(web3j, user, bcId, RETRY, pollingInterval);
    Erc20HtlcTransfer transfer = Erc20HtlcTransfer.load(transferContractAddress, web3j, tm, freeGasProvider);


    Bytes preimageSalt = PRNG.getPublicRandomBytes32();
    byte[] preimageSaltBytes = preimageSalt.toArray();
    Bytes commitment = CommitmentCalculator.calculate(preimageSalt, user.getAddress(), tokenContract, amountToTransfer);
    byte[] commitmentBytes = commitment.toArray();

    TransactionReceipt txr;
    try {
      txr = transfer.newTransferToOtherBlockchain(tokenContract, amountToTransfer, commitmentBytes).send();
    } catch (TransactionException ex) {
      LOG.error("transfer.newTransferToOtherBlockchain reverted: {}", RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    if (!txr.isStatusOK()) {
      throw new Exception("Status not OK: newTransferToOtherBlockchain");
    }
    return new byte[][]{preimageSaltBytes, commitmentBytes};
  }


  public void waitForHtlcToBePostedToRelayer(boolean onMainNet, byte[] commitment, String userPKey) throws Exception {
    String uri = onMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcIdStr = onMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = onMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);
    String receiverContractAddress = onMainNet ? transferMainNet : transferSidechain;

    long bcId = Long.parseLong(bcIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;

    // A gas provider which indicates no gas is charged for transactions.
    Credentials user = Credentials.create(userPKey);
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);
    Web3j web3j = Web3j.build(new HttpService(uri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    TransactionManager tm = new RawTransactionManager(web3j, user, bcId, RETRY, pollingInterval);

    Erc20HtlcTransfer receiver = Erc20HtlcTransfer.load(receiverContractAddress, web3j, tm, freeGasProvider);

    boolean exists = false;
    for (int i=0; i<1000; i++) {
      LOG.info(" Waiting for transfer to be posted to receiver: {}", i);
      exists = receiver.destTransferExists(commitment).send();
      if (exists) {
        LOG.info(" Transfer has been posted to receiver!");
        break;
      }
      Thread.sleep(100);
    }
    if (!exists) {
      LOG.error(" timed out waiting for transfer to be posted to receiver!");
      throw new Exception("timed out waiting for transfer to be posted");
    }
  }

  public ReceiverInfo getDetailsFromReceiver(boolean onMainNet, byte[] commitment, String userPKey) throws Exception {
    String uri = onMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcIdStr = onMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = onMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);
    String receiverContractAddress = onMainNet ? transferMainNet : transferSidechain;

    long bcId = Long.parseLong(bcIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;

    // A gas provider which indicates no gas is charged for transactions.
    Credentials user = Credentials.create(userPKey);
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);
    Web3j web3j = Web3j.build(new HttpService(uri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    TransactionManager tm = new RawTransactionManager(web3j, user, bcId, RETRY, pollingInterval);

    Erc20HtlcTransfer receiver = Erc20HtlcTransfer.load(receiverContractAddress, web3j, tm, freeGasProvider);
    Tuple7<String, String, String, BigInteger, byte[], BigInteger, BigInteger> info = receiver.getDestInfo(commitment).send();
    return new ReceiverInfo(commitment, info);
  }


  public void postPreimage(boolean onMainNet, byte[] preimage, byte[] commitment, String userPKey) throws Exception {
    String uri = onMainNet ? MAINNET_BLOCKCHAIN_URI : SIDECHAIN_BLOCKCHAIN_URI;
    String bcIdStr = onMainNet ? MAINNET_BLOCKCHAIN_ID : SIDECHAIN_BLOCKCHAIN_ID;
    String blockPeriod = onMainNet ? Integer.toString(MAINNET_BLOCK_PERIOD) : Integer.toString(SIDECHAIN_BLOCK_PERIOD);
    String receiverContractAddress = onMainNet ? transferMainNet : transferSidechain;

    long bcId = Long.parseLong(bcIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;

    // A gas provider which indicates no gas is charged for transactions.
    Credentials user = Credentials.create(userPKey);
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);
    Web3j web3j = Web3j.build(new HttpService(uri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    TransactionManager tm = new RawTransactionManager(web3j, user, bcId, RETRY, pollingInterval);

    Erc20HtlcTransfer receiver = Erc20HtlcTransfer.load(receiverContractAddress, web3j, tm, freeGasProvider);
    TransactionReceipt txr;
    try {
      txr = receiver.finaliseTransferFromOtherBlockchain(commitment, preimage).send();
    } catch (TransactionException ex) {
      LOG.error(" Receiver: finaliseTransferFromOtherBlockchain reverted: {}", RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    if (!txr.isStatusOK()) {
      throw new Exception(" Receiver: finaliseTransferFromOtherBlockchain: Status not OK");
    }
    LOG.info(" Posting preimage successful");
  }


}
