package io.github.sumrakai.jvmviz.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * Collector for JVM thread statistics.
 * Uses ThreadMXBean from JMX.
 */
public class ThreadCollector implements Collector {
    private static final Logger logger = LoggerFactory.getLogger(ThreadCollector.class);

    private final ThreadMXBean threadMXBean;
    private ThreadInfo currentInfo;

    public ThreadCollector() {
        this.threadMXBean = ManagementFactory.getThreadMXBean();
    }

    @Override
    public void collect() {
        try {
            int threadCount = threadMXBean.getThreadCount();
            int daemonThreadCount = threadMXBean.getDaemonThreadCount();
            long totalStartedThreadCount = threadMXBean.getTotalStartedThreadCount();
            int peakThreadCount = threadMXBean.getPeakThreadCount();

            currentInfo = new ThreadInfo(
                    threadCount,
                    daemonThreadCount,
                    totalStartedThreadCount,
                    peakThreadCount
            );

            logger.debug("Thread data collected: {} threads ({} daemon)",
                    threadCount, daemonThreadCount);
        } catch (Exception e) {
            logger.error("Error collecting thread data", e);
        }
    }

    @Override
    public String getName() {
        return "ThreadCollector";
    }

    public ThreadInfo getThreadInfo() {
        return currentInfo;
    }
}