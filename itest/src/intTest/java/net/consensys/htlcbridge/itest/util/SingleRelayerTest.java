package net.consensys.htlcbridge.itest.util;

import net.consensys.htlcbridge.common.KeyPairGen;
import net.consensys.htlcbridge.transfer.ReceiverInfo;
import net.consensys.htlcbridge.transfer.TransferState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class SingleRelayerTest extends IntegrationTestBase {
  private static final Logger LOG = LogManager.getLogger(SingleRelayerTest.class);

  @Test
  public void singleTransfer() throws Exception {
    String bankerPKey = createPrivateKey();
    String relayerPKey = createPrivateKey();
    createUsers(1);

    int USER_TOK_1_BASE_STARTING_BALANCE = 10;

    LOG.info("Set-up MainNet: Deploy ERC20 contracts and give users some tokens");
    // An entity other than the relayers is the deployer of the ERC 20s on MainNet.
    // Relayer 1 is the deployer of the ERC 20s on the Sidechain.
    String erc20Tok1MainNet = deployErc20Contract(true, TOK1_NAME, TOK1_SYMBOL, TOK1_TOTAL_SUPPLY, bankerPKey);
    createInitialBalances(true, erc20Tok1MainNet, bankerPKey, true, USER_TOK_1_BASE_STARTING_BALANCE);

    LOG.info("Deploy ERC20 contracts and Receiver and Transfer contract on Sidechain");
    // Relayer 1 is the deployer of the ERC 20s on the Sidechain.
    String erc20Tok1Sidechain = deployErc20Contract(false, TOK1_NAME, TOK1_SYMBOL, TOK1_TOTAL_SUPPLY, relayerPKey);

    long mainnetSourceTimeLock = DEFAULT_MAINNET_SOURCE_TIME_LOCK;
    long sidechainDestinationTimeLock = DEFAULT_SIDECHAIN_DESTINATION_TIME_LOCK;
    long mainnetDestinationTimeLock = DEFAULT_MAINNET_DESTINATION_TIME_LOCK;
    long sidechainSourceTimeLock = DEFAULT_SIDECHAIN_SOURCE_TIME_LOCK;
    this.transferSidechain = deployTransferContract(false, relayerPKey, sidechainSourceTimeLock, sidechainDestinationTimeLock);

    LOG.info("Transfer all tokens on Sidechain to Receiver contract");
    transferErc20Tokens(false, erc20Tok1Sidechain, relayerPKey, this.transferSidechain, TOK1_TOTAL_SUPPLY);

    LOG.info("Deploy Transfer contract on MainNet");
    this.transferMainNet = deployTransferContract(true, relayerPKey, mainnetSourceTimeLock, mainnetDestinationTimeLock);

    LOG.info("Add token(s) to list of authorised ERC 20 contracts");
    // Always add tokens to receivers first.
    authoriseErc20TokensOnReceiver(false, transferSidechain, relayerPKey, erc20Tok1Sidechain, erc20Tok1MainNet);
    authoriseErc20TokensOnReceiver(true, transferMainNet, relayerPKey, erc20Tok1MainNet, erc20Tok1Sidechain);
    authoriseErc20TokensOnTransfer(false, transferSidechain, relayerPKey, erc20Tok1Sidechain);
    authoriseErc20TokensOnTransfer(true, transferMainNet, relayerPKey, erc20Tok1MainNet);

    LOG.info("Approve ERC20 transferfrom for Transfer contract on MainNet");
    approveTransferOfTokens(true, erc20Tok1MainNet);

    LOG.info("Sending config to Relayer");
    configureRelayer("localhost", true, relayerPKey, 8080, mainnetSourceTimeLock);

  }



//  public void runTest() throws Exception {
//
//
//
//
//    LOG.info("Writing Relayer config file");
//    writeRelayerConfigFile(true, relayer1PKey, 8080, mainnetSourceTimeLock, "relayer1.config");
//    LOG.info("Launching MainNet to Sidechain Relayer");
//    launchRelayer("relayer1.config");
////    this.relayerToSidechain.setRelayers(2, 0);
//
//    for (int i = 0; i < NUM_USERS; i++) {
//      int user = i;
//      Executors.newSingleThreadExecutor().execute(new Runnable() {
//        @Override
//        public void run() {
//          String userAddress = userAddresses[user];
//          String userPKey = userPKeys[user];
//          LOG.info("Started thread for user {}: {}", user, userAddress);
//          try {
//            byte[][] preimageSalts = new byte[NUM_TRANFERS][];
//            byte[][] commitments = new byte[NUM_TRANFERS][];
//
//            for (int i = 0; i < NUM_TRANFERS; i++) {
//              BigInteger amountToTransfer = BigInteger.valueOf(i + 1);
//              LOG.info("User {}: New Transfer {} ERC tokens ({}) to Sidechain", userAddress, i + 1, TOK1_NAME);
//              byte[][] result = newTransfer(true, userPKey, erc20Tok1MainNet, amountToTransfer);
//              preimageSalts[i] = result[0];
//              commitments[i] = result[1];
//            }
//
//            for (int i = 0; i < NUM_TRANFERS; i++) {
//              BigInteger amountToTransfer = BigInteger.valueOf(i + 1);
//              // TODO wait for destination confirmations before posting the preimage. Otherwise,
//              // an attacker could reorganise the destination blockchain, removing the transaction with the commitment.
//              // If the user reveals the preimage, the dishonest relayer could use it to cash in on the source chain?
//              waitForHtlcToBePostedToRelayer(false, commitments[i], userPKey);
//
//              LOG.info("User {}: Checking deal matches what was committed to.", userAddress);
//              ReceiverInfo receiverInfo = getDetailsFromReceiver(false, commitments[i], userPKey);
//              LOG.info(" Receiver Info: {}", receiverInfo.toString());
//              if (!amountToTransfer.equals(receiverInfo.getAmount())) {
//                LOG.error(" Transfer Amount did not match: Expected: {}, Actual: {}", amountToTransfer, receiverInfo.getAmount());
//                throw new Exception(" Transfer Amount did not match");
//              }
//              if (!userAddress.equalsIgnoreCase(receiverInfo.getRecipientAddress())) {
//                LOG.error(" Recipient did not match: Expected: {}, Actual: {}", userAddress, receiverInfo.getRecipientAddress());
//                throw new Exception(" Recipient did not match");
//              }
//              if (!erc20Tok1MainNet.equalsIgnoreCase(receiverInfo.getTokenContractOtherBlockchain())) {
//                LOG.error(" Token did not match: Expected: {}, Actual: {}", erc20Tok1MainNet, receiverInfo.getTokenContractOtherBlockchain());
//                throw new Exception(" Token did not match");
//              }
//              if (!receiverInfo.getState().equals(TransferState.OPEN)) {
//                LOG.error(" Unexpected transfer state: Expected: {}, Actual: {}", TransferState.OPEN, receiverInfo.getState());
//                throw new Exception(" Unexpected transfer state");
//              }
//              long now = System.currentTimeMillis() / 1000;
//              if (now > receiverInfo.getTimeLock().longValue()) {
//                LOG.error(" Transfer has timed out: Now: {}, TimeLock: {}", now, receiverInfo.getTimeLock());
//                throw new Exception(" Transfer has timed out");
//              }
//
//              LOG.info(" Balances Before Transfer on Sidechain for Token {}: {}", TOK1_NAME, erc20Tok1Sidechain);
//              LOG.info("  Receiver Contract: {}", getBalanceErc20Tokens(false, erc20Tok1Sidechain, relayer1PKey, transferSidechain));
//              LOG.info("  Relayer1: {}", getBalanceErc20Tokens(false, erc20Tok1Sidechain, relayer1PKey));
////      LOG.info("  Relayer2: {}", getBalanceErc20Tokens(false, erc20Tok1Sidechain, relayer2PKey));
//              LOG.info("  User2: {}", getBalanceErc20Tokens(false, erc20Tok1Sidechain, userPKey));
//
//              LOG.info("User {}: Posting preimage to Sidechain", userAddress);
//              postPreimage(false, preimageSalts[i], commitments[i], userPKey);
//
//              LOG.info(" Balances After Transfer on Sidechain for Token {}: {}", TOK1_NAME, erc20Tok1Sidechain);
//              LOG.info("  Receiver Contract: {}", getBalanceErc20Tokens(false, erc20Tok1Sidechain, relayer1PKey, transferSidechain));
//              LOG.info("  Relayer1: {}", getBalanceErc20Tokens(false, erc20Tok1Sidechain, relayer1PKey));
////      LOG.info("  Relayer2: {}", getBalanceErc20Tokens(false, erc20Tok1Sidechain, relayer2PKey));
//              LOG.info("  User2: {}", getBalanceErc20Tokens(false, erc20Tok1Sidechain, userPKey));
//            }
//
//          } catch (Exception ex) {
//            LOG.error("Error executing for user {}: {}", user, userAddress);
//
//          }
//        }
//      });
//    }
//
//
//
////
////    // Complete the transfer.
////    try {
////      txr = this.transferContract.finaliseTransferToOtherBlockchain(commitmentBytes, preimageBytes).send();
////    } catch (TransactionException ex) {
////      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
////      throw ex;
////    }
////    if (!txr.isStatusOK()) {
////      throw new Exception("Status not OK: finaliseTransferToOtherBlockchain");
////    }
////
////    // Check that the transfer contract believes the transfer is completed.
////    BigInteger transferState = this.transferContract.transferState(commitmentBytes).send();
////    assertTrue(TransferState.FINALILISED.equals(transferState));
////
////    // Check that the balance was transferred in the ERC 20 contract.
////    BigInteger balance = token1Erc20.balanceOf(transferContractAddress).send();
////    assertEquals(amountToTransfer, balance);
////
////    balance = token1Erc20.balanceOf(this.credentials.getAddress()).send();
////    assertEquals(TEST_SUPPLY.subtract(amountToTransfer), balance);
//
//  }
//

}
