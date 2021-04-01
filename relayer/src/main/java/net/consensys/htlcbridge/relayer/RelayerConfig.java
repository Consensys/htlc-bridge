package net.consensys.htlcbridge.relayer;


public class RelayerConfig {
  public String sourceBcUri;
  public String sourceTransferContract;
  public int sourceBlockPeriod;
  public int sourceConfirmations;
  public int sourceRetries;
  public long sourceBcId;
  public String sourceRelayerPKey;
  public String sourceGasStrategy;

  public String destBcUri;
  public String destTransferContract;
  public int destBlockPeriod;
  public int destConfirmations;
  public int destRetries;
  public long destBcId;
  public String destRelayerPKey;
  public String destGasStrategy;

  public long maxTimeLock;

  public int apiPort;

  // Default constructor needed for loading from JSON.
  public RelayerConfig() { }

  public RelayerConfig(String sourceBcUri,
      String sourceTransferContract,
      int sourceBlockPeriod,
      int sourceConfirmations,
      int sourceRetries,
      long sourceBcId,
      String sourceRelayerPKey,
      String sourceGasStrategy,

      String destBcUri,
      String destTransferContract,
      int destBlockPeriod,
      int destConfirmations,
      int destRetries,
      long destBcId,
      String destRelayerPKey,
      String destGasStrategy,

      long maxTimeLock,

      int apiPort) {
    this.sourceBcUri = sourceBcUri;
    this.sourceTransferContract = sourceTransferContract;
    this.sourceBlockPeriod = sourceBlockPeriod;
    this.sourceConfirmations = sourceConfirmations;
    this.sourceRetries = sourceRetries;
    this.sourceBcId = sourceBcId;
    this.sourceRelayerPKey = sourceRelayerPKey;
    this.sourceGasStrategy = sourceGasStrategy;

    this.destBcUri = destBcUri;
    this.destTransferContract = destTransferContract;
    this.destBlockPeriod = destBlockPeriod;
    this.destConfirmations = destConfirmations;
    this.destRetries = destRetries;
    this.destBcId = destBcId;
    this.destRelayerPKey = destRelayerPKey;
    this.destGasStrategy = destGasStrategy;

    this.maxTimeLock = maxTimeLock;

    this.apiPort = apiPort;
  }
}
