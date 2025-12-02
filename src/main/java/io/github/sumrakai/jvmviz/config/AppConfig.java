package io.github.sumrakai.jvmviz.config;

import io.github.sumrakai.jvmviz.collector.GcCollector;
import io.github.sumrakai.jvmviz.collector.MemoryCollector;
import io.github.sumrakai.jvmviz.collector.ThreadCollector;
import io.github.sumrakai.jvmviz.service.MonitorService;
import io.github.sumrakai.jvmviz.visualizer.ConsoleVisualizer;
import io.github.sumrakai.jvmviz.visualizer.Visualizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the application.
 */
@Configuration
public class AppConfig {

    @Bean
    public ThreadCollector threadCollector() {
        return new ThreadCollector();
    }

    @Bean
    public MemoryCollector memoryCollector() {
        return new MemoryCollector();
    }

    @Bean
    public GcCollector gcCollector() {
        return new GcCollector();
    }

    @Bean
    public Visualizer visualizer() {
        return new ConsoleVisualizer();
    }

    @Bean
    public MonitorService monitorService(ThreadCollector threadCollector,
                                         MemoryCollector memoryCollector,
                                         GcCollector gcCollector,
                                         Visualizer visualizer) {
        return new MonitorService(threadCollector, memoryCollector, gcCollector, visualizer);
    }
}