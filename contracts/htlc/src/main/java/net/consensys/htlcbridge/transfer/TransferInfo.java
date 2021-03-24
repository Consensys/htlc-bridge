package net.consensys.htlcbridge.transfer;

import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;

public class TransferInfo {
  private byte[] commitment;
  private String senderAddress;
  private String tokenContract;
  private BigInteger amount;
  private byte[] preimage;
  private BigInteger timeLock;
  private TransferState state;

  public TransferInfo(byte[] commitment, Tuple6<String, String, BigInteger, byte[], BigInteger, BigInteger> info) {
    this.commitment = commitment;
    this.senderAddress = info.component1();
    this.tokenContract = info.component2();
    this.amount = info.component3();
    this.preimage = info.component4();
    this.timeLock = info.component5();
    this.state = TransferState.create(info.component6());
  }


    public byte[] getCommitment() {
    return commitment;
  }

  public String getSenderAddress() {
    return senderAddress;
  }

  public String getTokenContract() {
    return tokenContract;
  }

  public BigInteger getAmount() {
    return amount;
  }

  public byte[] getPreimage() {
    return preimage;
  }

  public BigInteger getTimeLock() {
    return timeLock;
  }

  public TransferState getState() {
    return state;
  }

  @Override
  public String toString() {
    String comStr = (new BigInteger(1, this.commitment)).toString(16);
    String preStr = (new BigInteger(1, this.preimage)).toString(16);

    return
        "Commitment: " + comStr +
        ", Preimage: " + preStr +
        ", Sender: " + this.senderAddress +
        ", Token: " + this.tokenContract +
        ", Amount: " + this.amount +
        ", Timelock: " + this.timeLock +
        ", State: " + this.state;
  }
}
