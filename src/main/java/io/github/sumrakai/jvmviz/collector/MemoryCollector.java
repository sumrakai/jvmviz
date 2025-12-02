package io.github.sumrakai.jvmviz.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * Collector for JVM memory statistics.
 * Uses MemoryMXBean from JMX.
 */
public class MemoryCollector implements Collector {
    private static final Logger logger = LoggerFactory.getLogger(MemoryCollector.class);

    private final MemoryMXBean memoryMXBean;
    private MemoryInfo currentInfo;

    public MemoryCollector() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
    }

    @Override
    public void collect() {
        try {
            MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
            MemoryUsage nonHeapUsage = memoryMXBean.getNonHeapMemoryUsage();

            currentInfo = new MemoryInfo(
                    heapUsage.getUsed(),
                    heapUsage.getMax(),
                    heapUsage.getCommitted(),
                    nonHeapUsage.getUsed(),
                    nonHeapUsage.getMax(),
                    nonHeapUsage.getCommitted()
            );

            logger.debug("Memory data collected: heap {}% used",
                    currentInfo.getHeapUsagePercent());
        } catch (Exception e) {
            logger.error("Error collecting memory data", e);
        }
    }

    @Override
    public String getName() {
        return "MemoryCollector";
    }

    public MemoryInfo getMemoryInfo() {
        return currentInfo;
    }
}