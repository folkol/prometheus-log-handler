package com.folkol.prometheus;

import io.prometheus.client.CollectorRegistry;
import org.junit.Test;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.Assert.assertEquals;

public class PrometheusHandlerTest {

    private Handler target = new PrometheusHandler();

    @Test
    public void testPublishAndCollect() throws Exception {
        LogRecord record = new LogRecord(Level.SEVERE, "lollers!");
        record.setThrown(new RuntimeException("wut"));

        target.publish(record);
        target.publish(record);

        assertEquals(2.0, getSample("RuntimeException_lollers"), 0.1);
    }

    @Test(timeout = 1000)
    public void testDetectCauseLoop() throws Exception {
        LogRecord logRecord = new LogRecord(Level.SEVERE, "Infinite loop");
        Throwable foo = new Throwable();
        Throwable bar = new Throwable(foo);
        foo.initCause(bar);
        logRecord.setThrown(foo);

        target.publish(logRecord);

        assertEquals(1.0, getSample("Throwable_Throwable_Infiniteloop"), 0.1);
    }

    private Double getSample(String java_lang_runtimeException) {
        return CollectorRegistry.defaultRegistry
                   .getSampleValue(
                       PrometheusHandler.HANDLER_NAME,
                       new String[]{PrometheusHandler.CLASS_LABEL},
                       new String[]{java_lang_runtimeException});
    }
}
