package io.github.sumrakai.jvmviz;

import io.github.sumrakai.jvmviz.config.AppConfig;
import io.github.sumrakai.jvmviz.service.MonitorService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main application class for JVM Visualization Tool.
 * Initializes Spring context and starts JVM monitoring.
 */
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        MonitorService monitorService = context.getBean(MonitorService.class);

        final LoadGenerator loadGenerator;
        if (args.length > 0 && "--load".equals(args[0])) {
            loadGenerator = new LoadGenerator();
            loadGenerator.start();
        } else {
            loadGenerator = null;
        }

        monitorService.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (loadGenerator != null) {
                loadGenerator.stop();
            }
            monitorService.stop();
            context.close();
        }));

        try {
            while (monitorService.isRunning()) {
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            monitorService.stop();
            if (loadGenerator != null) {
                loadGenerator.stop();
            }
            context.close();
        }
    }
}