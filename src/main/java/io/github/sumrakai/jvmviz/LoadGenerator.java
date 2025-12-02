package io.github.sumrakai.jvmviz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Load generator for testing JVM monitoring.
 * Creates threads, allocates memory and triggers garbage collection.
 */
public class LoadGenerator {

    private static final int THREAD_COUNT = 10;
    private static final int MEMORY_MB = 100;
    private static final Random random = new Random();

    private final List<Thread> threads = new ArrayList<>();
    private volatile boolean running = false;

    public void start() {
        if (running) {
            System.out.println("LoadGenerator уже запущен");
            return;
        }

        running = true;
        System.out.println("Запуск генератора нагрузки...");
        System.out.println("Создание " + THREAD_COUNT + " потоков");
        System.out.println("Выделение памяти: ~" + MEMORY_MB + " MB");
        System.out.println();

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(this::workLoop, "LoadThread-" + i);
            thread.setDaemon(true);
            thread.start();
            threads.add(thread);
        }

        Thread memoryThread = new Thread(this::memoryAllocator, "MemoryAllocator");
        memoryThread.setDaemon(true);
        memoryThread.start();
        threads.add(memoryThread);
    }

    public void stop() {
        running = false;
        threads.forEach(Thread::interrupt);
        System.out.println("Генератор нагрузки остановлен");
    }

    private void workLoop() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(random.nextInt(100) + 50);

                List<String> temp = new ArrayList<>();
                for (int i = 0; i < 1000; i++) {
                    temp.add("String-" + i + "-" + random.nextInt());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void memoryAllocator() {
        List<byte[]> memoryChunks = new ArrayList<>();
        int oomCount = 0;
        final int MAX_OOM_ATTEMPTS = 3;

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                byte[] chunk = new byte[MEMORY_MB * 1024 * 1024];
                memoryChunks.add(chunk);

                if (memoryChunks.size() > 5) {
                    memoryChunks.clear();
                    System.gc();
                }

                oomCount = 0;
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (OutOfMemoryError e) {
                oomCount++;
                memoryChunks.clear();
                System.gc();
                
                if (oomCount >= MAX_OOM_ATTEMPTS) {
                    System.err.println("OutOfMemoryError occurred " + MAX_OOM_ATTEMPTS + 
                        " times. Stopping memory allocator.");
                    break;
                }
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}