package com.folkol.prometheus;

import io.prometheus.client.Counter;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class PrometheusHandler extends Handler {
    public static final String HANDLER_NAME = "log_messages_total";
    public static final String CLASS_LABEL = "cause";
    private static final Counter counter =
        Counter.build()
            .name(HANDLER_NAME)
            .help("This counter will count Java Util Logging messages.")
            .labelNames(CLASS_LABEL)
            .register();

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel().intValue() < Level.WARNING.intValue()) {
            return;
        }
        String label = calculateLabel(record);
        counter.labels(label).inc();
    }

    private String calculateLabel(LogRecord record) {
        Set<Throwable> seen = new HashSet<>();
        StringJoiner stringJoiner = new StringJoiner("_");
        Throwable cause = record.getThrown();
        while (cause != null && !seen.contains(cause)) {
            stringJoiner.add(cause.getClass().getSimpleName());
            seen.add(cause);
            cause = cause.getCause();
        }
        stringJoiner.add(record.getMessage());
        return stringJoiner.toString().replaceAll("\\W+", "");
    }

    @Override
    public void flush() {
        // NOP
    }

    @Override
    public void close() throws SecurityException {
        // NOP
    }
}
