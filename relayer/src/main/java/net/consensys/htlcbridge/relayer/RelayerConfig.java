/*
 * Copyright 2021 ConsenSys Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
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
