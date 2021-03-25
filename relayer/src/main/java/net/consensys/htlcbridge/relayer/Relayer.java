package net.consensys.htlcbridge.relayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import net.consensys.htlcbridge.relayer.api.RestAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.File;
import java.io.IOException;

public class Relayer extends AbstractVerticle {
  private static final Logger LOG = LogManager.getLogger(Relayer.class);

  public static final int API_PORT = 8080;

  RestAPI api;

  SourceBlockchainObserver sourceBlockchainObserver;
  int sourceBlockPeriod;
  DestinationBlockchainObserver destBlockchainObserver;
  int destBlockPeriod;

  public Relayer(File configFile) throws Exception {
    this((RelayerConfig) (new ObjectMapper().readerFor(RelayerConfig.class).readValue(configFile)));
  }

  public Relayer(RelayerConfig config) throws Exception {
    this.api = new RestAPI(this.vertx, config.apiPort);
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
  }

  // TODO work out where relayer is up to.
  // TODO auto-share around the relaying of blocks in a redundant way.

  public Relayer(String sourceBcUri, String sourceTransferContract, int sourceBlockPeriod, int sourceConfirmations,
                 int sourceRetries, long sourceBcId, String sourceRelayerPKey, ContractGasProvider sourceGasProvider,
                 String destBcUri, String destTransferContract, int destBlockPeriod, int destConfirmations,
                 int destRetries, long destBcId, String destRelayerPKey, ContractGasProvider destGasProvider) {
    this.api = new RestAPI(this.vertx, API_PORT);
    this.sourceBlockchainObserver = new SourceBlockchainObserver(
        sourceBcUri, sourceTransferContract, sourceBlockPeriod, sourceConfirmations,
        sourceRelayerPKey, sourceRetries, sourceBcId, sourceGasProvider,
        destBcUri, destTransferContract, destBlockPeriod, destConfirmations,
        destRelayerPKey, destRetries, destBcId, destGasProvider);
    this.sourceBlockPeriod = sourceBlockPeriod;

    this.destBlockchainObserver = new DestinationBlockchainObserver(
        sourceBcUri, sourceTransferContract, sourceBlockPeriod, sourceConfirmations,
        sourceRelayerPKey, sourceRetries, sourceBcId, sourceGasProvider,
        destBcUri, destTransferContract, destBlockPeriod, destConfirmations,
        destRelayerPKey, destRetries, destBcId, destGasProvider);
    this.destBlockPeriod = destBlockPeriod;
  }

  public void setRelayers(int numRelayers, int relayerOffset) {
    this.sourceBlockchainObserver.setRelayers(numRelayers, relayerOffset);
    this.destBlockchainObserver.setRelayers(numRelayers, relayerOffset);
  }


  @Override
  public void start() {
    LOG.info("Started");
    this.vertx.setPeriodic(this.sourceBlockPeriod, counter -> {
      this.sourceBlockchainObserver.checkNewBlock();
    });

    this.vertx.setPeriodic(this.destBlockPeriod, counter -> {
      this.destBlockchainObserver.checkNewBlock();
    });


//    vertx.createHttpServer()
//        .requestHandler(req -> {
//          System.out.println("Request #" + counter++ +
//              " from " + req.remoteAddress().host());
//          req.response().end("Hello from Coderland!");
//        })
//        .listen(8080);

    HttpServer server = this.vertx.createHttpServer();

        Router router = Router.router(this.vertx);

//    router.route().handler(ctx -> {
//
//      // This handler will be called for every request
//      HttpServerResponse response = ctx.response();
//      response.putHeader("content-type", "text/plain");
//
//      // Write to the response and end it
//      response.end("Hello World from Vert.x-Web!");
//    });
//

//    router.get("/").handler(ctx -> {
//      LOG.info("Request received");
//    });

    this.vertx.setPeriodic(5000, counter -> {
      gen();
    });


    router
        .get("/some/path")
        // this handler will ensure that the response is serialized to json
        // the content type is set to "application/json"
        .respond(
            ctx -> {
              LOG.info("Some path");
              return Future.succeededFuture(new JsonObject().put("hello", "world"));
            });

    router
        .get("/ver")
        // this handler will ensure that the response is serialized to json
        // the content type is set to "application/json"
        .respond(
            ctx -> Future.succeededFuture(new JsonObject().put("version", "1")));

//    router
//        .get("/some/path")
//        // this handler will ensure that the Pojo is serialized to json
//        // the content type is set to "application/json"
//        .respond(
//            ctx -> Future.succeededFuture(new Pojo()));

    server.requestHandler(router).listen(API_PORT);


//    this.api.createAPI();
  }

  @Override
  public void stop() {
    LOG.info("Shutting down");
  }


  public void gen() {

  }

  public static void main (String[] args) throws Exception {
    String filename = args[0];
    System.out.println("Filename: " + filename);
    File configFile = new File(filename);
    Relayer relayer = new Relayer(configFile);

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(relayer);
  }
}
