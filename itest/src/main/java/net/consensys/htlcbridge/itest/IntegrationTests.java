package net.consensys.htlcbridge.itest;

import net.consensys.htlcbridge.admin.Admin;
import net.consensys.htlcbridge.admin.commands.DeployTransferContract;
import net.consensys.htlcbridge.common.KeyPairGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;

public class IntegrationTests {
  private static final Logger LOG = LogManager.getLogger(IntegrationTests.class);

  public static final String SOURCE_BLOCKCHAIN_URI = "http://127.0.0.1:8400/";
  public static final String SOURCE_BLOCKCHAIN_ID = "40";
  public static final int SOURCE_BLOCK_PERIOD = 4000;
  public static final int SOURCE_CONFIRMATIONS = 3;
  public String sourceTransferContract;

  public static final String DESTINATION_BLOCKCHAIN_URI = "http://127.0.0.1:8400/";
  public static final String DESTINATION_BLOCKCHAIN_ID = "40";
  public static final int DESTINATION_BLOCK_PERIOD = 2000;
  public static final int DESTINATION_CONFIRMATIONS = 1;
  public static final String DESTINATION_RECEIVER_CONTRACT = "";

  public static final int API_PORT = 8080;




  public void deploySourceTransferContract() {
    String[] args = new String[] {
        "ignored",
        SOURCE_BLOCKCHAIN_URI,
        SOURCE_BLOCKCHAIN_ID,       // Chain ID
        new KeyPairGen().generateKeyPairGetPrivateKey(),
        Integer.toString(SOURCE_BLOCK_PERIOD),     // Block Period
        "86400"};   // Timelock: 24 * 60 * 60 = 86400

    this.sourceTransferContract = DeployTransferContract.deploy(args);
  }


  public String deployErc20Contract(boolean deployOnSource, String tokenName, String tokenSymbol, String totalSupply, String pKeyOwner) {
    String uri = deployOnSource ? SOURCE_BLOCKCHAIN_URI : DESTINATION_BLOCKCHAIN_URI;
    String bcId = deployOnSource ? SOURCE_BLOCKCHAIN_ID : DESTINATION_BLOCKCHAIN_ID;
    String blockPeriod = deployOnSource ? Integer.toString(SOURCE_BLOCK_PERIOD) : Integer.toString(DESTINATION_BLOCK_PERIOD);

    String[] args = new String[] {
        "ignored",
        uri,
        bcId,       // Chain ID
        pKeyOwner,
        blockPeriod,
        totalSupply,
        tokenName,
        tokenSymbol};

    return DeployTransferContract.deploy(args);
  }


  public static void main() {

  }
}
