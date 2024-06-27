package com.lpb.mid.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public static String convertStringToLocalDateTime(Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSSS");
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            logger.error("Error when parse date time: {}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }
}
