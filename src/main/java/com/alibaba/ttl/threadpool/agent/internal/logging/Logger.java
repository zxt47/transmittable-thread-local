package com.alibaba.ttl.threadpool.agent.internal.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public abstract class Logger {
    private static volatile int loggerImplType = -1;

    public static void setLoggerImplType(String type) {
        if (loggerImplType != -1) {
            throw new IllegalStateException("TTL logger implementation type is already set! type = " + loggerImplType);
        }

        if ("STDERR".equalsIgnoreCase(type)) {
            loggerImplType = 0;
        } else if ("STDOUT".equalsIgnoreCase(type)) {
            loggerImplType = 1;
        } else if ("JUL".equalsIgnoreCase(type)) {
            loggerImplType = 2;
        } else {
            loggerImplType = 0;
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        if (loggerImplType == -1) {
            throw new IllegalStateException("TTL logger implementation type is NOT set!");
        }

        switch (loggerImplType) {
            case 1:
                return new StdOutLogger(clazz);
            case 2:
                return new JucLogger(clazz);
            default:
                return new StdErrorLogger(clazz);
        }
    }

    private final Class<?> clazz;

    private Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void info(String msg) {
        log(Level.INFO, msg, null);
    }

    public abstract void log(Level level, String msg, Throwable thrown);

    private static class StdErrorLogger extends Logger {
        StdErrorLogger(Class<?> clazz) {
            super(clazz);
        }

        @Override
        public void log(Level level, String msg, Throwable thrown) {
            if (level == Level.SEVERE) {
                System.err.println(level + ": " + msg);
                if (thrown != null) thrown.printStackTrace();
            }
        }
    }

    private static class StdOutLogger extends Logger {
        StdOutLogger(Class<?> clazz) {
            super(clazz);
        }

        @Override
        public void log(Level level, String msg, Throwable thrown) {
            System.out.println(level + ": " + msg);
            if (thrown != null) thrown.printStackTrace(System.out);
        }
    }


    private static class JucLogger extends Logger {
        private final java.util.logging.Logger logger;

        JucLogger(Class<?> clazz) {
            super(clazz);

            logger = java.util.logging.Logger.getLogger(clazz.getName());
        }

        @Override
        public void log(Level level, String msg, Throwable thrown) {
            // millis and thread are filled by the constructor
            LogRecord record = new LogRecord(level, msg);
            record.setLoggerName(logger.getName());
            record.setThrown(thrown);
            // Note: parameters in record are not set because SLF4J only
            // supports a single formatting style
            fillCallerData(logger.getName(), record);
            logger.log(record);
        }

        /**
         * Fill in caller data if possible.
         *
         * @param record The record to update
         */
        private void fillCallerData(String callerFQCN, LogRecord record) {
            StackTraceElement[] steArray = new Throwable().getStackTrace();

            int selfIndex = -1;
            for (int i = 0; i < steArray.length; i++) {
                final String className = steArray[i].getClassName();
                if (className.equals(callerFQCN) || className.equals(JucLogger.class.getName())) {
                    selfIndex = i;
                    break;
                }
            }

            int found = -1;
            for (int i = selfIndex + 1; i < steArray.length; i++) {
                final String className = steArray[i].getClassName();
                if (!(className.equals(callerFQCN) || className.equals(JucLogger.class.getName()))) {
                    found = i;
                    break;
                }
            }

            if (found != -1) {
                StackTraceElement ste = steArray[found];
                // setting the class name has the side effect of setting
                // the needToInferCaller variable to false.
                record.setSourceClassName(ste.getClassName());
                record.setSourceMethodName(ste.getMethodName());
            }
        }
    }
}
