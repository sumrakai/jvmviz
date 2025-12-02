package io.github.sumrakai.jvmviz.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Collector for JVM garbage collector statistics.
 * Uses GarbageCollectorMXBean from JMX.
 */
public class GcCollector implements Collector {
    private static final Logger logger = LoggerFactory.getLogger(GcCollector.class);

    private final List<GarbageCollectorMXBean> gcBeans;
    private GcInfo currentInfo;

    public GcCollector() {
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
    }

    @Override
    public void collect() {
        try {
            List<GcInfo.GcStats> stats = new ArrayList<>();

            for (GarbageCollectorMXBean gcBean : gcBeans) {
                String name = gcBean.getName();
                long collectionCount = gcBean.getCollectionCount();
                long collectionTime = gcBean.getCollectionTime();
                String[] memoryPoolNames = gcBean.getMemoryPoolNames();
                String pools = String.join(", ", memoryPoolNames);

                stats.add(new GcInfo.GcStats(
                        name,
                        collectionCount,
                        collectionTime,
                        pools
                ));
            }

            currentInfo = new GcInfo(stats);

            logger.debug("GC data collected: {} collectors, {} total collections",
                    stats.size(), currentInfo.getTotalCollectionCount());
        } catch (Exception e) {
            logger.error("Error collecting GC data", e);
        }
    }

    @Override
    public String getName() {
        return "GcCollector";
    }

    public GcInfo getGcInfo() {
        return currentInfo;
    }
}