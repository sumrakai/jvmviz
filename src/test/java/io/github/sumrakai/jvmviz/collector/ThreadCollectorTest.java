package io.github.sumrakai.jvmviz.collector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadCollectorTest {

    private ThreadCollector collector;

    @BeforeEach
    void setUp() {
        collector = new ThreadCollector();
    }

    @Test
    void testCollect() {
        collector.collect();

        ThreadInfo info = collector.getThreadInfo();
        assertNotNull(info);
        assertTrue(info.threadCount() > 0);
        assertTrue(info.daemonThreadCount() >= 0);
        assertTrue(info.peakThreadCount() >= info.threadCount());
        assertTrue(info.totalStartedThreadCount() >= info.threadCount());
    }

    @Test
    void testGetName() {
        String name = collector.getName();

        assertEquals("ThreadCollector", name);
    }

    @Test
    void testMultipleCollections() {
        collector.collect();
        ThreadInfo first = collector.getThreadInfo();

        collector.collect();
        ThreadInfo second = collector.getThreadInfo();

        assertNotNull(first);
        assertNotNull(second);
        assertTrue(second.totalStartedThreadCount() >= first.totalStartedThreadCount());
    }
}