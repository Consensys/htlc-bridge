package net.consensys.htlcbridge.transfer;

import java.math.BigInteger;

public enum TransferState {
  OPEN(0),
  REFUNDED(1),
  FINALILISED(2),
  INVALID(3);

  int state;

  private TransferState(int state) {
    this.state = state;
  }

  public static TransferState create(BigInteger stateB) {
    long statel = stateB.longValue();
    switch ((int)statel) {
      case 0:
        return OPEN;
      case 1:
        return REFUNDED;
      case 2:
        return FINALILISED;
      default:
        return INVALID;
    }
  }

  public boolean equals(TransferState other) {
    return other.state == this.state;
  }

  public boolean equals(BigInteger other) {
    return other.longValue() == this.state;
  }
}
