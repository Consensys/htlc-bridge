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

import net.consensys.htlcbridge.common.RevertReason;
import net.consensys.htlcbridge.voting.soliditywrappers.VotingAlgMajorityWhoVoted;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SetVotingAlgTest extends AbstractVotingTest {
  public static final String ADDRESS_NOT_SET = "0x0000000000000000000000000000000000000000";
  private static final Logger LOG = LogManager.getLogger(SetVotingAlgTest.class);



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

  // Check that a voting algorithm can be set.
  @Test
  public void setVotingAlgorithmAndVotingPeriod() throws Exception {
    setupWeb3();
    deployVotingContract();

    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    BigInteger votingPeriod = BigInteger.valueOf(100);
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), votingPeriod).send();
    assertTrue(txr.isStatusOK());

    Tuple2<String, BigInteger> conf = this.votingContract.getVotingConfig().send();
    String votingAlgAddress = conf.component1();
    BigInteger actualVotingPeriod = conf.component2();

    assertEquals(votingAlgContract.getContractAddress(), votingAlgAddress);
    assertEquals(votingPeriod, actualVotingPeriod);
  }

  @Test
  public void setVotingAlgorithmInvalidAddress() throws Exception {
    setupWeb3();
    deployVotingContract();

    BigInteger votingPeriod = BigInteger.valueOf(100);
    try {
      this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), ADDRESS_NOT_SET, votingPeriod).send();
      throw new Error("Unexpectedly, no exception thrown");
    } catch (TransactionException ex) {
      LOG.info(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
    }
  }

  @Test
  public void setVotingAlgorithmNonVotingContract() throws Exception {
    setupWeb3();
    deployVotingContract();

    BigInteger votingPeriod = BigInteger.valueOf(100);
    try {
      this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), this.votingContract.getContractAddress(), votingPeriod).send();
      throw new Error("Unexpectedly, no exception thrown");
    } catch (TransactionException ex) {
      LOG.info(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
    }
  }

  @Test
  public void setVotingPeriodZero() throws Exception {
    setupWeb3();
    deployVotingContract();

    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    BigInteger votingPeriod = BigInteger.ZERO;
    try {
      this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), votingPeriod).send();
      throw new Error("Unexpectedly, no exception thrown");
    } catch (TransactionException ex) {
      LOG.info(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
    }
  }

}
