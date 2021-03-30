package net.consensys.htlcbridge.transfer;

import net.consensys.htlcbridge.common.Hash;
import net.consensys.htlcbridge.common.PRNG;
import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.openzeppelin.soliditywrappers.ERC20PresetFixedSupply;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;
import org.junit.Test;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

public class Events extends AbstractErc20HtlcTransferTest {
  private static final Logger LOG = LogManager.getLogger(Events.class);

  @Test
  public void initTransferEvent() throws Exception {
    setupWeb3();
    deployTransferContract();
    ERC20PresetFixedSupply token1Erc20 = deployErc20Contract();

    // Add the token to the list of tokens that can be transferred.
    String tokenContractAddress = token1Erc20.getContractAddress();
    TransactionReceipt txr = this.transferContract.proposeVote(
        TransferVoteTypes.VOTE_ADD_SOURCE_ALLOWED_TOKEN.asBigInt(), tokenContractAddress, BigInteger.ZERO).send();
    if (!txr.isStatusOK()) {
      throw new Exception("Status not OK: addAllowedToken");
    }

    // Approve of the transfer contract transferring tokens on behalf of the user.
    String transferContractAddress = this.transferContract.getContractAddress();
    BigInteger amountToApprove = BigInteger.TEN;
    txr = token1Erc20.approve(transferContractAddress, amountToApprove).send();
    if (!txr.isStatusOK()) {
      throw new Exception("Status not OK: ERC20 Approve");
    }

    // Set-up HTLC / token transfer.
    BigInteger amountToTransfer = BigInteger.ONE;
    Bytes preimageSalt = PRNG.getPublicRandomBytes32();
    byte[] Salt = preimageSalt.toArray();
    Bytes commitment = CommitmentCalculator.calculate(preimageSalt, this.credentials.getAddress(), tokenContractAddress, amountToTransfer);
    byte[] commitmentBytes = commitment.toArray();
    long nowInSeconds = System.currentTimeMillis() / 1000;
    try {
      txr = this.transferContract.newTransferToOtherBlockchain(tokenContractAddress, amountToTransfer, commitmentBytes).send();
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    if (!txr.isStatusOK()) {
      throw new Exception("Status not OK: newTransferToOtherBlockchain");
    }
    List<Erc20HtlcTransfer.SourceTransferInitEventResponse> events = this.transferContract.getSourceTransferInitEvents(txr);

    assertEquals(1, events.size());
    Erc20HtlcTransfer.SourceTransferInitEventResponse event = events.get(0);
    assertEquals(amountToTransfer, event.amount);
    assertArrayEquals(commitmentBytes, event.commitment);
    assertEquals(this.credentials.getAddress(), event.sender);
    assertEquals(tokenContractAddress, event.tokenContract);
    LOG.info(nowInSeconds);
    LOG.info(event.timeLock);
    // Time lock will be
    assertTrue(nowInSeconds <= event.timeLock.longValue() &&
        nowInSeconds+BLOCK_PERIOD_OF_TEST_BLOCKCHAIN + TEST_TIMELOCK_INT+1 >= event.timeLock.longValue());
  }
}
