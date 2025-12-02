package io.github.sumrakai.jvmviz.collector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для MemoryCollector.
 */
class MemoryCollectorTest {

    private MemoryCollector collector;

    @BeforeEach
    void setUp() {
        collector = new MemoryCollector();
    }

    @Test
    void testCollect() {
        // Когда
        collector.collect();

        // Тогда
        MemoryInfo info = collector.getMemoryInfo();
        assertNotNull(info, "MemoryInfo должен быть собран");
        assertTrue(info.heapUsed() >= 0, "Использованная heap память не может быть отрицательной");
        assertTrue(info.heapMax() > 0, "Максимальная heap память должна быть больше 0");
        assertTrue(info.heapUsed() <= info.heapMax(),
                "Использованная память не может превышать максимальную");
        assertTrue(info.heapCommitted() >= info.heapUsed(),
                "Committed память должна быть >= использованной");
    }

    @Test
    void testGetName() {
        // Когда
        String name = collector.getName();

        // Тогда
        assertEquals("MemoryCollector", name);
    }

    @Test
    void testHeapUsagePercent() {
        // Когда
        collector.collect();
        MemoryInfo info = collector.getMemoryInfo();

        // Тогда
        assertNotNull(info);
        double usagePercent = info.getHeapUsagePercent();
        assertTrue(usagePercent >= 0.0 && usagePercent <= 100.0,
                "Процент использования должен быть от 0 до 100");
    }

    @Test
    void testFormatBytes() {
        // Когда
        String bytes = MemoryInfo.formatBytes(1024);
        String kb = MemoryInfo.formatBytes(1024 * 1024);
        String mb = MemoryInfo.formatBytes(1024 * 1024 * 1024);

        // Тогда
        assertTrue(bytes.contains("KB"), "1024 байт должно форматироваться как KB");
        assertTrue(kb.contains("MB"), "1 MB должно форматироваться как MB");
        assertTrue(mb.contains("GB"), "1 GB должно форматироваться как GB");
    }
}