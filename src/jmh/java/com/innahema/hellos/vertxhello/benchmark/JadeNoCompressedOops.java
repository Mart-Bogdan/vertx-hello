package com.innahema.hellos.vertxhello.benchmark;

import org.openjdk.jmh.annotations.Fork;

@Fork(jvmArgsAppend = "-XX:-UseCompressedOops")
public class JadeNoCompressedOops extends Jade
{

}

