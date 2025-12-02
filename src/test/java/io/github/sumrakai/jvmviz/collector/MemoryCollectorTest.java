package io.github.sumrakai.jvmviz.collector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryCollectorTest {

    private MemoryCollector collector;

    @BeforeEach
    void setUp() {
        collector = new MemoryCollector();
    }

    @Test
    void testCollect() {
        collector.collect();

        MemoryInfo info = collector.getMemoryInfo();
        assertNotNull(info);
        assertTrue(info.heapUsed() >= 0);
        assertTrue(info.heapMax() > 0);
        assertTrue(info.heapUsed() <= info.heapMax());
        assertTrue(info.heapCommitted() >= info.heapUsed());
    }

    @Test
    void testGetName() {
        String name = collector.getName();

        assertEquals("MemoryCollector", name);
    }

    @Test
    void testHeapUsagePercent() {
        collector.collect();
        MemoryInfo info = collector.getMemoryInfo();

        assertNotNull(info);
        double usagePercent = info.getHeapUsagePercent();
        assertTrue(usagePercent >= 0.0 && usagePercent <= 100.0);
    }

    @Test
    void testFormatBytes() {
        String bytes = MemoryInfo.formatBytes(1024);
        String kb = MemoryInfo.formatBytes(1024 * 1024);
        String mb = MemoryInfo.formatBytes(1024 * 1024 * 1024);

        assertTrue(bytes.contains("KB"));
        assertTrue(kb.contains("MB"));
        assertTrue(mb.contains("GB"));
    }
}