package com.stockmanagement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

	private static final Logger LOG = LoggerFactory.getLogger(LogUtils.class);


    public static void logDebug(String className, String methodName, String logmessage)
    {
        LOG.debug("class={} method={} logmessage={}", new Object[] {
            className, methodName, logmessage
        });
    }

    public static void logInfo(String className, String methodName, String logmessage)
    {
        LOG.info("class={} method={} logmessage={}", new Object[] {
            className, methodName, logmessage
        });
    }
    
    public static void logError(String className, String methodName, String logmessage)
    {
        LOG.error("class={} method={} logmessage={}", new Object[] {
            className, methodName, logmessage
        });
    }

}
