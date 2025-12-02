package io.github.sumrakai.jvmviz.collector;

/**
 * JVM thread information.
 */
public record ThreadInfo(
        int threadCount,
        int daemonThreadCount,
        long totalStartedThreadCount,
        int peakThreadCount
) {}