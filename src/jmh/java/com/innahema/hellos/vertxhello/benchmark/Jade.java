package com.innahema.hellos.vertxhello.benchmark;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RouteImpl;
import io.vertx.ext.web.impl.RouterImpl;
import io.vertx.ext.web.impl.RoutingContextImpl;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

//@Fork(1)
//@Fork(jvmArgsPrepend = "-agentlib:jdwp=transport=dt_socket,server=n,address=winnie-pc:5005,suspend=n")
//@BenchmarkMode(Mode.All)
@State(Scope.Thread)
public class Jade extends BaseConfig
{

    TemplateEngine engine;
    private Vertx vertx;
    private RouterImpl router;
    private RouteImpl route;
    private RoutingContext routingContext;

    @Setup
    public void setUp() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        engine = JadeTemplateEngine.create();

        vertx = Vertx.vertx();

        router = new RouterImpl(vertx);
        route = (RouteImpl) router.get("/");
        Set<RouteImpl> routes = new HashSet<>();
        routes.add( route);

        Constructor<?> constructor = Class
                .forName("io.vertx.core.http.impl.HttpServerRequestImpl")
                .getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        HttpServerRequest request = (HttpServerRequest) constructor
                .newInstance(
                        null,
                        new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET,"/"),
                        null
                );

        routingContext = new RoutingContextImpl("/", router, request, routes);
        routingContext.put("name","Hello");
    }

    @Benchmark
    public void simple_template(Blackhole hole) throws InterruptedException {

        //CountDownLatch latch = new CountDownLatch(1);
        engine.render(routingContext, "templates/jade/hello.jade", res->{
            if(res.failed())
                throw new RuntimeException(res.cause());
            hole.consume(res.result());
            //    latch.countDown();
        });
        //latch.await();
    }
    @Benchmark
    public void with_layout(Blackhole hole) throws InterruptedException {

        engine.render(routingContext, "templates/jade/helloL.jade", res->{
            if(res.failed())
                throw new RuntimeException(res.cause());
            hole.consume(res.result());
        });
    }
}
