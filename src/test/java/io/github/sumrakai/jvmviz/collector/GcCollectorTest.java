package io.github.sumrakai.jvmviz.collector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GcCollectorTest {

    private GcCollector collector;

    @BeforeEach
    void setUp() {
        collector = new GcCollector();
    }

    @Test
    void testCollect() {
        collector.collect();

        GcInfo info = collector.getGcInfo();
        assertNotNull(info);
        assertNotNull(info.collectors());
        assertFalse(info.collectors().isEmpty());
    }

    @Test
    void testGetName() {
        String name = collector.getName();

        assertEquals("GcCollector", name);
    }

    @Test
    void testGcStats() {
        collector.collect();
        GcInfo info = collector.getGcInfo();

        assertNotNull(info);
        for (GcInfo.GcStats stats : info.collectors()) {
            assertNotNull(stats.name());
            assertFalse(stats.name().isEmpty());
            assertTrue(stats.collectionCount() >= 0);
            assertTrue(stats.collectionTime() >= 0);
        }
    }

    @Test
    void testTotalCollectionCount() {
        collector.collect();
        GcInfo info = collector.getGcInfo();

        assertNotNull(info);
        long total = info.getTotalCollectionCount();
        assertTrue(total >= 0);

        long sum = info.collectors().stream()
                .mapToLong(GcInfo.GcStats::collectionCount)
                .sum();
        assertEquals(total, sum);
    }

    @Test
    void testFormatTime() {
        String ms = GcInfo.formatTime(500);
        String seconds = GcInfo.formatTime(1500);
        String minutes = GcInfo.formatTime(65000);

        assertTrue(ms.contains("ms"));
        assertTrue(seconds.contains("s"));
        assertTrue(minutes.contains("min"));
    }
}