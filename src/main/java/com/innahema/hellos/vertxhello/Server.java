package com.innahema.hellos.vertxhello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

/**
 * Created by Bogdan Mart on 07.03.2016.
 */
public class Server extends AbstractVerticle
{
    TemplateEngine jadeEngine = JadeTemplateEngine.create();
    TemplateEngine thymeleafEngine = ThymeleafTemplateEngine.create();
    public void start() {
        HttpServer server = getVertx().createHttpServer();



        Router router = Router.router(vertx);

        router.get("/hello/:name").handler(ctx->{

            ctx.put("name", ctx.request().getParam("name"));
            renderTemplate(ctx, jadeEngine, "templates/jade/hello.jade");
        });
        router.get("/hello").handler(ctx->{

            ctx.put("name", "Default name");
            renderTemplate(ctx, jadeEngine, "templates/jade/hello.jade");
        });
        router.get("/helloL").handler(ctx->{

            ctx.put("name", "Default name");
            renderTemplate(ctx, jadeEngine, "templates/jade/helloL.jade");
        });


        router.get("/t/hello").handler(ctx->{

            ctx.put("name", "Default name");
            renderTemplate(ctx, thymeleafEngine, "templates/thymeleaf/hello.html");
        });

        router.get("/").handler(routingContext -> {
            routingContext.reroute("/index.html");
        });
        router.get().handler(routingContext -> {

                String file = routingContext.request().path();
                String filename = "src/main/webapp/" + file;

                getVertx().fileSystem().exists(
                        filename,
                        exists -> {
                            if(exists.succeeded()&&exists.result())
                                routingContext.response().sendFile(filename);
                            else
                                routingContext.next();
                        }
                );
            });

        router.get().handler(routingContext->{
                HttpServerResponse response = routingContext.response();

                response.setStatusCode(404);
                response.end("404. File not found!");
            });

        server.requestHandler(router::accept).listen(8080);
    }

    private void renderTemplate(RoutingContext ctx, TemplateEngine engine, String templateFileName)
    {
        engine.render(ctx, templateFileName, res->{
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