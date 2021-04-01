package net.consensys.htlcbridge.relayer.api;


public class SlotConfig {
  public int relayerOffset;
  public int numRelayers;

  // Default constructor needed for JSON decode.
  public SlotConfig() {}

  public SlotConfig(int numRelayers, int relayerOffset) {
    this.numRelayers = numRelayers;
    this.relayerOffset = relayerOffset;
  }
}
