package com.innahema.hellos.vertxhello.benchmark.hellotest.impl;

import com.innahema.hellos.vertxhello.benchmark.hellotest.BaseConfig;
import com.innahema.hellos.vertxhello.benchmark.hellotest.HelloTest;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;

@State(Scope.Thread)
@Fork(jvmArgsAppend = "-XX:+UseCompressedOops")
public class Jade extends BaseConfig implements HelloTest {
    @Setup
    @Override
    public void setUp() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        setUp(JadeTemplateEngine.create());
    }

    @Benchmark
    @Override
    public void simple_template(Blackhole hole) throws InterruptedException {
        test(hole, "templates/jade/hello.jade");
    }

    @Benchmark
    @Override
    public void with_layout(Blackhole hole) throws InterruptedException {
        test(hole, "templates/jade/helloL.jade");
    }
}


