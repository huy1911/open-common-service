package com.lpb.mid.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
@Log4j2
public class StringConvertUtils {
    public static <T> T convertValueV2(Object data, Class<T> tClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.convertValue(data, tClass);
        } catch (Exception e) {
            log.error("Error when parse object to object, reason: {}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
    public static String convertToString(Object data) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String str = "";
        try {
            str = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error("Error when parse object to string, reason: {}", ExceptionUtils.getStackTrace(e));
        }
        return str;
    }

    public static <T> T readValueWithInsensitiveProperties(String str, Class<T> tClass) {
        return readValueWithInsensitiveProperties(str, tClass, null);
    }

    public static <T> T readValueWithInsensitiveProperties(String str, Class<T> tClass, PropertyNamingStrategy strategy) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        if (strategy != null) objectMapper.setPropertyNamingStrategy(strategy);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);

        try {

            return objectMapper.readValue(str, tClass);
        } catch (Exception e) {
            log.error("Error when parse string to object, reason: {}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
