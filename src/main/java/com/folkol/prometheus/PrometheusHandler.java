package com.folkol.prometheus;

import io.prometheus.client.Counter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class PrometheusHandler extends Handler {
    static final Map<Class, Counter> counters = new HashMap<>();

    public PrometheusHandler() {
        System.out.println("wut");
    }

    @Override
    public void publish(LogRecord record) {
        Class<? extends Throwable> klass = record.getThrown().getClass();
        Counter counter = counters.get(klass);
        String label = klass.getName().replaceAll("[^a-zA-Z=]", "_");
        if(counter == null) {
            counter = Counter.build()
                          .name("log-handler")
                          .help(record.toString().replaceAll("[^a-zA-Z]", "-"))
                          .labelNames(label)
                          .register();
            counters.put(klass, counter);
        }
        counter.labels(label).inc();
    }

    @Override
    public void flush() {
        System.err.println("Flushing");
    }

    @Override
    public void close() throws SecurityException {
        System.err.println("Closing");
        for(Counter c : counters.values()) {
            System.out.println(c.collect());
        }
    }
}
