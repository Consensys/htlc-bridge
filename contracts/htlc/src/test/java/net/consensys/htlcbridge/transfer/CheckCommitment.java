package net.consensys.htlcbridge.transfer;

import net.consensys.htlcbridge.common.PRNG;
import net.consensys.htlcbridge.openzeppelin.soliditywrappers.ERC20PresetFixedSupply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckCommitment extends AbstractErc20HtlcTransferTest {
  private static final Logger LOG = LogManager.getLogger(CheckCommitment.class);

  // Note that this test assumes instant finality.
  @Test
  public void checkCommitmentCalculation() throws Exception {
    setupWeb3();
    deployTransferContract();
    ERC20PresetFixedSupply token1Erc20 = deployErc20Contract();
    String tokenContractAddress = token1Erc20.getContractAddress();
    String userAddress = this.credentials.getAddress();

    check(userAddress, tokenContractAddress, BigInteger.ONE);
    check(userAddress, tokenContractAddress, BigInteger.valueOf(0x1f));
    check(userAddress, tokenContractAddress, BigInteger.valueOf(0xff));
    check(userAddress, tokenContractAddress, BigInteger.valueOf(0x10f));
    check(userAddress, tokenContractAddress, BigInteger.valueOf(0xffff));
    check(userAddress, tokenContractAddress, new BigInteger("ffffffffffffffff", 16));
  }

  void check(String userAddress, String tokenContractAddress, BigInteger amountToTransfer) throws Exception {

    Bytes preimageSalt = PRNG.getPublicRandomBytes32();
    Bytes commitment = CommitmentCalculator.calculate(preimageSalt, userAddress, tokenContractAddress, amountToTransfer);
    LOG.info("Commitment: {}", commitment);
    try {
      boolean commitmentMatches = this.transferContract.preimageMatchesCommitment(
          commitment.toArray(), preimageSalt.toArray(), userAddress, tokenContractAddress, amountToTransfer).send();
      assertTrue(commitmentMatches);
    } catch (Exception ex) {
//      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
  }

}
