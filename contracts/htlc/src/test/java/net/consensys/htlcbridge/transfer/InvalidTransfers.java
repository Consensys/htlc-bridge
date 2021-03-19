package net.consensys.htlcbridge.transfer;

import net.consensys.htlcbridge.common.RevertReason;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;



import java.math.BigInteger;

public class InvalidTransfers extends AbstractErc20HtlcTransferTest {
  private static final Logger LOG = LogManager.getLogger(AbstractErc20HtlcTransferTest.class);

  @Test
  public void invalidToken() throws Exception {
    setupWeb3();
    deployContract();

    // Use the transfer contract's address as a valid address that should fail.
    String invalidTokenContractAddress = this.transferContract.getContractAddress();

    BigInteger amount = BigInteger.ONE;

    byte[] commitment = new byte[32];


    try {
      TransactionReceipt txR = this.transferContract.newTransferToOtherBlockchain(invalidTokenContractAddress, amount, commitment).send();
    } catch (TransactionException ex) {
      LOG.error(" Revert Reason: {}", RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));

    }

  }


// tranfer exists
  // negative amount
  // no allowance
  // Token not in allowed set.

}
