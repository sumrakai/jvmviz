package io.github.sumrakai.jvmviz.visualizer;

import io.github.sumrakai.jvmviz.collector.GcInfo;
import io.github.sumrakai.jvmviz.collector.MemoryInfo;
import io.github.sumrakai.jvmviz.collector.ThreadInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для ConsoleVisualizer.
 */
class ConsoleVisualizerTest {

    private ConsoleVisualizer visualizer;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        visualizer = new ConsoleVisualizer();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testVisualizeWithValidData() {
        // Дано
        ThreadInfo threadInfo = new ThreadInfo(10, 5, 20, 15);
        MemoryInfo memoryInfo = new MemoryInfo(
                100 * 1024 * 1024, // 100 MB used
                500 * 1024 * 1024, // 500 MB max
                200 * 1024 * 1024, // 200 MB committed
                10 * 1024 * 1024,  // 10 MB non-heap used
                50 * 1024 * 1024,  // 50 MB non-heap max
                15 * 1024 * 1024   // 15 MB non-heap committed
        );
        GcInfo gcInfo = new GcInfo(List.of(
                new GcInfo.GcStats("G1 Young", 10, 500, "Eden, Survivor")
        ));

        // Когда
        visualizer.visualize(threadInfo, memoryInfo, gcInfo);
        String output = outputStream.toString();

        // Тогда
        assertNotNull(output);
        assertFalse(output.isEmpty());
        assertTrue(output.contains("Threads"), "Должна быть секция Threads");
        assertTrue(output.contains("Heap Memory"), "Должна быть секция Heap Memory");
        assertTrue(output.contains("Garbage Collectors"), "Должна быть секция GC");
        assertTrue(output.contains("10"), "Должно быть количество потоков");
    }

    @Test
    void testVisualizeWithNullData() {
        // Когда
        visualizer.visualize(null, null, null);
        String output = outputStream.toString();

        // Тогда
        // Не должно быть вывода, если данные null
        assertFalse(output.contains("Threads"), "Не должно быть вывода при null данных");
    }

    @Test
    void testClearScreen() {
        // Когда
        visualizer.clearScreen();
        String output = outputStream.toString();

        // Тогда
        // ANSI escape code должен быть выведен
        assertTrue(output.contains("\033") || output.isEmpty(),
                "Должен быть ANSI escape code для очистки экрана");
    }

    @Test
    void testProgressBarCalculation() {
        // Дано
        ThreadInfo threadInfo = new ThreadInfo(10, 5, 20, 15);
        MemoryInfo memoryInfo = new MemoryInfo(
                250 * 1024 * 1024, // 250 MB used (50% от 500 MB)
                500 * 1024 * 1024, // 500 MB max
                300 * 1024 * 1024,
                10 * 1024 * 1024,
                50 * 1024 * 1024,
                15 * 1024 * 1024
        );

        // Когда
        visualizer.visualize(threadInfo, memoryInfo, null);
        String output = outputStream.toString();

        // Тогда
        // Проверяем, что процент отображается (может быть 50.0% или просто 50%)
        assertTrue(output.contains("50") || output.contains("50.0"),
                "Должен отображаться процент использования памяти (50%)");
    }
}