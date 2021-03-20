package net.consensys.htlcbridge.transfer;

import java.math.BigInteger;

public enum TransferState {
  OPEN(0),
  REFUNDED(1),
  FINALILISED(2);

  int state;

  private TransferState(int state) {
    this.state = state;
  }

  public boolean equals(TransferState other) {
    return other.state == this.state;
  }

  public boolean equals(BigInteger other) {
    return other.longValue() == this.state;
  }
}
