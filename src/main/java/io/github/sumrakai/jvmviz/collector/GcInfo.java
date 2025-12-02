package io.github.sumrakai.jvmviz.collector;

import java.util.List;
import java.util.Locale;

/**
 * JVM garbage collector information.
 * Contains statistics for all available GCs.
 */
public record GcInfo(
        List<GcStats> collectors
) {
    /**
     * Statistics for a single garbage collector.
     */
    public record GcStats(
            String name,
            long collectionCount,
            long collectionTime,
            String memoryPoolNames
    ) {}

    /**
     * Returns total collection count across all GCs.
     */
    public long getTotalCollectionCount() {
        return collectors.stream()
                .mapToLong(GcStats::collectionCount)
                .sum();
    }

    /**
     * Returns total time spent on GC (in milliseconds).
     */
    public long getTotalCollectionTime() {
        return collectors.stream()
                .mapToLong(GcStats::collectionTime)
                .sum();
    }

    /**
     * Formats time to human-readable format.
     */
    public static String formatTime(long milliseconds) {
        if (milliseconds < 1000) {
            return milliseconds + " ms";
        }
        double seconds = milliseconds / 1000.0;
        if (seconds < 60) {
            return String.format(Locale.US, "%.2f s", seconds);
        }
        double minutes = seconds / 60.0;
        return String.format(Locale.US, "%.2f min", minutes);
    }
}