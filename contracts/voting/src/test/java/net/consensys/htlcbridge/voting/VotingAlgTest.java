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

import net.consensys.htlcbridge.voting.soliditywrappers.VotingTest;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

public class VotingAlgTest extends AbstractVotingTest {
  public static final String ADDRESS_NOT_SET = "0000";

  // Check that by default no voting algorithm is set.
  @Test
  public void defaultVotingAlg() throws Exception {
    setupWeb3();
    deployVotingContract();

    Tuple2<String, BigInteger> conf = this.votingContract.getVotingConfig().send();
    String votingAlgAddress = conf.component1();
    BigInteger votingPeriod = conf.component2();

    assertEquals(ADDRESS_NOT_SET, votingAlgAddress);
    assertEquals(BigInteger.ZERO, votingPeriod);
  }

}
