package com.innahema.hellos.vertxhello.benchmark;


import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;

@Fork(value = 1,jvmArgs = "-server",warmups = 0)
@Warmup(iterations=50)
@Measurement(iterations = 25)
public class BaseConfig {
}
