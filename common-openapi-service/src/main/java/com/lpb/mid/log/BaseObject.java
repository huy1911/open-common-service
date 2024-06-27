package com.lpb.mid.log;

import com.lpb.mid.utils.JsonHelpers;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author vietnq3
 */
public class BaseObject implements Serializable {
    protected static Logger logger = LoggerFactory.getLogger(BaseObject.class);

    public BaseObject() {
    }

    public void writeLogLow(String message) {
        this.writeLog(LogLevel.LOW.getVal(), message);
    }

    public void writeLogLow(String message, Object obj) {
        this.writeLog(LogLevel.LOW.getVal(), message, obj);
    }

    public void writeLogLow(String message, Object obj1, Object obj2) {
        this.writeLog(LogLevel.LOW.getVal(), message, obj1, obj2);
    }

    public void writeLogLow(String message, Object... objects) {
        this.writeLog(LogLevel.LOW.getVal(), message, objects);
    }

    public void writeLogLow(String message, Throwable e) {
        this.writeLog(LogLevel.LOW.getVal(), message, e);
    }

    public void writeLogMedium(String message) {
        this.writeLog(LogLevel.LOW.getVal(), message);
    }

    public void writeLogMedium(String message, Object obj) {
        this.writeLog(LogLevel.LOW.getVal(), message, obj);
    }

    public void writeLogMedium(String message, Object obj1, Object obj2) {
        this.writeLog(LogLevel.LOW.getVal(), message, obj1, obj2);
    }

    public void writeLogMedium(String message, Object... objects) {
        this.writeLog(LogLevel.LOW.getVal(), message, objects);
    }

    public void writeLogMedium(String message, Throwable e) {
        this.writeLog(LogLevel.LOW.getVal(), message, e);
    }

    public void writeLogHigh(String message) {
        this.writeLog(LogLevel.LOW.getVal(), message);
    }

    public void writeLogHigh(String message, Object obj) {
        this.writeLog(LogLevel.LOW.getVal(), message, obj);
    }

    public void writeLogHigh(String message, Object obj1, Object obj2) {
        this.writeLog(LogLevel.LOW.getVal(), message, obj1, obj2);
    }

    public void writeLogHigh(String message, Object... objects) {
        this.writeLog(LogLevel.LOW.getVal(), message, objects);
    }

    public void writeLogHigh(String message, Throwable e) {
        this.writeLog(LogLevel.LOW.getVal(), message, e);
    }

    private void writeLog(String logLevel, String message, Throwable e) {
        ThreadContext.put("logLevel", logLevel);
        logger.error(message, e);
        ThreadContext.remove("logLevel");
    }

    private void writeLog(String logLevel, String message, Object obj) {
        ThreadContext.put("logLevel", logLevel);
        logger.info(message, obj);
        ThreadContext.remove("logLevel");
    }

    private void writeLog(String logLevel, String message, Object obj1, Object obj2) {
        ThreadContext.put("logLevel", logLevel);
        logger.info(message, obj1, obj2);
        ThreadContext.remove("logLevel");
    }

    private void writeLog(String logLevel, String message, Object... objects) {
        ThreadContext.put("logLevel", logLevel);
        logger.info(message, objects);
        ThreadContext.remove("logLevel");
    }

    public void logError(LogLevel logLevel, String message) {
        ThreadContext.put("logLevel", logLevel.getVal());
        logger.error(message);
        ThreadContext.remove("logLevel");
    }

    public void logError(LogLevel logLevel, String message, Object obj) {
        ThreadContext.put("logLevel", logLevel.getVal());
        logger.error(message, obj);
        ThreadContext.remove("logLevel");
    }

    public void logError(LogLevel logLevel, String message, Object obj1, Object obj2) {
        ThreadContext.put("logLevel", logLevel.getVal());
        logger.error(message, obj1, obj2);
        ThreadContext.remove("logLevel");
    }

    public void logError(LogLevel logLevel, String message, Object... objects) {
        ThreadContext.put("logLevel", logLevel.getVal());
        logger.error(message, objects);
        ThreadContext.remove("logLevel");
    }

    public void logError(LogLevel logLevel, String message, Throwable throwable) {
        ThreadContext.put("logLevel", logLevel.getVal());
        logger.error(message, throwable);
        ThreadContext.remove("logLevel");
    }

    public String toString() {
        return JsonHelpers.toJSONString(this);
    }
}
