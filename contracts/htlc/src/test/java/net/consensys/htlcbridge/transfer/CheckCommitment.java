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
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.math.BigInteger;
import java.util.Arrays;

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
    Bytes commitment = calculateCommitment(preimageSalt, userAddress, tokenContractAddress, amountToTransfer);
    LOG.info("Commitment: {}", commitment);
    try {
      boolean commitmentMatches = this.transferContract.preimageMatchesCommitment1(
          commitment.toArray(), preimageSalt.toArray(), userAddress, tokenContractAddress, amountToTransfer).send();
      assertTrue(commitmentMatches);
    } catch (Exception ex) {
//      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
  }

  // Preimage Salt MUST be 32 bytes.
  public Bytes calculateCommitment(Bytes preimageSalt, String userAddress, String tokenAddress, BigInteger amount) throws Exception {
    Bytes userAddressBytes = addressToFixedLengthByteArray(userAddress);
    Bytes tokenAddressBytes = addressToFixedLengthByteArray(tokenAddress);
    Bytes amountBytes = numberToFixedLengthByteArray(amount);

    Bytes preimage = Bytes.concatenate(preimageSalt, userAddressBytes, tokenAddressBytes, amountBytes);
    LOG.info("Preimage: {}", preimage);
    return Hash.keccak256(preimage);
  }

  Bytes addressToFixedLengthByteArray(String address) {
    final int ADDRESS_LEN = 20;
    return hexStringToFixedLengthByteArray(address, ADDRESS_LEN);
  }

  Bytes numberToFixedLengthByteArray(BigInteger number) throws Exception {
    final int UINT256_LEN = 32;
    if (number.compareTo(BigInteger.ZERO) < 0) {
      throw new Exception("Amount negative");
    }
    number = number.abs();
    String str = number.toString(16);
    if ((str.length() & 1) == 1) {
      str = "0" + str;
    }
    return hexStringToFixedLengthByteArray(str, UINT256_LEN);
  }

  Bytes hexStringToFixedLengthByteArray(String value, int fixedLen) {
    LOG.info("hexStringToFixedLengthByteArray: value: {}, fixedLen: {}", value, fixedLen);
    byte[] bytes = Bytes.fromHexString(value).toArray();
    byte[] result;
    if (bytes.length == fixedLen) {
      result = bytes;
    }
    else {
      result = new byte[fixedLen];
      if (bytes.length < fixedLen) {
        System.arraycopy(bytes, 0, result, fixedLen - bytes.length, bytes.length);
      }
      else {
        System.arraycopy(bytes, bytes.length - fixedLen, result, 0, fixedLen);
      }
    }
    return Bytes.wrap(result);
  }
}
