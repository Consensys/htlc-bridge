package net.consensys.htlcbridge.voting;

import net.consensys.htlcbridge.common.AbstractWeb3Test;
import net.consensys.htlcbridge.voting.soliditywrappers.VotingTest;
import org.web3j.tx.TransactionManager;


public abstract class AbstractVotingTest extends AbstractWeb3Test {
  protected VotingTest votingContract;

  protected void deployVotingContract() throws Exception {
    this.votingContract = VotingTest.deploy(this.web3j, this.tm, this.freeGasProvider).send();
  }

  protected VotingTest loadContract(TransactionManager tm1) throws Exception {
    return VotingTest.load(this.votingContract.getContractAddress(), this.web3j, tm1, this.freeGasProvider);
  }

}