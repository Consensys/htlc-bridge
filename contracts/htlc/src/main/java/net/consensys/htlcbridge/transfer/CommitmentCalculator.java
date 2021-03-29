/*
 * Copyright 2021 ConsenSys Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package net.consensys.htlcbridge.transfer;

import net.consensys.htlcbridge.common.Hash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;

import java.math.BigInteger;

public class CommitmentCalculator {
  private static final Logger LOG = LogManager.getLogger(CommitmentCalculator.class);

  // Preimage Salt MUST be 32 bytes.
  public static Bytes calculate(Bytes preimageSalt, String userAddress, String tokenAddress, BigInteger amount) throws Exception {
    Bytes userAddressBytes = addressToFixedLengthByteArray(userAddress);
    Bytes tokenAddressBytes = addressToFixedLengthByteArray(tokenAddress);
    Bytes amountBytes = numberToFixedLengthByteArray(amount);

    Bytes preimage = Bytes.concatenate(preimageSalt, userAddressBytes, tokenAddressBytes, amountBytes);
    LOG.trace("Preimage: {}", preimage);
    return Hash.keccak256(preimage);
  }

  public static Bytes addressToFixedLengthByteArray(String address) {
    final int ADDRESS_LEN = 20;
    return hexStringToFixedLengthByteArray(address, ADDRESS_LEN);
  }

  public static Bytes numberToFixedLengthByteArray(BigInteger number) throws Exception {
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

  public static Bytes hexStringToFixedLengthByteArray(String value, int fixedLen) {
    // LOG.trace("hexStringToFixedLengthByteArray: value: {}, fixedLen: {}", value, fixedLen);
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
