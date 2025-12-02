package io.github.sumrakai.jvmviz.collector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для ThreadCollector.
 */
class ThreadCollectorTest {

    private ThreadCollector collector;

    @BeforeEach
    void setUp() {
        collector = new ThreadCollector();
    }

    @Test
    void testCollect() {
        // Когда
        collector.collect();

        // Тогда
        ThreadInfo info = collector.getThreadInfo();
        assertNotNull(info, "ThreadInfo должен быть собран");
        assertTrue(info.threadCount() > 0, "Должен быть хотя бы один поток");
        assertTrue(info.daemonThreadCount() >= 0, "Количество daemon потоков не может быть отрицательным");
        assertTrue(info.peakThreadCount() >= info.threadCount(),
                "Peak threads должен быть >= текущего количества");
        assertTrue(info.totalStartedThreadCount() >= info.threadCount(),
                "Всего запущенных потоков должно быть >= текущего количества");
    }

    @Test
    void testGetName() {
        // Когда
        String name = collector.getName();

        // Тогда
        assertEquals("ThreadCollector", name);
    }

    @Test
    void testMultipleCollections() {
        // Когда
        collector.collect();
        ThreadInfo first = collector.getThreadInfo();

        collector.collect();
        ThreadInfo second = collector.getThreadInfo();

        // Тогда
        assertNotNull(first);
        assertNotNull(second);
        // Данные могут изменяться, но структура должна быть валидной
        assertTrue(second.totalStartedThreadCount() >= first.totalStartedThreadCount(),
                "Количество запущенных потоков не должно уменьшаться");
    }
}