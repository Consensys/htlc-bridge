package net.consensys.htlcbridge.voting;

import net.consensys.htlcbridge.common.AbstractWeb3Test;
import net.consensys.htlcbridge.voting.soliditywrappers.VotingAlgMajorityWhoVoted;
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

  protected VotingAlgMajorityWhoVoted deployVotingAlgMajorityWhoVoted() throws Exception {
    return VotingAlgMajorityWhoVoted.deploy(this.web3j, this.tm, this.freeGasProvider).send();
  }

  protected void waitForVotingPeriodToEnd(String votingTopic) throws Exception {
    // Given a voting period of 1 second, and a block period larger than that, we should
    // only have to wait for a block to be produced, so the block.timestamp will
    // register as greater than 1 second after the block containing the transaction
    // that submitted the voting proposal.
    int sleepInterval = 100; // 100 ms
    int maxWait = 100;
    boolean timedOut = true;
    for (int i = 0; i < maxWait; i++) {
      Thread.sleep(sleepInterval);
      if (this.votingContract.votePeriodExpired(votingTopic).send()) {
        timedOut = false;
        break;
      }
    }
    if (timedOut) {
      throw new Exception("voting period hasn't timed out");
    }
  }

}