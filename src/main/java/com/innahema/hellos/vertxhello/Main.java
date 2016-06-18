package com.innahema.hellos.vertxhello;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Created by Bogdan Mart on 07.03.2016.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting application");

        Vertx vertx = Vertx.vertx();
        String port = System.getenv("PORT");
        if (port == null)
            port = "8080";

        DeploymentOptions options = new DeploymentOptions()
                .setConfig(
                        new JsonObject().put("http.port", Integer.valueOf(port))
                );

        // We pass the options as the second parameter of the deployVerticle method.
        vertx.deployVerticle(Server.class.getName(), options);
    }
}
