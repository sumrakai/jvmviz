package io.github.sumrakai.jvmviz.collector;

/**
 * Base interface for all JVM statistics collectors.
 * Defines contract for data collection.
 */
public interface Collector {

    /**
     * Collects current data.
     * Called periodically from MonitorService.
     */
    void collect();

    /**
     * Returns collector name for logging.
     */
    String getName();
}