package net.consensys.htlcbridge.relayer.data;

import net.consensys.htlcbridge.transfer.ReceiverInfo;
import net.consensys.htlcbridge.transfer.TransferInfo;
import net.consensys.htlcbridge.transfer.TransferState;

import java.math.BigInteger;

public class TransferInformation {
  private byte[] commitment;
  private byte[] preimage;
  private boolean fromMainNetToSidechain;
  private String userAddress;
  private String relayerAddress;
  private String mainNetTokenContract;
  private String sidechainTokenContract;
  private BigInteger amount;
  private BigInteger sourceTimeLock;
  private BigInteger destTimeLock;
  private TransferState sourceLastKnownState;
  private TransferState destLastKnownState;

  public TransferInformation(boolean fromMainNetToSidechain, byte[] commitment, String sender, String tokenContract, BigInteger amount, BigInteger timeLock) {
    this.commitment = commitment;
    this.fromMainNetToSidechain = fromMainNetToSidechain;
    this.userAddress = sender;
    this.mainNetTokenContract = tokenContract;
    this.amount = amount;
    this.sourceTimeLock = timeLock;
    this.sourceLastKnownState = TransferState.OPEN;
  }


  public TransferInformation(boolean fromMainNetToSidechain, TransferInfo txInfo) {
    this.commitment = txInfo.getCommitment();
    this.preimage = txInfo.getPreimage();
    this.fromMainNetToSidechain = fromMainNetToSidechain;
    this.userAddress = txInfo.getSenderAddress();
    this.mainNetTokenContract = txInfo.getTokenContract();
    this.amount = txInfo.getAmount();
    this.sourceTimeLock = txInfo.getTimeLock();
    this.sourceLastKnownState = txInfo.getState();
  }

  public void addReceiverInfo(ReceiverInfo rxInfo) {
    if (this.preimage == null) {
      this.preimage = rxInfo.getPreimage();
    }
    else {
      // TODO check they are the same.
    }
    // TODO check that sender and recepient are the same.
    // TODO check amount is the same
    this.destTimeLock = rxInfo.getTimeLock();
    this.destLastKnownState = rxInfo.getState();
  }

  public byte[] getCommitment() {
    return commitment;
  }

  public byte[] getPreimage() {
    return preimage;
  }

  public boolean isFromMainNetToSidechain() {
    return fromMainNetToSidechain;
  }

  public String getUserAddress() {
    return userAddress;
  }

  public String getRelayerAddress() {
    return relayerAddress;
  }

  public String getMainNetTokenContract() {
    return mainNetTokenContract;
  }

  public String getSidechainTokenContract() {
    return sidechainTokenContract;
  }

  public BigInteger getAmount() {
    return amount;
  }

  public BigInteger getSourceTimeLock() {
    return sourceTimeLock;
  }

  public BigInteger getDestTimeLock() {
    return destTimeLock;
  }

  public TransferState getSourceLastKnownState() {
    return sourceLastKnownState;
  }

  public TransferState getDestLastKnownState() {
    return destLastKnownState;
  }
}
