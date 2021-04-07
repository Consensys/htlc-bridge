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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import net.consensys.htlcbridge.relayer.api.RestAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.File;

public class Relayer extends AbstractVerticle {
  private static final Logger LOG = LogManager.getLogger(Relayer.class);

  public SourceBlockchainObserver sourceBlockchainObserver;
  int sourceBlockPeriod;
  public DestinationBlockchainObserver destBlockchainObserver;
  int destBlockPeriod;

  RestAPI api;
  int port;

  public Relayer(File configFile) throws Exception {
    this((RelayerConfig) (new ObjectMapper().readerFor(RelayerConfig.class).readValue(configFile)));
  }

  public Relayer(RelayerConfig config) throws Exception {
    this.sourceBlockchainObserver = new SourceBlockchainObserver(
        config.sourceBcUri, config.sourceTransferContract, config.sourceBlockPeriod, config.sourceConfirmations,
        config.sourceRelayerPKey, config.sourceRetries, config.sourceBcId, config.sourceGasStrategy,
        config.destBcUri, config.destTransferContract, config.destBlockPeriod, config.destConfirmations,
        config.destRelayerPKey, config.destRetries, config.destBcId, config.destGasStrategy);
    this.sourceBlockPeriod = config.sourceBlockPeriod;

    this.destBlockchainObserver = new DestinationBlockchainObserver(
        config.sourceBcUri, config.sourceTransferContract, config.sourceBlockPeriod, config.sourceConfirmations,
        config.sourceRelayerPKey, config.sourceRetries, config.sourceBcId, config.sourceGasStrategy,
        config.destBcUri, config.destTransferContract, config.destBlockPeriod, config.destConfirmations,
        config.destRelayerPKey, config.destRetries, config.destBcId, config.destGasStrategy);
    this.destBlockPeriod = config.destBlockPeriod;

    this.port = config.apiPort;

    this.api = new RestAPI(this);
  }

  /**
   * Initialise the verticle.<p>
   * This is called by Vert.x when the verticle instance is deployed. Don't call it yourself.
   * @param vertx  the deploying Vert.x instance
   * @param context  the context of the verticle
   */
  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    this.sourceBlockchainObserver.init(vertx);
    this.destBlockchainObserver.init(vertx);
  }


  @Override
  public void start() {
    LOG.info("Started");

    // Check for new blocks each 100ms.
    this.vertx.setPeriodic(1000, counter -> {
      this.sourceBlockchainObserver.checkNewBlock();
    });

    this.vertx.setPeriodic(5000, counter -> {
      this.destBlockchainObserver.checkNewBlock();
    });

    HttpServer server = this.vertx.createHttpServer();
    Router router = Router.router(this.vertx);
    this.api.createAPI(router);
    server.requestHandler(router).listen(this.port);
  }

  @Override
  public void stop() {
    LOG.info("Shutting down");
  }



  public static void main (String[] args) throws Exception {
    System.out.println("Starting Relayer");
    LOG.info("Relayer start-up commenced");
    if (args.length != 1) {
      System.out.println("Please provide one parameter: a config file name");
      return;
    }

    String filename = args[0];
    LOG.info("Configuration file: " + filename);
    File configFile = new File(filename);
    Relayer relayer = new Relayer(configFile);

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(relayer);
  }
}
