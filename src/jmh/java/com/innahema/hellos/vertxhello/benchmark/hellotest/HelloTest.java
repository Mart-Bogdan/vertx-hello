package com.innahema.hellos.vertxhello.benchmark.hellotest;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Taras Zubrei
 */
public interface HelloTest {
    @Setup
    void setUp() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException;

    @Benchmark
    void simple_template(Blackhole hole) throws InterruptedException;

    @Benchmark
    void with_layout(Blackhole hole) throws InterruptedException;
}
