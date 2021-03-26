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
import net.consensys.htlcbridge.voting.soliditywrappers.VotingTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;

import java.math.BigInteger;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Check that the numVotes and whoVoted functions work as expected.
 */
public class WhoVotedTest extends AbstractVotingTest {
  public static final String ADDRESS_NOT_SET = "0x0000000000000000000000000000000000000000";
  private static final Logger LOG = LogManager.getLogger(WhoVotedTest.class);


  @Test
  public void noVote() throws Exception {
    setupWeb3();
    deployVotingContract();

    // Step 1 is configure a voting contract.
    BigInteger longTime = BigInteger.valueOf(100);
    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    BigInteger votingPeriod = longTime;
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), votingPeriod).send();
    assertTrue(txr.isStatusOK());

    // Now check information for a vote that hasn't happened.
    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();

    BigInteger numVotedFor = this.votingContract.numVotes(cred2Address, true).send();
    assertEquals(BigInteger.ZERO, numVotedFor);
    BigInteger numVotedAgainst = this.votingContract.numVotes(cred2Address, false).send();
    assertEquals(BigInteger.ZERO, numVotedAgainst);

    // Calling who voted will fail as the array offset will be out of bounds
    try {
      this.votingContract.whoVoted(cred2Address, true, BigInteger.ZERO).send();
      LOG.error("Unexpectly, no exception thrown");
      throw new Error();
    } catch (ContractCallException ex) {
      LOG.info("Error thrown as expected");
    }

  }


  @Test
  public void oneVoter() throws Exception {
    setupWeb3();
    deployVotingContract();

    // Step 1 is configure a voting contract.
    BigInteger longTime = BigInteger.valueOf(100);
    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    BigInteger votingPeriod = longTime;
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), votingPeriod).send();
    assertTrue(txr.isStatusOK());

    // Step 2 is propose a vote.
    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Now we should be able to check the vote.
    String voter = this.votingContract.whoVoted(cred2Address, true, BigInteger.ZERO).send();
    assertEquals(this.credentials.getAddress(), voter);
    BigInteger numVotedFor = this.votingContract.numVotes(cred2Address, true).send();
    assertEquals(BigInteger.ONE, numVotedFor);
    BigInteger numVotedAgainst = this.votingContract.numVotes(cred2Address, false).send();
    assertEquals(BigInteger.ZERO, numVotedAgainst);
  }

  @Test
  public void twoVoters() throws Exception {
    setupWeb3();
    deployVotingContract();

    // Add an extra admin
    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Now set a voting algorithm
    BigInteger longTime = BigInteger.valueOf(100);
    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    BigInteger votingPeriod = longTime;
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), votingPeriod).send();
    assertTrue(txr.isStatusOK());

    // Vote on something
    receipt = this.votingContract.proposeVote(VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Vote for it.
    TransactionManager cred2Tm = new RawTransactionManager(this.web3j, credentials2, BLOCKCHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
    VotingTest cred2VotingContract = VotingTest.load(this.votingContract.getContractAddress(), this.web3j, cred2Tm, this.freeGasProvider);
    try {
      receipt = cred2VotingContract.vote(VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred2Address, true).send();
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    assert(receipt.isStatusOK());

    // Now we should be able to check the vote.
    String voter = this.votingContract.whoVoted(cred2Address, true, BigInteger.ZERO).send();
    assertEquals(this.credentials.getAddress(), voter);
    voter = this.votingContract.whoVoted(cred2Address, true, BigInteger.ONE).send();
    assertEquals(credentials2.getAddress(), voter);
    BigInteger numVotedFor = this.votingContract.numVotes(cred2Address, true).send();
    assertEquals(BigInteger.TWO, numVotedFor);
    BigInteger numVotedAgainst = this.votingContract.numVotes(cred2Address, false).send();
    assertEquals(BigInteger.ZERO, numVotedAgainst);
  }

  @Test
  public void oneForOneAgainstVoters() throws Exception {
    oneForOneAgainstVoters(100);
  }
  public String oneForOneAgainstVoters(int votingPeriod) throws Exception {
    setupWeb3();
    deployVotingContract();

    // Add an extra admin
    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Now set a voting algorithm
    BigInteger longTime = BigInteger.valueOf(votingPeriod);
    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), longTime).send();
    assertTrue(txr.isStatusOK());

    // Vote on something
    receipt = this.votingContract.proposeVote(VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Vote against it.
    TransactionManager cred2Tm = new RawTransactionManager(this.web3j, credentials2, BLOCKCHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
    VotingTest cred2VotingContract = VotingTest.load(this.votingContract.getContractAddress(), this.web3j, cred2Tm, this.freeGasProvider);
    try {
      receipt = cred2VotingContract.vote(VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred2Address, false).send();
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    assert(receipt.isStatusOK());

    // Now we should be able to check the vote.
    String voter = this.votingContract.whoVoted(cred2Address, true, BigInteger.ZERO).send();
    assertEquals(this.credentials.getAddress(), voter);
    voter = this.votingContract.whoVoted(cred2Address, false, BigInteger.ZERO).send();
    assertEquals(credentials2.getAddress(), voter);
    BigInteger numVotedFor = this.votingContract.numVotes(cred2Address, true).send();
    assertEquals(BigInteger.ONE, numVotedFor);
    BigInteger numVotedAgainst = this.votingContract.numVotes(cred2Address, false).send();
    assertEquals(BigInteger.ONE, numVotedAgainst);

    return cred2Address;
  }

  @Test
  public void oneForTwoAgainstVoters() throws Exception {
    setupWeb3();
    deployVotingContract();

    // Add an extra admin
    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Add an extra admin
    Credentials credentials3 = createNewIdentity();
    String cred3Address = credentials3.getAddress();
    receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred3Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Now set a voting algorithm
    BigInteger longTime = BigInteger.valueOf(100);
    VotingAlgMajorityWhoVoted votingAlgContract = deployVotingAlgMajorityWhoVoted();
    BigInteger votingPeriod = longTime;
    TransactionReceipt txr = this.votingContract.proposeVote(VoteTypes.VOTE_CHANGE_VOTING.asBigInt(), votingAlgContract.getContractAddress(), votingPeriod).send();
    assertTrue(txr.isStatusOK());

    // Vote on something
    receipt = this.votingContract.proposeVote(VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Vote against it.
    TransactionManager cred2Tm = new RawTransactionManager(this.web3j, credentials2, BLOCKCHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
    VotingTest cred2VotingContract = VotingTest.load(this.votingContract.getContractAddress(), this.web3j, cred2Tm, this.freeGasProvider);
    try {
      receipt = cred2VotingContract.vote(VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred2Address, false).send();
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    assert(receipt.isStatusOK());

    // Vote against it.
    TransactionManager cred3Tm = new RawTransactionManager(this.web3j, credentials3, BLOCKCHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
    VotingTest cred3VotingContract = VotingTest.load(this.votingContract.getContractAddress(), this.web3j, cred3Tm, this.freeGasProvider);
    try {
      receipt = cred3VotingContract.vote(VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred2Address, false).send();
    } catch (TransactionException ex) {
      LOG.error(RevertReason.decodeRevertReason(ex.getTransactionReceipt().get().getRevertReason()));
      throw ex;
    }
    assert(receipt.isStatusOK());

    // Now we should be able to check the vote.
    String voter = this.votingContract.whoVoted(cred2Address, true, BigInteger.ZERO).send();
    assertEquals(this.credentials.getAddress(), voter);
    voter = this.votingContract.whoVoted(cred2Address, false, BigInteger.ZERO).send();
    assertEquals(credentials2.getAddress(), voter);
    voter = this.votingContract.whoVoted(cred2Address, false, BigInteger.ONE).send();
    assertEquals(credentials3.getAddress(), voter);
    BigInteger numVotedFor = this.votingContract.numVotes(cred2Address, true).send();
    assertEquals(BigInteger.ONE, numVotedFor);
    BigInteger numVotedAgainst = this.votingContract.numVotes(cred2Address, false).send();
    assertEquals(BigInteger.TWO, numVotedAgainst);
  }


  @Test
  public void afterVote() throws Exception {
    String votingTopic = oneForOneAgainstVoters(POLLING_INTERVAL * 2 / 1000);

    waitForVotingPeriodToEnd(votingTopic);

    TransactionReceipt txr = this.votingContract.actionVotes(votingTopic).send();
    assertTrue(txr.isStatusOK());

    BigInteger numVotedFor = this.votingContract.numVotes(votingTopic, true).send();
    assertEquals(BigInteger.ZERO, numVotedFor);
    BigInteger numVotedAgainst = this.votingContract.numVotes(votingTopic, false).send();
    assertEquals(BigInteger.ZERO, numVotedAgainst);

    // Calling who voted will fail as the array offset will be out of bounds
    try {
      this.votingContract.whoVoted(votingTopic, true, BigInteger.ZERO).send();
      LOG.error("Unexpectly, no exception thrown");
      throw new Error();
    } catch (ContractCallException ex) {
      LOG.info("Error thrown as expected");
    }

  }


}
