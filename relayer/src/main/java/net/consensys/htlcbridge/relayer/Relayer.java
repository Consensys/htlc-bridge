package net.consensys.htlcbridge.relayer;

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

public class Relayer extends AbstractVerticle {
  private static final Logger LOG = LogManager.getLogger(Relayer.class);

  public static final String SOURCE_BLOCKCHAIN_URI = "http://127.0.0.1:8400/";
  public static final int SOURCE_BLOCK_PERIOD = 4000;
  public static final int SOURCE_CONFIRMATIONS = 3;
  public static final String SOURCE_TRANSFER_CONTRACT = "0x4c869fd21ab76638b881fb42a3964d0c239c3044";

  public static final String DESTINATION_BLOCKCHAIN_URI = "http://127.0.0.1:8400/";
  public static final int DESTINATION_BLOCK_PERIOD = 2000;
  public static final int DESTINATION_CONFIRMATIONS = 1;
  public static final String DESTINATION_RECEIVER_CONTRACT = "";

  public static final int API_PORT = 8080;




  RestAPI api;
  SourceBlockchainObserver sourceBlockchainObserver;

  int sourceBlockPeriod;

  public Relayer() {
    this(SOURCE_BLOCKCHAIN_URI, SOURCE_TRANSFER_CONTRACT, SOURCE_BLOCK_PERIOD, SOURCE_CONFIRMATIONS,
      DESTINATION_BLOCKCHAIN_URI, DESTINATION_RECEIVER_CONTRACT, DESTINATION_BLOCK_PERIOD,
        DESTINATION_CONFIRMATIONS,
        5, 40, null, null);

  }

  // TODO work out where relayer is up to.
  // TODO auto-share around the relaying of blocks in a redundant way.

  public Relayer(String sourceBcUri, String sourceTransferContract, int sourceBlockPeriod, int sourceConfirmations,
                 String destBcUri, String destTransferContract, int destBlockPeriod, int destConfirmations,
                 int destRetries, long destBcId, String destRelayerPKey, ContractGasProvider gasProvider) {
    this.api = new RestAPI(this.vertx, API_PORT);
    this.sourceBlockchainObserver = new SourceBlockchainObserver(
        sourceBcUri, sourceTransferContract, sourceBlockPeriod, sourceConfirmations,
        destBcUri, destTransferContract, destBlockPeriod, destConfirmations,
        destRelayerPKey, destRetries, destBcId, gasProvider);
    this.sourceBlockPeriod = sourceBlockPeriod;
  }


  @Override
  public void start() {
    LOG.info("Started");
    this.vertx.setPeriodic(this.sourceBlockPeriod, counter -> {
      this.sourceBlockchainObserver.checkNewBlock();
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

  public static void main (String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Relayer());
  }
}
