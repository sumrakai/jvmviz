package io.github.sumrakai.jvmviz.service;

import io.github.sumrakai.jvmviz.collector.Collector;
import io.github.sumrakai.jvmviz.collector.GcCollector;
import io.github.sumrakai.jvmviz.collector.GcInfo;
import io.github.sumrakai.jvmviz.collector.MemoryCollector;
import io.github.sumrakai.jvmviz.collector.MemoryInfo;
import io.github.sumrakai.jvmviz.collector.ThreadCollector;
import io.github.sumrakai.jvmviz.collector.ThreadInfo;
import io.github.sumrakai.jvmviz.visualizer.Visualizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * JVM monitoring service.
 * Uses Java virtual threads for better performance.
 * Coordinates collectors and visualizers.
 */
public class MonitorService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorService.class);
    private static final long UPDATE_INTERVAL_MS = 2000;

    private final ThreadCollector threadCollector;
    private final MemoryCollector memoryCollector;
    private final GcCollector gcCollector;
    private final Visualizer visualizer;
    private final List<Collector> collectors;

    private volatile boolean running = false;
    private Thread monitorThread;

    public MonitorService(ThreadCollector threadCollector,
                          MemoryCollector memoryCollector,
                          GcCollector gcCollector,
                          Visualizer visualizer) {
        this.threadCollector = threadCollector;
        this.memoryCollector = memoryCollector;
        this.gcCollector = gcCollector;
        this.visualizer = visualizer;
        this.collectors = List.of(threadCollector, memoryCollector, gcCollector);
    }

    public void start() {
        if (running) {
            logger.warn("MonitorService is already running");
            return;
        }

        running = true;
        monitorThread = Thread.ofVirtual()
                .name("MonitorThread")
                .start(this::monitorLoop);

        logger.info("MonitorService started (virtual thread)");
    }

    public void stop() {
        if (!running) {
            return;
        }

        running = false;
        if (monitorThread != null) {
            try {
                monitorThread.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Interrupted while waiting for thread termination");
            }
        }

        logger.info("MonitorService stopped");
    }

    private void monitorLoop() {
        while (running) {
            try {
                collectors.forEach(Collector::collect);

                ThreadInfo threadInfo = threadCollector.getThreadInfo();
                MemoryInfo memoryInfo = memoryCollector.getMemoryInfo();
                GcInfo gcInfo = gcCollector.getGcInfo();

                if (threadInfo != null && memoryInfo != null) {
                    visualizer.clearScreen();
                    visualizer.visualize(threadInfo, memoryInfo, gcInfo);
                }

                Thread.sleep(UPDATE_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.info("Monitoring thread interrupted");
                break;
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}