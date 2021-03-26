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
package net.consensys.htlcbridge.voting;

import net.consensys.htlcbridge.voting.soliditywrappers.VotingTest;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

import static junit.framework.TestCase.assertFalse;

public class RemoveAdminTest extends AbstractVotingTest {

  @Test
  public void removeAdmin() throws Exception {
    setupWeb3();
    deployVotingContract();

    Credentials credentials2 = createNewIdentity();
    TransactionManager tm2 = createTransactionManager(credentials2);
    VotingTest votingContract2 = loadContract(tm2);

    String cred2Address = credentials2.getAddress();


    // Add an admin
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Remove the original admin
    receipt = votingContract2.proposeVote(
        VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), credentials.getAddress(), BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Check that the new admin is an admin and that the old admin is no longer an admin
    assertFalse(this.votingContract.isAdmin(this.credentials.getAddress()).send());
    assert(this.votingContract.isAdmin(cred2Address).send());
    assert(this.votingContract.getNumAdmins().send().compareTo(BigInteger.ONE) == 0);
  }


  @Test
  public void failCantRemoveSelf() throws Exception {
    setupWeb3();
    deployVotingContract();

    // Add an admin
    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();
    BigInteger cred2AddressBig = new BigInteger(cred2Address.substring(2), 16);

    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Remove the original admin using the original admin's credentials.
    try {
      receipt = this.votingContract.proposeVote(
          VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), credentials.getAddress(), BigInteger.ZERO).send();
      assertFalse(receipt.isStatusOK());
    } catch (TransactionException ex) {
      // Do nothing.
    }

    // Check that both are still admins
    assert(this.votingContract.isAdmin(this.credentials.getAddress()).send());
    assert(this.votingContract.isAdmin(cred2Address).send());
    assert(this.votingContract.getNumAdmins().send().compareTo(BigInteger.TWO) == 0);
  }


  // Do not allow a non-admin to remove an admin.
  @Test
  public void failRemoveByNonAdmin() throws Exception {
    setupWeb3();
    deployVotingContract();

    Credentials credentials2 = createNewIdentity();
    TransactionManager tm2 = createTransactionManager(credentials2);
    VotingTest votingContract2 = loadContract(tm2);

    Credentials credentials3 = createNewIdentity();

    String cred3Address = credentials3.getAddress();
    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred3Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // Check only added admins are admins
    assert(this.votingContract.isAdmin(this.credentials.getAddress()).send());
    assertFalse(this.votingContract.isAdmin(credentials2.getAddress()).send());
    assert(this.votingContract.isAdmin(cred3Address).send());

    // Remove the original admin using the original admin's credentials.
    try {
      receipt = votingContract2.proposeVote(
          VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred3Address, BigInteger.ZERO).send();
      assertFalse(receipt.isStatusOK());
    } catch (TransactionException ex) {
      // Do nothing.
    }

    // Check only added admins are admins
    assert(this.votingContract.isAdmin(this.credentials.getAddress()).send());
    assertFalse(this.votingContract.isAdmin(credentials2.getAddress()).send());
    assert(this.votingContract.isAdmin(cred3Address).send());
  }

  // Fail if the address to be removed is not an admin
  @Test
  public void failRemoveNonAdmin() throws Exception {
    setupWeb3();
    deployVotingContract();

    Credentials credentials3 = createNewIdentity();
    String cred3Address = credentials3.getAddress();

    try {
      TransactionReceipt receipt = this.votingContract.proposeVote(
          VoteTypes.VOTE_REMOVE_ADMIN.asBigInt(), cred3Address, BigInteger.ZERO).send();
      assertFalse(receipt.isStatusOK());
    } catch (TransactionException ex) {
      // Do nothing.
    }
  }

}
