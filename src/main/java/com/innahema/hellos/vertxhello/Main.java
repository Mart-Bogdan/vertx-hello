package com.innahema.hellos.vertxhello;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Created by Bogdan Mart on 07.03.2016.
 */
public class Main
{
    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(4));
        //Vertx vertx = Vertx.vertx();

        Server server = new Server();
        server.init(vertx,vertx.getOrCreateContext());
        server.start();

    }
}
