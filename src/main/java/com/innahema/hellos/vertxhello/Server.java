package com.innahema.hellos.vertxhello;

import io.vertx.core.AbstractVerticle;

/**
 * Created by Bogdan Mart on 07.03.2016.
 */
public class Server extends AbstractVerticle
{
    public void start() {
        getVertx().createHttpServer().requestHandler(req -> {
            String file = req.path().equals("/") ? "index.html" : req.path();
//                req.response().setStatusCode(200);
//                req.response().end();
            req.response().sendFile("src/main/webapp/" + file);
        }).listen(8080);
    }
}