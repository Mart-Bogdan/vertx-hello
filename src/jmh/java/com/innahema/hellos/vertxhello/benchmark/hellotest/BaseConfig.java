package com.innahema.hellos.vertxhello.benchmark.hellotest;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RouteImpl;
import io.vertx.ext.web.impl.RouterImpl;
import io.vertx.ext.web.impl.RoutingContextImpl;
import io.vertx.ext.web.templ.TemplateEngine;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

@Fork(value = 1, jvmArgs = "-server", warmups = 0)
@Warmup(iterations = 1)
@Measurement(iterations = 5)
public class BaseConfig {
    private TemplateEngine engine;
    private RoutingContext routingContext;

    protected void setUp(TemplateEngine templateEngine) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        engine = templateEngine;

        Vertx vertx = Vertx.vertx();
        RouterImpl router = new RouterImpl(vertx);
        RouteImpl route = (RouteImpl) router.get("/");
        Set<RouteImpl> routes = new HashSet<>();
        routes.add(route);

        Constructor<?> constructor = Class
                .forName("io.vertx.core.http.impl.HttpServerRequestImpl")
                .getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        HttpServerRequest request = (HttpServerRequest) constructor
                .newInstance(
                        null,
                        new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"),
                        null
                );

        routingContext = new RoutingContextImpl("/", router, request, routes);
        routingContext.put("name", "Hello");
    }

    protected void test(Blackhole hole, String path) throws InterruptedException {
        engine.render(routingContext, path, res -> {
            if (res.failed())
                throw new RuntimeException(res.cause());
            hole.consume(res.result());
        });
    }
}
