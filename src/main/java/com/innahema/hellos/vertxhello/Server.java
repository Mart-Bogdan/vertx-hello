package com.innahema.hellos.vertxhello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

/**
 * Created by Bogdan Mart on 07.03.2016.
 */
public class Server extends AbstractVerticle
{
    public void start() {

        HttpServer server = getVertx().createHttpServer();



        TemplateEngine engine = JadeTemplateEngine.create();
        TemplateHandler handler = TemplateHandler.create(engine);


        Router router = Router.router(vertx);

        router.get("/").handler(routingContext -> {
            routingContext.reroute("/index.html");
        });
        router.route().handler(routingContext -> {

            String file = routingContext.request().path();
            routingContext.response().sendFile("src/main/webapp/" + file);

        });

        server.requestHandler(router::accept).listen(8080);
    }
}