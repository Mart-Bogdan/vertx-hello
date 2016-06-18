package com.innahema.hellos.vertxhello.benchmark.hellotest.impl;

import org.openjdk.jmh.annotations.Fork;

/**
 * @author Taras Zubrei
 */
@Fork(jvmArgsAppend = "-XX:-UseCompressedOops")
public class HandlebarsNoCompressedOops extends Handlebars {
}
