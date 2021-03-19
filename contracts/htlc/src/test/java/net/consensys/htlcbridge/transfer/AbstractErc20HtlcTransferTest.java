/*
 * Copyright 2019 ConsenSys Software Inc
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

import net.consensys.htlcbridge.common.AbstractWeb3Test;
import net.consensys.htlcbridge.transfer.soliditywrappers.Erc20HtlcTransfer;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Check operation, assuming the calls are single blockchain (that is not part of a
 * cross-blockchain) call.
 */
public class AbstractErc20HtlcTransferTest extends AbstractWeb3Test {
  Erc20HtlcTransfer transferContract;

  public static final BigInteger TEST_TIMELOCK = BigInteger.ONE;

  protected void deployContract() throws Exception {
    this.transferContract = Erc20HtlcTransfer.deploy(this.web3j, this.tm, this.freeGasProvider, TEST_TIMELOCK).send();
  }


  @Test
  public void checkDeployment() throws Exception {
    setupWeb3();
    deployContract();

    assertEquals(TEST_TIMELOCK, this.transferContract.timeLockPeriod().send());
  }

}