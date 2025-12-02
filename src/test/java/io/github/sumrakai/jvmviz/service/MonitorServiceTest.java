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
 * Тесты для MonitorService.
 * Используем реальные объекты вместо моков из-за ограничений Mockito с Java 25.
 */
class MonitorServiceTest {

    private ThreadCollector threadCollector;
    private MemoryCollector memoryCollector;
    private GcCollector gcCollector;
    private Visualizer visualizer;
    private MonitorService monitorService;

    @BeforeEach
    void setUp() {
        threadCollector = new ThreadCollector();
        memoryCollector = new MemoryCollector();
        gcCollector = new GcCollector();
        visualizer = new ConsoleVisualizer();
        monitorService = new MonitorService(
                threadCollector,
                memoryCollector,
                gcCollector,
                visualizer
        );
    }

    @Test
    void testStart() throws InterruptedException {
        // Когда
        monitorService.start();

        // Тогда
        assertTrue(monitorService.isRunning(), "Сервис должен быть запущен");

        // Останавливаем для очистки
        monitorService.stop();
    }

    @Test
    void testStop() throws InterruptedException {
        // Дано
        monitorService.start();
        assertTrue(monitorService.isRunning());

        // Когда
        monitorService.stop();
        Thread.sleep(100); // Даём время на завершение

        // Тогда
        assertFalse(monitorService.isRunning(), "Сервис должен быть остановлен");
    }

    @Test
    void testDoubleStart() throws InterruptedException {
        // Дано
        monitorService.start();

        // Когда
        monitorService.start(); // Попытка запустить второй раз

        // Тогда
        assertTrue(monitorService.isRunning());

        // Очистка
        monitorService.stop();
    }

    @Test
    void testStopWhenNotRunning() {
        // Когда
        monitorService.stop();

        // Тогда
        assertFalse(monitorService.isRunning());
        // Не должно быть исключений
    }

    @Test
    void testIsRunning() {
        // Дано
        assertFalse(monitorService.isRunning(), "Изначально сервис не запущен");

        // Когда
        monitorService.start();

        // Тогда
        assertTrue(monitorService.isRunning());

        // Очистка
        monitorService.stop();
    }
}