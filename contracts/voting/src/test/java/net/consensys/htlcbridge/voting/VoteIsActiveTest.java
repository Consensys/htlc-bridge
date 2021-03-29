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

import java.math.BigInteger;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test:
 * isVoteActive, voteType, endOfVotingPeriod, votePeriodExpired
 */
public class VoteIsActiveTest extends AbstractVotingTest {
  public static final String ADDRESS_NOT_SET = "0x0000000000000000000000000000000000000000";
  private static final Logger LOG = LogManager.getLogger(VoteIsActiveTest.class);

  @Test
  public void beforeVote() throws Exception {
    setupWeb3();
    deployVotingContract();

    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();

    checkEmpty(cred2Address);
  }

  @Test
  public void duringVote() throws Exception {
    setupWeb3();
    deployVotingContract();

    // Step 1 is configure a voting contract.
    long longTimeL = 100;
    BigInteger longTime = BigInteger.valueOf(longTimeL);
    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), longTime).send();
    assertTrue(txr.isStatusOK());

    // Step 2 is propose a vote.
    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();
    long now = System.currentTimeMillis() / 1000;
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    boolean voteIsActive = this.votingContract.isVoteActive(cred2Address).send();
    assertTrue(voteIsActive);

    BigInteger voteType = this.votingContract.voteType(cred2Address).send();
    assertEquals(VoteTypes.VOTE_ADD_ADMIN.asBigInt(), voteType);

    BigInteger votingPeriodEnd = this.votingContract.endOfVotingPeriod(cred2Address).send();
    long votingPeriodEndL = votingPeriodEnd.longValue();
    assertTrue(now+longTimeL <= votingPeriodEndL && now+longTimeL + POLLING_INTERVAL/1000 >= votingPeriodEndL);

    boolean votingPeriodExpired = this.votingContract.votePeriodExpired(cred2Address).send();
    assertFalse(votingPeriodExpired);
  }

  @Test
  public void afterVote() throws Exception {
    setupWeb3();
    deployVotingContract();

    // Step 1 is configure a voting contract.
    BigInteger shortTime = BigInteger.valueOf(1);
    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), shortTime).send();
    assertTrue(txr.isStatusOK());

    // Step 2 is propose a vote.
    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();
    LOG.info("Now: {}", System.currentTimeMillis());
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    txr = this.votingContract.actionVotes(cred2Address).send();
    assertTrue(txr.isStatusOK());

    checkEmpty(cred2Address);
  }

  @Test
  public void voteTypeRemoveAdmin() throws Exception {
    setupWeb3();
    deployVotingContract();

    // Add an admin
    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Configure a voting contract.
    long longTimeL = 100;
    BigInteger longTime = BigInteger.valueOf(longTimeL);
    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), longTime).send();
    assertTrue(txr.isStatusOK());

    // Propose a vote.
    try {
      receipt = this.votingContract.proposeVote(
          VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
      assert(receipt.isStatusOK());
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }

    BigInteger voteType = this.votingContract.voteType(cred2Address).send();
    assertEquals(VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), voteType);
  }

  @Test
  public void voteTypeChangeVote() throws Exception {
    setupWeb3();
    deployVotingContract();

    // Step 1 is configure a voting contract.
    long longTimeL = 100;
    BigInteger longTime = BigInteger.valueOf(longTimeL);
    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), longTime).send();
    assertTrue(txr.isStatusOK());

    // Step 2 is propose a vote: change the voting period.
    BigInteger longerTime = BigInteger.valueOf(1000);
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), longerTime).send();
    assert(receipt.isStatusOK());

    BigInteger voteType = this.votingContract.voteType(votingAlgContract.getContractAddress()).send();
    assertEquals(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), voteType);
  }


  private void checkEmpty(String votingTarget) throws Exception {
    boolean voteIsActive = this.votingContract.isVoteActive(votingTarget).send();
    assertFalse(voteIsActive);

    BigInteger voteType = this.votingContract.voteType(votingTarget).send();
    assertEquals(VoteTypes.VOTE_NONE.asBigInt(), voteType);

    BigInteger votingPeriodEnd = this.votingContract.endOfVotingPeriod(votingTarget).send();
    assertEquals(BigInteger.ZERO, votingPeriodEnd);

    boolean votingPeriodExpired = this.votingContract.votePeriodExpired(votingTarget).send();
    assertTrue(votingPeriodExpired);
  }

}
