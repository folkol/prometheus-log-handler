package com.folkol.prometheus;

import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PrometheusHandlerTest {
    @Test
    public void testCallback() throws Exception {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.addHandler(new PrometheusHandler());

        Logger logger = Logger.getLogger(PrometheusHandlerTest.class.getName());
        logger.log(Level.SEVERE, "lollers", new RuntimeException("nisse"));
        logger.log(Level.SEVERE, "lollers", new RuntimeException("nisse"));
        logger.log(Level.SEVERE, "lollers", new IllegalArgumentException("sadada"));
    }
}
