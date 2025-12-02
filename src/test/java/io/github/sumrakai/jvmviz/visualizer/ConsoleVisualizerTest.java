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
        ThreadInfo threadInfo = new ThreadInfo(10, 5, 20, 15);
        MemoryInfo memoryInfo = new MemoryInfo(
                100 * 1024 * 1024,
                500 * 1024 * 1024,
                200 * 1024 * 1024,
                10 * 1024 * 1024,
                50 * 1024 * 1024,
                15 * 1024 * 1024
        );
        GcInfo gcInfo = new GcInfo(List.of(
                new GcInfo.GcStats("G1 Young", 10, 500, "Eden, Survivor")
        ));

        visualizer.visualize(threadInfo, memoryInfo, gcInfo);
        String output = outputStream.toString();

        assertNotNull(output);
        assertFalse(output.isEmpty());
        assertTrue(output.contains("Threads"));
        assertTrue(output.contains("Heap Memory"));
        assertTrue(output.contains("Garbage Collectors"));
        assertTrue(output.contains("10"));
    }

    @Test
    void testVisualizeWithNullData() {
        visualizer.visualize(null, null, null);
        String output = outputStream.toString();

        assertFalse(output.contains("Threads"));
    }

    @Test
    void testClearScreen() {
        visualizer.clearScreen();
        String output = outputStream.toString();

        assertTrue(output.contains("\033") || output.isEmpty());
    }

    @Test
    void testProgressBarCalculation() {
        ThreadInfo threadInfo = new ThreadInfo(10, 5, 20, 15);
        MemoryInfo memoryInfo = new MemoryInfo(
                250 * 1024 * 1024,
                500 * 1024 * 1024,
                300 * 1024 * 1024,
                10 * 1024 * 1024,
                50 * 1024 * 1024,
                15 * 1024 * 1024
        );

        visualizer.visualize(threadInfo, memoryInfo, null);
        String output = outputStream.toString();

        assertTrue(output.contains("50") || output.contains("50.0"));
    }
}