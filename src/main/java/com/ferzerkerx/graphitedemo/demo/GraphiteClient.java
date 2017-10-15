package com.ferzerkerx.graphitedemo.demo;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.io.IOUtils.closeQuietly;

@Component
public class GraphiteClient implements Closeable {

    private static Logger LOG = LoggerFactory.getLogger(GraphiteClient.class);

    @Nonnull
    private final MetricRegistry metricRegistry;

    @Nonnull
    private Graphite graphite;

    @Nonnull
    private final GraphiteReporter graphiteReporter;


    @Autowired
    public GraphiteClient() throws IOException {
        String hostname = "localhost";
        int port = 2003;

        this.graphite = new Graphite(hostname, port);
        graphite.connect();

        this.metricRegistry = new MetricRegistry();

        graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
                .build(graphite);
        graphiteReporter.start(1, SECONDS);

    }

    public void push(@Nonnull String name, @Nonnull String value) {
        long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(2)); //Docker Image a few hours ago
        try {
            graphite.send(name, value, timestamp);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void markHistogram(@Nonnull String metricName, long value) {
        try {
            Histogram histogram = metricRegistry.histogram(metricName);
            if (histogram != null) {
                histogram.update(value);
            }

        } catch (Exception e) {
            LOG.error("Could not mark histogram for {}.", metricName, e);
        }
    }

    public void markMeter(String metricName) {
        try {

            Meter meter = metricRegistry.meter(metricName);
            if (meter != null) {
                meter.mark();
            }
        } catch (Exception e) {
            LOG.error("Could not mark meter for {}.", metricName, e);
        }
    }


    @Override
    public void close() throws IOException {
        closeQuietly(graphiteReporter);
        closeQuietly(graphite);
    }
}
