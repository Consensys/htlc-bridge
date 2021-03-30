package net.consensys.htlcbridge.admin.commands;

import net.consensys.htlcbridge.admin.Admin;
import net.consensys.htlcbridge.transfer.TransferVoteTypes;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class AuthoriseERC20ForReceiver {
  private static final Logger LOG = LogManager.getLogger(AuthoriseERC20ForReceiver.class);


  public static void authorise(String[] args) throws Exception {
    LOG.info("Authorise ERC20 Contract on Receiver");

    if (args.length != 8) {
      Admin.showHelp();
      return;
    }
    String blockchanUri = args[1];
    String blockchainIdStr = args[2];
    String privateKey = args[3];
    String blockPeriod = args[4];
    String receiverContractAddress = args[5];
    String localErc20ContractAddress = args[6];
    String remoteErc20ContractAddress = args[7];

    long bcId = Long.parseLong(blockchainIdStr);
    int pollingInterval = Integer.parseInt(blockPeriod);
    final int RETRY = 5;

    Web3j web3j;
    TransactionManager tm;
    Credentials credentials;
    // A gas provider which indicates no gas is charged for transactions.
    ContractGasProvider freeGasProvider = new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    credentials = Credentials.create(privateKey);

    web3j = Web3j.build(new HttpService(blockchanUri), pollingInterval, new ScheduledThreadPoolExecutor(5));
    tm = new RawTransactionManager(web3j, credentials, bcId, RETRY, pollingInterval);

    String ownerOfSupply = credentials.getAddress();


    try {
      BigInteger localErc20ContractAddressBigInt = addressAsBigInt(localErc20ContractAddress);
      Erc20HtlcTransfer receiverContract = Erc20HtlcTransfer.load(receiverContractAddress, web3j, tm, freeGasProvider);
      TransactionReceipt txr = receiverContract.proposeVote(
          TransferVoteTypes.VOTE_ADD_DEST_ALLOWED_TOKEN.asBigInt(), remoteErc20ContractAddress, localErc20ContractAddressBigInt).send();
      if (!txr.isStatusOK()) {
        throw new Exception("Unknown error processing request to authorise token contracts in receiver");
      }
    }
    catch(Exception ex){
        LOG.error("Exception while authorising token contracts in receiver: {}", ex.getMessage());
        throw ex;
    }
  }

  public static BigInteger addressAsBigInt(String address) {
    if (address.startsWith("0x")) {
      return new BigInteger(address.substring(2), 16);
    }
    return new BigInteger(address, 16);
  }
}