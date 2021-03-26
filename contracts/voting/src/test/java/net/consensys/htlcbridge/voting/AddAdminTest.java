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
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

import static junit.framework.TestCase.assertFalse;

public class AddAdminTest extends AbstractVotingTest {

  @Test
  public void contractDeployerIsAdmin() throws Exception {
    setupWeb3();
    deployVotingContract();

    // The address that deployed the contract should be an admin
    assert(this.votingContract.isAdmin(this.credentials.getAddress()).send());
    // There should be only one admin.
    assert(this.votingContract.getNumAdmins().send().compareTo(BigInteger.ONE) == 0);
  }


  @Test
  public void addAdmin() throws Exception {
    setupWeb3();
    deployVotingContract();

    Credentials credentials2 = createNewIdentity();
    String cred2Address = credentials2.getAddress();

    TransactionReceipt receipt = this.votingContract.proposeVote(
        VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
    assert(receipt.isStatusOK());

    // The newly added address should be an admin.
    assert(this.votingContract.isAdmin(cred2Address).send());
    // There should now be two admins
    assert(this.votingContract.getNumAdmins().send().compareTo(BigInteger.TWO) == 0);
  }

  // Do not allow a non-admin to add an admin.
  @Test
  public void failAddAdminByNonAdmin() throws Exception {
    setupWeb3();
    deployVotingContract();

    Credentials credentials2 = createNewIdentity();
    TransactionManager tm2 = createTransactionManager(credentials2);
    VotingTest votingContract2 = loadContract(tm2);

    String cred2Address = credentials2.getAddress();

    try {
      TransactionReceipt receipt = votingContract2.proposeVote(
          VoteTypes.VOTE_ADD_ADMIN.asBigInt(), cred2Address, BigInteger.ZERO).send();
      assertFalse(receipt.isStatusOK());
    } catch (TransactionException ex) {
      // Do nothing.
    }

    // The add an admin should have failed.
    assertFalse(this.votingContract.isAdmin(cred2Address).send());
    // There should be only one admin.
    assert(this.votingContract.getNumAdmins().send().compareTo(BigInteger.ONE) == 0);
  }

  // Do not allow an admin to be added twice.
  @Test
  public void failAddSameAdminTwice() throws Exception {
    setupWeb3();
    deployVotingContract();

    try {
      TransactionReceipt receipt = this.votingContract.proposeVote(
          VoteTypes.VOTE_ADD_ADMIN.asBigInt(), credentials.getAddress(), BigInteger.ZERO).send();
      assertFalse(receipt.isStatusOK());
    } catch (TransactionException ex) {
      // Do nothing.
    }

    // There should be only one admin.
    assert(this.votingContract.getNumAdmins().send().compareTo(BigInteger.ONE) == 0);
  }

}
