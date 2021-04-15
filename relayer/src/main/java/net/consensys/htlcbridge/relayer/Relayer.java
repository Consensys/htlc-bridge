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
  public int sourceBlockPeriod;
  public DestinationBlockchainObserver destBlockchainObserver;
  public int destBlockPeriod;

  RestAPI api;
  int port;
  public RelayerConfig conf;

  public Relayer(File configFile) throws Exception {
    this((RelayerConfig) (new ObjectMapper().readerFor(RelayerConfig.class).readValue(configFile)));
  }

  public Relayer(RelayerConfig config) throws Exception {
    this(config.apiPort);
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

    this.conf = config;
  }

  public Relayer(int port) throws Exception {
    resetConfig();
    this.port = port;
    this.api = new RestAPI(this);
  }

  public void resetConfig() {
    this.sourceBlockchainObserver = null;
    this.sourceBlockPeriod = 0;
    this.destBlockchainObserver = null;
    this.destBlockPeriod = 0;

    this.conf = new RelayerConfig();
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
    if (args.length != 2) {
      LOG.error("Two parameters reqired:");
      LOG.error(" sh relayer CONF <config file name>");
      LOG.error(" sh relayer PORT <port number>");
      System.exit(-1);
      return; // Seems to be needed to allow static analysis tools to work.
    }

    Relayer relayer;
    String filenameOrPortOption = args[0];
    if (filenameOrPortOption.equalsIgnoreCase("CONF")) {
      String filename = args[1];
      LOG.info("Configuration file: " + filename);
      File configFile = new File(filename);
      relayer = new Relayer(configFile);
    }
    else if (filenameOrPortOption.equalsIgnoreCase("PORT")) {
      int port;
      try {
        port = Integer.parseInt(args[1]);
      } catch (NumberFormatException ex) {
        LOG.error("Invalid port number: {}", args[1]);
        System.exit(-1);
        return; // Seems to be needed to allow static analysis tools to work.
      }
      LOG.info("Configuration will be supplied via REST API on port {}", port);
      relayer = new Relayer(port);
    }
    else {
      LOG.error("Unknown option: " + filenameOrPortOption);
      System.exit(-1);
      return; // Seems to be needed to allow static analysis tools to work.
    }

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(relayer);
  }
}
