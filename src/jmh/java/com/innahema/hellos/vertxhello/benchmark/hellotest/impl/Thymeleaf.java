package com.innahema.hellos.vertxhello.benchmark.hellotest.impl;

import com.innahema.hellos.vertxhello.benchmark.hellotest.BaseConfig;
import com.innahema.hellos.vertxhello.benchmark.hellotest.HelloTest;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;

@Fork(jvmArgsAppend = "-XX:+UseCompressedOops")
@State(Scope.Thread)
public class Thymeleaf extends BaseConfig implements HelloTest {
    @Override
    public void setUp() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        setUp(ThymeleafTemplateEngine.create());
    }

    @Override
    public void simple_template(Blackhole hole) throws InterruptedException {
        test(hole, "templates/thymeleaf/hello.html");
    }

    @Override
    public void with_layout(Blackhole hole) throws InterruptedException {
        test(hole, "templates/thymeleaf/helloL.html");
    }
}
