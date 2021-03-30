package net.consensys.htlcbridge.transfer;

import net.consensys.htlcbridge.common.Hash;
import net.consensys.htlcbridge.common.PRNG;
import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.openzeppelin.soliditywrappers.ERC20PresetFixedSupply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HappyPath extends AbstractErc20HtlcTransferTest {
  private static final Logger LOG = LogManager.getLogger(HappyPath.class);

  // Note that this test assumes instant finality.
  @Test
  public void sourceStandardTransfer() throws Exception {
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
    byte[] preimageSaltBytes = preimageSalt.toArray();
    Bytes commitment = CommitmentCalculator.calculate(preimageSalt, this.credentials.getAddress(), tokenContractAddress, amountToTransfer);
    byte[] commitmentBytes = commitment.toArray();
    try {
      txr = this.transferContract.newTransferToOtherBlockchain(tokenContractAddress, amountToTransfer, commitmentBytes).send();
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    if (!txr.isStatusOK()) {
      throw new Exception("Status not OK: newTransferToOtherBlockchain");
    }

    // Complete the transfer.
    try {
      txr = this.transferContract.finaliseTransferToOtherBlockchain(commitmentBytes, preimageSaltBytes).send();
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    if (!txr.isStatusOK()) {
      throw new Exception("Status not OK: finaliseTransferToOtherBlockchain");
    }

    // Check that the transfer contract believes the transfer is completed.
    BigInteger transferState = this.transferContract.sourceTransferState(commitmentBytes).send();
    LOG.info("Transfer State: {}: {}", TransferState.create(transferState), transferState);
    assertTrue(TransferState.FINALILISED.equals(transferState));

    // Check that the balance was transferred in the ERC 20 contract.
    BigInteger balance = token1Erc20.balanceOf(transferContractAddress).send();
    assertEquals(amountToTransfer, balance);

    balance = token1Erc20.balanceOf(this.credentials.getAddress()).send();
    assertEquals(TEST_SUPPLY.subtract(amountToTransfer), balance);
  }



  // Note that this test assumes instant finality.
  @Test
  public void sourceRefundTransfer() throws Exception {
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
    Bytes commitment = CommitmentCalculator.calculate(preimageSalt, this.credentials.getAddress(), tokenContractAddress, amountToTransfer);
    byte[] commitmentBytes = commitment.toArray();
    try {
      txr = this.transferContract.newTransferToOtherBlockchain(tokenContractAddress, amountToTransfer, commitmentBytes).send();
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    if (!txr.isStatusOK()) {
      throw new Exception("Status not OK: newTransferToOtherBlockchain");
    }

    // Wait for time lock to expire. Given the 1ms timelock - it should have already expired.
    while (!this.transferContract.sourceTransferExpired(commitmentBytes).send()) {
      LOG.info("waiting for time lock to expire");
      Thread.sleep(100);
    }

    // Complete the transfer.
    try {
      txr = this.transferContract.refundTransferToOtherBlockchain(commitmentBytes).send();
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    if (!txr.isStatusOK()) {
      throw new Exception("Status not OK: finaliseTransferToOtherBlockchain");
    }

    // Check that the transfer contract believes the transfer was refunded.
    BigInteger transferState = this.transferContract.sourceTransferState(commitmentBytes).send();
    assertTrue(TransferState.REFUNDED.equals(transferState));

    // Check that the balance was not transferred in the ERC 20 contract.
    BigInteger balance = token1Erc20.balanceOf(transferContractAddress).send();
    assertEquals(BigInteger.ZERO, balance);

    balance = token1Erc20.balanceOf(this.credentials.getAddress()).send();
    assertEquals(TEST_SUPPLY, balance);
  }


}
