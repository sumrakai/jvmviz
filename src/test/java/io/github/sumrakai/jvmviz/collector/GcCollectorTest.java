package io.github.sumrakai.jvmviz.collector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для GcCollector.
 */
class GcCollectorTest {

    private GcCollector collector;

    @BeforeEach
    void setUp() {
        collector = new GcCollector();
    }

    @Test
    void testCollect() {
        // Когда
        collector.collect();

        // Тогда
        GcInfo info = collector.getGcInfo();
        assertNotNull(info, "GcInfo должен быть собран");
        assertNotNull(info.collectors(), "Список коллекторов не должен быть null");
        assertFalse(info.collectors().isEmpty(),
                "Должен быть хотя бы один сборщик мусора");
    }

    @Test
    void testGetName() {
        // Когда
        String name = collector.getName();

        // Тогда
        assertEquals("GcCollector", name);
    }

    @Test
    void testGcStats() {
        // Когда
        collector.collect();
        GcInfo info = collector.getGcInfo();

        // Тогда
        assertNotNull(info);
        for (GcInfo.GcStats stats : info.collectors()) {
            assertNotNull(stats.name(), "Имя GC не должно быть null");
            assertFalse(stats.name().isEmpty(), "Имя GC не должно быть пустым");
            assertTrue(stats.collectionCount() >= 0,
                    "Количество сборок не может быть отрицательным");
            assertTrue(stats.collectionTime() >= 0,
                    "Время сборки не может быть отрицательным");
        }
    }

    @Test
    void testTotalCollectionCount() {
        // Когда
        collector.collect();
        GcInfo info = collector.getGcInfo();

        // Тогда
        assertNotNull(info);
        long total = info.getTotalCollectionCount();
        assertTrue(total >= 0, "Общее количество сборок не может быть отрицательным");

        // Проверяем, что сумма равна сумме всех коллекторов
        long sum = info.collectors().stream()
                .mapToLong(GcInfo.GcStats::collectionCount)
                .sum();
        assertEquals(total, sum, "Общее количество должно равняться сумме всех GC");
    }

    @Test
    void testFormatTime() {
        // Когда
        String ms = GcInfo.formatTime(500);
        String seconds = GcInfo.formatTime(1500);
        String minutes = GcInfo.formatTime(65000);

        // Тогда
        assertTrue(ms.contains("ms"), "Меньше секунды должно форматироваться как ms");
        assertTrue(seconds.contains("s"), "Больше секунды должно форматироваться как s");
        assertTrue(minutes.contains("min"), "Больше минуты должно форматироваться как min");
    }
}