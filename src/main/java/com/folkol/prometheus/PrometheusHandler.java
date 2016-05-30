package com.folkol.prometheus;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class PrometheusHandler extends Handler {
    @Override
    public void publish(LogRecord record) {
        System.err.println("LOLLERS!!!!!!!!!!!!" + record.getMessage());
    }

    @Override
    public void flush() {
        System.err.println("Flushing");
    }

    @Override
    public void close() throws SecurityException {
        System.err.println("Closing");
    }
}
