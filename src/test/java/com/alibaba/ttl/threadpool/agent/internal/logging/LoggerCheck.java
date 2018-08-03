package com.alibaba.ttl.threadpool.agent.internal.logging;

import org.junit.Test;

import java.util.logging.Level;

public class LoggerCheck {
    @Test
    public void test_JUL_logger() {
        Logger.setLoggerImplType("JUL");

        final Logger logger = Logger.getLogger(LoggerCheck.class);
        logger.log(Level.SEVERE, "Nooooo", new RuntimeException("Bong"));

        logger.info("Hello");
    }
}
