package io.github.sumrakai.jvmviz.collector;

/**
 * JVM memory information.
 */
public record MemoryInfo(
        long heapUsed,
        long heapMax,
        long heapCommitted,
        long nonHeapUsed,
        long nonHeapMax,
        long nonHeapCommitted
) {
    /**
     * Calculates heap memory usage percentage.
     */
    public double getHeapUsagePercent() {
        if (heapMax == 0) {
            return 0.0;
        }
        return (double) heapUsed / heapMax * 100.0;
    }

    /**
     * Formats bytes to human-readable format (MB, GB).
     */
    public static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        long kb = bytes / 1024;
        if (kb < 1024) {
            return kb + " KB";
        }
        long mb = kb / 1024;
        if (mb < 1024) {
            return mb + " MB";
        }
        long gb = mb / 1024;
        return gb + " GB";
    }
}