package com.lpb.mid.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.*;

public class JsonHelpers {
    private static final Logger logger = LoggerFactory.getLogger(JsonHelpers.class);
    private static ObjectMapper objectMapper;
    private static ObjectMapper snakeCaseObjectMapper;

    public JsonHelpers() {
    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
        return objectMapper;
    }

    public static ObjectMapper getSnakeCaseObjectMapper() {
        if (snakeCaseObjectMapper == null) {
            snakeCaseObjectMapper = new ObjectMapper();
            snakeCaseObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            snakeCaseObjectMapper.registerModule(new JavaTimeModule());
            snakeCaseObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            snakeCaseObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            snakeCaseObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        }
        return snakeCaseObjectMapper;
    }

    public static CollectionType getCollectionType(Class<?> clazzz) {
        return getObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, clazzz);
    }

    public static String stringify(Object object) {
        return toJSONString(object);
    }

    public static String toJSONString(Object model) {
        try {
            String json = getObjectMapper().writeValueAsString(model);
            return json;
        } catch (Exception var2) {
            logger.error("Java to Json exception", var2);
            return null;
        }
    }

    public static <T> T toJavaObject(String jsonStr, Class<T> clazz) {
        try {
            T model = getObjectMapper().readValue(jsonStr, clazz);
            return model;
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static <T> T toJavaObject(Object obj, Class<T> clazz) {
        try {
            T model = getObjectMapper().convertValue(obj, clazz);
            return model;
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static Map<String, Object> parseObject(String jsonStr) {
        try {
            JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(Map.class, String.class, Object.class);
            return getObjectMapper().readValue(jsonStr, javaType);
        } catch (Exception var2) {
            logger.error("Json to Java exception", var2);
            return null;
        }
    }

    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        return toJavaObject(jsonStr, clazz);
    }

    public static <T> T parseObject(String jsonStr, TypeReference<T> typeReference) {
        try {
            T model = getObjectMapper().readValue(jsonStr, typeReference);
            return model;
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
        try {
            JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(List.class, clazz);
            return getObjectMapper().readValue(jsonStr, javaType);
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static <T> T convertObject(Object obj, Class<T> clazz) {
        return toJavaObject(obj, clazz);
    }

    public static <T> T convertObject(Object obj, TypeReference<T> typeReference) {
        try {
            T model = getObjectMapper().convertValue(obj, typeReference);
            return model;
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static <T> List<T> convertArray(Object obj, Class<T> clazz) {
        try {
            JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(List.class, clazz);
            return getObjectMapper().convertValue(obj, javaType);
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static Map toMap(String jsonStr) {
        return (Map) toJavaObject(jsonStr, Map.class);
    }

    public static byte[] toJsonBytes(Object obj) {
        if (obj == null) {
            return "{}".getBytes(Charset.forName("UTF-8"));
        } else {
            try {
                return getObjectMapper().writeValueAsBytes(obj);
            } catch (Exception var2) {
                logger.error("Json to bytes exception", var2);
                return null;
            }
        }
    }

    public static JSONObject optJSONObject(JSONObject object, Iterator<String> iterator) {
        if (object != null) {
            return iterator.hasNext() ? optJSONObject(object.optJSONObject((String) iterator.next()), iterator) : object;
        } else {
            return null;
        }
    }

    public static String toSnakeCaseJSONString(Object model) {
        try {
            String json = getSnakeCaseObjectMapper().writeValueAsString(model);
            return json;
        } catch (Exception var2) {
            logger.error("Java to Json exception", var2);
            return null;
        }
    }

    public static <T> T toSnakeCaseJavaObject(String jsonStr, Class<T> clazz) {
        try {
            T model = getSnakeCaseObjectMapper().readValue(jsonStr, clazz);
            return model;
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static <T> T toSnakeCaseJavaObject(Object obj, Class<T> clazz) {
        try {
            T model = getSnakeCaseObjectMapper().convertValue(obj, clazz);
            return model;
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static Map<String, Object> parseSnakeCaseObject(String jsonStr) {
        try {
            JavaType javaType = getSnakeCaseObjectMapper().getTypeFactory().constructParametricType(Map.class, String.class, Object.class);
            return getSnakeCaseObjectMapper().readValue(jsonStr, javaType);
        } catch (Exception var2) {
            logger.error("Json to Java exception", var2);
            return null;
        }
    }

    public static <T> T parseSnakeCaseObject(String jsonStr, Class<T> clazz) {
        return toSnakeCaseJavaObject(jsonStr, clazz);
    }

    public static <T> T parseSnakeCaseObject(String jsonStr, TypeReference<T> typeReference) {
        try {
            T model = getSnakeCaseObjectMapper().readValue(jsonStr, typeReference);
            return model;
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static <T> List<T> parseSnakeCaseArray(String jsonStr, Class<T> clazz) {
        try {
            JavaType javaType = getSnakeCaseObjectMapper().getTypeFactory().constructParametricType(List.class, clazz);
            return getSnakeCaseObjectMapper().readValue(jsonStr, javaType);
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static <T> T convertSnakeCaseObject(Object obj, Class<T> clazz) {
        return toSnakeCaseJavaObject(obj, clazz);
    }

    public static <T> T convertSnakeCaseObject(Object obj, TypeReference<T> typeReference) {
        try {
            T model = getSnakeCaseObjectMapper().convertValue(obj, typeReference);
            return model;
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }

    public static <T> List<T> convertSnakeCaseArray(Object obj, Class<T> clazz) {
        try {
            JavaType javaType = getSnakeCaseObjectMapper().getTypeFactory().constructParametricType(List.class, clazz);
            return getSnakeCaseObjectMapper().convertValue(obj, javaType);
        } catch (Exception var3) {
            logger.error("Json to Java exception", var3);
            return null;
        }
    }
}
