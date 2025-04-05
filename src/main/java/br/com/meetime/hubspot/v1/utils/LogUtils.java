package br.com.meetime.hubspot.v1.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger("HubSpotLogger");

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void trace(String message) {
        logger.trace(message);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
