package io.github.sumrakai.jvmviz.visualizer;

import io.github.sumrakai.jvmviz.collector.GcInfo;
import io.github.sumrakai.jvmviz.collector.MemoryInfo;
import io.github.sumrakai.jvmviz.collector.ThreadInfo;

import java.util.Locale;

/**
 * Console visualizer with ASCII graphics.
 * Displays data as progress bars and tables.
 */
public class ConsoleVisualizer implements Visualizer {

    private static final int PROGRESS_BAR_WIDTH = 50;
    private static final String FILLED_CHAR = "█";
    private static final String EMPTY_CHAR = "░";
    private static final String BOTTOM_BORDER = "└───────────────────────────────────────────────────────────";
    private static final Locale LOCALE = Locale.US;

    @Override
    public void clearScreen() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    @Override
    public void visualize(ThreadInfo threadInfo, MemoryInfo memoryInfo, GcInfo gcInfo) {
        if (threadInfo == null || memoryInfo == null) {
            return;
        }

        printHeader();
        printThreadInfo(threadInfo);
        printMemoryInfo(memoryInfo);
        if (gcInfo != null) {
            printGcInfo(gcInfo);
        }
        printSeparator();
    }

    private void printHeader() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              JVM Visualization Tool v1.0.0                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void printThreadInfo(ThreadInfo info) {
        System.out.println("┌─ Threads ─────────────────────────────────────────────────");
        System.out.printf(LOCALE, "│ Current Threads:     %-35d%n", info.threadCount());
        System.out.printf(LOCALE, "│ Daemon Threads:      %-35d%n", info.daemonThreadCount());
        System.out.printf(LOCALE, "│ Peak Threads:        %-35d%n", info.peakThreadCount());
        System.out.printf(LOCALE, "│ Total Started:       %-35d%n", info.totalStartedThreadCount());
        System.out.println(BOTTOM_BORDER);
        System.out.println();
    }

    private void printMemoryInfo(MemoryInfo info) {
        double heapUsagePercent = info.getHeapUsagePercent();

        System.out.println("┌─ Heap Memory ────────────────────────────────────────────");
        System.out.printf(LOCALE, "│ Used:    %-15s │ Max:     %-15s%n",
                MemoryInfo.formatBytes(info.heapUsed()),
                MemoryInfo.formatBytes(info.heapMax()));
        System.out.println("│");

        String progressBar = createProgressBar(heapUsagePercent);
        String percentStr = String.format(LOCALE, "%.1f%%", heapUsagePercent);

        System.out.printf(LOCALE, "│ [%s] %s%n", progressBar, percentStr);
        System.out.println("│");

        System.out.printf(LOCALE, "│ Committed: %-47s%n",
                MemoryInfo.formatBytes(info.heapCommitted()));
        System.out.println(BOTTOM_BORDER);
        System.out.println();

        System.out.println("┌─ Non-Heap Memory ─────────────────────────────────────────");
        System.out.printf(LOCALE, "│ Used:      %-47s%n",
                MemoryInfo.formatBytes(info.nonHeapUsed()));
        System.out.printf(LOCALE, "│ Committed: %-47s%n",
                MemoryInfo.formatBytes(info.nonHeapCommitted()));
        System.out.println(BOTTOM_BORDER);
        System.out.println();
    }

    private void printGcInfo(GcInfo gcInfo) {
        System.out.println("┌─ Garbage Collectors ─────────────────────────────────────");

        System.out.printf(LOCALE, "│ Total Collections:   %-35d%n",
                gcInfo.getTotalCollectionCount());
        System.out.printf(LOCALE, "│ Total GC Time:       %-35s%n",
                GcInfo.formatTime(gcInfo.getTotalCollectionTime()));
        System.out.println("│");

        for (GcInfo.GcStats stats : gcInfo.collectors()) {
            System.out.printf(LOCALE, "│ %-20s%n", stats.name() + ":");
            System.out.printf(LOCALE, "│   Collections:      %-33d%n", stats.collectionCount());
            System.out.printf(LOCALE, "│   Time:              %-33s%n",
                    GcInfo.formatTime(stats.collectionTime()));

            if (stats.collectionCount() > 0) {
                long avgTime = stats.collectionTime() / stats.collectionCount();
                System.out.printf(LOCALE, "│   Avg Time/GC:       %-33s%n",
                        GcInfo.formatTime(avgTime));
            }

            System.out.println("│");
        }

        System.out.println(BOTTOM_BORDER);
        System.out.println();
    }

    private String createProgressBar(double percent) {
        int filled = (int) (PROGRESS_BAR_WIDTH * percent / 100.0);
        filled = Math.min(filled, PROGRESS_BAR_WIDTH);
        filled = Math.max(filled, 0);

        int empty = PROGRESS_BAR_WIDTH - filled;

        return FILLED_CHAR.repeat(filled) + EMPTY_CHAR.repeat(empty);
    }

    private void printSeparator() {
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println("Press Ctrl+C to stop");
    }
}