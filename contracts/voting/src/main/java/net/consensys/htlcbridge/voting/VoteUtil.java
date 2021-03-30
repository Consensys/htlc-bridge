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
package net.consensys.htlcbridge.voting;

import java.math.BigInteger;

/**
 * Utilities that are useful when working with the voting contract.
 */
public class VoteUtil {

  /**
   * Additional Info is a uint256. Use this function to convert an address
   * (a String), to a uint256 (a Big Integer), so that the address can be
   * passed in as the Additional Info parameter.
   *
   * @param address
   * @return
   */
  public static BigInteger addressAsBigInt(String address) {
    if (address.startsWith("0x")) {
      return new BigInteger(address.substring(2), 16);
    }
    return new BigInteger(address, 16);
  }

}
