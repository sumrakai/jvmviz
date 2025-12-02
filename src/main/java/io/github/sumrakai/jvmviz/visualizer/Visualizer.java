package io.github.sumrakai.jvmviz.visualizer;

import io.github.sumrakai.jvmviz.collector.MemoryInfo;
import io.github.sumrakai.jvmviz.collector.ThreadInfo;
import io.github.sumrakai.jvmviz.collector.GcInfo;



/**
 * Interface for visualizing JVM data in console.
 */
public interface Visualizer {

    /**
     * Visualizes collected JVM data.
     *
     * @param threadInfo thread information
     * @param memoryInfo memory information
     * @param gcInfo garbage collector information
     */
    void visualize(ThreadInfo threadInfo, MemoryInfo memoryInfo, GcInfo gcInfo);

    /**
     * Clears screen before displaying new information.
     */
    void clearScreen();
}