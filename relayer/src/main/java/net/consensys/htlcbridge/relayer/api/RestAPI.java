package net.consensys.htlcbridge.relayer.api;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import net.consensys.htlcbridge.relayer.Relayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestAPI {
  private static final Logger LOG = LogManager.getLogger(RestAPI.class);

  Vertx vertx;
  int port;

  int counter = 0;

  public RestAPI(Vertx vertx, int port) {
    this.vertx = vertx;
    this.port = port;
  }

  public void createAPI() {

    vertx.createHttpServer()
        .requestHandler(req -> {
          System.out.println("Request #" + counter++ +
              " from " + req.remoteAddress().host());
          req.response().end("Hello from Coderland!");
        })
        .listen(8080);

//    HttpServer server = this.vertx.createHttpServer();
//
//    Router router = Router.router(this.vertx);
//
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
//    server.requestHandler(router).listen(this.port);

//
//
//    HttpServer server = this.vertx.createHttpServer();
//
//    Router router = Router.router(this.vertx);
//
////    router.route().handler(ctx -> {
////
////      // This handler will be called for every request
////      HttpServerResponse response = ctx.response();
////      response.putHeader("content-type", "text/plain");
////
////      // Write to the response and end it
////      response.end("Hello World from Vert.x-Web!");
////    });
////
//
////    router.get("/").handler(ctx -> {
////      LOG.info("Request received");
////    });
//
//    router
//        .get("/some/path")
//        // this handler will ensure that the response is serialized to json
//        // the content type is set to "application/json"
//        .respond(
//            ctx -> Future.succeededFuture(new JsonObject().put("hello", "world")));
//
//    router
//        .get("/ver")
//        // this handler will ensure that the response is serialized to json
//        // the content type is set to "application/json"
//        .respond(
//            ctx -> Future.succeededFuture(new JsonObject().put("version", "1")));
//
////    router
////        .get("/some/path")
////        // this handler will ensure that the Pojo is serialized to json
////        // the content type is set to "application/json"
////        .respond(
////            ctx -> Future.succeededFuture(new Pojo()));
//
//    server.requestHandler(router).listen(this.port);

    LOG.info("Listening on port {}", this.port);
  }



}
