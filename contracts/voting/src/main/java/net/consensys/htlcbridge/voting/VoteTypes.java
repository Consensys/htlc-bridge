package net.consensys.htlcbridge.voting;

import java.math.BigInteger;

public enum VoteTypes {
  VOTE_NONE(0),                            // 0: MUST be the first value so it is the zero / deleted value.
  VOTE_ADD_ADMIN(1),                       // 1
  VOTE_REMOVE_ADMIN(2),                    // 2
  VOTE_CHANGE_VOTING(3);                   // 3

  private int val;

  private VoteTypes(int value) {
    this.val = value;
  }

  public BigInteger asBigInt() {
    return BigInteger.valueOf(this.val);
  }
}
