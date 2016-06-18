package com.innahema.hellos.vertxhello.benchmark.hellotest.impl;

import com.innahema.hellos.vertxhello.benchmark.hellotest.BaseConfig;
import com.innahema.hellos.vertxhello.benchmark.hellotest.HelloTest;
import io.vertx.ext.web.templ.MVELTemplateEngine;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Taras Zubrei
 */
@State(Scope.Thread)
@Fork(jvmArgsAppend = "-XX:+UseCompressedOops")
public class Mvel extends BaseConfig implements HelloTest {
    @Override
    public void setUp() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        setUp(MVELTemplateEngine.create());
    }

    @Override
    public void simple_template(Blackhole hole) throws InterruptedException {
        test(hole, "templates/mvel/hello.templ");
    }

    @Override
    public void with_layout(Blackhole hole) throws InterruptedException {
        test(hole, "templates/mvel/helloL.templ");
    }
}
