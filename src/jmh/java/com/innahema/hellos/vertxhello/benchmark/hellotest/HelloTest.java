package com.innahema.hellos.vertxhello.benchmark.hellotest;

import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Taras Zubrei
 */
public interface HelloTest {
    void setUp() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException;

    void simple_template(Blackhole hole) throws InterruptedException;

    void with_layout(Blackhole hole) throws InterruptedException;
}
