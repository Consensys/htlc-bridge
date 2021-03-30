package net.consensys.htlcbridge.transfer;


import java.math.BigInteger;

public enum TransferVoteTypes {
    VOTE_NONE(0),                            // 0: MUST be the first value so it is the zero / deleted value.
    VOTE_ADD_ADMIN(1),
    VOTE_REMOVE_ADMIN(2),
    VOTE_CHANGE_VOTING(3),
    VOTE_SOURCE_TIMELOCK(100),
    VOTE_DEST_TIMELOCK(101),
    VOTE_ADD_SOURCE_ALLOWED_TOKEN(102),
    VOTE_REMOVE_SOURCE_ALLOWED_TOKEN(103),
    VOTE_ADD_DEST_ALLOWED_TOKEN(104),
    VOTE_REMOVE_DEST_ALLOWED_TOKEN(105);

    private int val;

    private TransferVoteTypes(int value) {
      this.val = value;
    }

    public BigInteger asBigInt() {
      return BigInteger.valueOf(this.val);
    }
}
