package com.ferzerkerx.graphitedemo.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

@SpringBootApplication
@Import({
        AppConfiguration.class
})
public class GraphiteDemoApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(GraphiteDemoApplication.class);
    private static final int NUMBER_OF_VALUES = 3000;

    @Nonnull
    private final GraphiteClient graphiteClient;

    @Autowired
    public GraphiteDemoApplication(@Nonnull GraphiteClient graphiteClient) {
        this.graphiteClient = graphiteClient;
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GraphiteDemoApplication.class);

        try (ConfigurableApplicationContext run = springApplication.run(args)) {
            LOG.info("Application started");
        }
    }

    @Override
    public void run(String... strings) {
        try (Stream<Long> values = createValues()) {
            values.forEach(this::processValue);
        }
    }

    private void processValue(Long value) {
        graphiteClient.push("graphName", Long.toString(value));
        graphiteClient.markHistogram("metricHistogram", value);
        graphiteClient.markMeter("metricMeter");
    }

    @Nonnull
    private Stream<Long> createValues() {
        return Stream.generate(() -> {
            Double value = Math.random() * 1000;
            return value.longValue();
        }).limit(NUMBER_OF_VALUES);
    }
}
