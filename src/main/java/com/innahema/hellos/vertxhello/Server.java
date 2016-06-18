package com.innahema.hellos.vertxhello;

import com.innahema.hellos.vertxhello.tempengine.ThymeleafTemplateEngineWithLayout;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import io.vertx.ext.web.templ.MVELTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

/**
 * Created by Bogdan Mart on 07.03.2016.
 */
public class Server extends AbstractVerticle {
    private TemplateEngine jadeEngine = JadeTemplateEngine.create();
    private TemplateEngine mvelEngine = MVELTemplateEngine.create();
    private TemplateEngine handlebarsEngine = HandlebarsTemplateEngine.create();
    private TemplateEngine thymeleafEngine = new ThymeleafTemplateEngineWithLayout("templates/thymeleaf/layout.html");
    private HttpServer httpServer;


    public void start(Future<Void> fut) {
        System.out.println("Starting " + getClass().getName());

        httpServer = getVertx().createHttpServer();
        Router router = Router.router(vertx);

        router.mountSubRouter("/jade", buildTemplateRouter(jadeEngine, "jade", "jade"));
        router.mountSubRouter("/t", buildTemplateRouter(thymeleafEngine, "html", "thymeleaf"));
        router.mountSubRouter("/mvel", buildTemplateRouter(mvelEngine, "templ", "mvel"));
        router.mountSubRouter("/handlebars", buildTemplateRouter(handlebarsEngine, "hbs", "handlebars"));

        router.get("/").handler(routingContext -> {
            routingContext.reroute("/index.html");
        });
        router.get().handler(routingContext -> {

            String file = routingContext.request().path();
            String filename = "src/main/webapp/" + file;
            Context context = Vertx.currentContext();
            getVertx().fileSystem().exists(
                    filename,
                    exists -> {
                        if (exists.succeeded() && exists.result())
                            routingContext.response().sendFile(filename);
                        else
                            routingContext.next();
                    }
            );
        });

        router.get().handler(routingContext -> {
            HttpServerResponse response = routingContext.response();

            response.setStatusCode(404);
            response.end("404. File not found!");
        });

        httpServer
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port"),
                        res -> {
                            if (res.succeeded()) {
                                System.out.println("Listening on port " + config().getInteger("http.port"));
                                fut.complete();
                            } else {
                                System.out.println("Failed to start");
                                Throwable cause = res.cause();

                                cause.printStackTrace();
                                fut.fail(cause);
                            }
                        }
                );
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        System.out.println("Stopping " + getClass().getName());
        httpServer.close(stopFuture.completer());
    }

    private Router buildTemplateRouter(TemplateEngine engine, String ext, String templFolder) {
        Router router = Router.router(getVertx());

        router.get("/hello/:name").handler(ctx -> {

            ctx.put("name", ctx.request().getParam("name"));
            renderTemplate(ctx, engine, "templates/" + templFolder + "/hello." + ext);
        });
        router.get("/hello").handler(ctx -> {

            ctx.put("name", "Default name");
            renderTemplate(ctx, engine, "templates/" + templFolder + "/hello." + ext);
        });
        router.get("/mem").handler(ctx -> {

            final double mb = 1024 * 1024;
            Runtime runtime = Runtime.getRuntime();
            long usedRam = runtime.totalMemory() - runtime.freeMemory();

            ctx.put("usedMemory", usedRam / mb);
            ctx.put("totalMemory", runtime.totalMemory() / mb);
            ctx.put("freeMemory", runtime.freeMemory() / mb);
            renderTemplate(ctx, engine, "templates/" + templFolder + "/mem." + ext);
        });
        router.get("/helloL").handler(ctx -> {

            ctx.put("name", "Default name");
            renderTemplate(ctx, engine, "templates/" + templFolder + "/helloL." + ext);
        });

        return router;
    }

    private void renderTemplate(RoutingContext ctx, TemplateEngine engine, String templateFileName) {
        engine.render(ctx, templateFileName, res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, TemplateHandler.DEFAULT_CONTENT_TYPE)
                        .end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }
}