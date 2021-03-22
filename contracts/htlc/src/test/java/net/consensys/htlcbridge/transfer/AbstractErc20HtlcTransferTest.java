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
import net.consensys.htlcbridge.openzeppelin.soliditywrappers.ERC20PresetFixedSupply;
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

  public static final int BLOCK_PERIOD_OF_TEST_BLOCKCHAIN = 2;
  public static final int TEST_TIMELOCK_INT = 1;
  public static final BigInteger TEST_TIMELOCK = BigInteger.valueOf(TEST_TIMELOCK_INT);
  public static final BigInteger TEST_SUPPLY = BigInteger.valueOf(1000);

  protected void deployTransferContract() throws Exception {
    this.transferContract = Erc20HtlcTransfer.deploy(this.web3j, this.tm, this.freeGasProvider, TEST_TIMELOCK).send();
  }

  protected ERC20PresetFixedSupply deployErc20Contract() throws Exception {
    String tokenName = "Token1";
    String tokenSymbol = "TOK1";
    BigInteger totalSupply = TEST_SUPPLY;
    String ownerOfSupply = this.credentials.getAddress();
    return deployErc20Contract(tokenName, tokenSymbol, totalSupply, ownerOfSupply);
  }

  protected ERC20PresetFixedSupply deployErc20Contract(String tokenName, String tokenSymbol, BigInteger totalSupply, String ownerOfSupply) throws Exception {
    return ERC20PresetFixedSupply.deploy(this.web3j, this.tm, this.freeGasProvider, tokenName, tokenSymbol, totalSupply, ownerOfSupply).send();
  }


  @Test
  public void checkDeployment() throws Exception {
    setupWeb3();
    deployTransferContract();

    assertEquals(TEST_TIMELOCK, this.transferContract.timeLockPeriod().send());
  }

}