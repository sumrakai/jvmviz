package io.github.sumrakai.jvmviz.service;

import io.github.sumrakai.jvmviz.collector.GcCollector;
import io.github.sumrakai.jvmviz.collector.MemoryCollector;
import io.github.sumrakai.jvmviz.collector.ThreadCollector;
import io.github.sumrakai.jvmviz.visualizer.ConsoleVisualizer;
import io.github.sumrakai.jvmviz.visualizer.Visualizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MonitorService.
 * Uses real objects instead of mocks due to Mockito limitations with Java 25.
 */
class MonitorServiceTest {

    private MonitorService monitorService;

    @BeforeEach
    void setUp() {
        ThreadCollector threadCollector = new ThreadCollector();
        MemoryCollector memoryCollector = new MemoryCollector();
        GcCollector gcCollector = new GcCollector();
        Visualizer visualizer = new ConsoleVisualizer();
        monitorService = new MonitorService(
                threadCollector,
                memoryCollector,
                gcCollector,
                visualizer
        );
    }

    @Test
    void testStart() {
        monitorService.start();

        assertTrue(monitorService.isRunning());

        monitorService.stop();
    }

    @Test
    void testStop() throws InterruptedException {
        monitorService.start();
        assertTrue(monitorService.isRunning());

        monitorService.stop();
        Thread.sleep(100);

        assertFalse(monitorService.isRunning());
    }

    @Test
    void testDoubleStart() {
        monitorService.start();
        monitorService.start();

        assertTrue(monitorService.isRunning());

        monitorService.stop();
    }

    @Test
    void testStopWhenNotRunning() {
        monitorService.stop();

        assertFalse(monitorService.isRunning());
    }

    @Test
    void testIsRunning() {
        assertFalse(monitorService.isRunning());

        monitorService.start();

        assertTrue(monitorService.isRunning());

        monitorService.stop();
    }
}