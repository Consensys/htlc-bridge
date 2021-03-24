package net.consensys.htlcbridge.transfer;

import org.web3j.tuples.generated.Tuple7;

import java.math.BigInteger;
import java.util.function.BiFunction;

public class ReceiverInfo {
  private byte[] commitment;
  private String relayerAddress;
  private String recipientAddress;
  private String tokenContractOtherBlockchain;
  private BigInteger amount;
  private byte[] preimage;
  private BigInteger timeLock;
  private TransferState state;

  public ReceiverInfo(byte[] commitment, Tuple7<String, String, String, BigInteger, byte[], BigInteger, BigInteger> info) {
    this.commitment = commitment;
    this.relayerAddress = info.component1();
    this.recipientAddress = info.component2();
    this.tokenContractOtherBlockchain = info.component3();
    this.amount = info.component4();
    this.preimage = info.component5();
    this.timeLock = info.component6();
    this.state = TransferState.create(info.component7());
  }

  public byte[] getCommitment() {
    return commitment;
  }

  public String getRelayerAddress() {
    return relayerAddress;
  }

  public String getRecipientAddress() {
    return recipientAddress;
  }

  public String getTokenContractOtherBlockchain() {
    return tokenContractOtherBlockchain;
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
        ", Recipient: " + this.recipientAddress +
        ", Token: " + this.tokenContractOtherBlockchain +
        ", Amount: " + this.amount +
        ", Timelock: " + this.timeLock +
        ", State: " + this.state +
        ", RelayerAddr: " + this.relayerAddress;
  }
}
