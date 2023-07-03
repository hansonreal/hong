package com.github.hong.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtil {

    public static final ObjectMapper mapper = SpringBeanUtil.getBean(ObjectMapper.class);

    /**
     * JSON序列化
     *
     * @param obj 待json序列化对象
     * @return json序列化结果
     */
    public static String serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        return prettyPrinter(obj);
    }

    /**
     * 美化输出
     */
    private static String prettyPrinter(Object obj) {
        try {
            String writeValueAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            return "\n" + writeValueAsString;
        } catch (JsonProcessingException e) {
            log.error("json序列化出错：" + obj, e);
            return null;
        }
    }


    public static <T> T parse(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <E> List<E> parseList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            log.error("json解析出错：" + json, e);
            return Collections.emptyList();
        }
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            log.error("json解析出错：" + json, e);
            return Collections.emptyMap();
        }
    }


    public static <K, V, T> T mapToBean(Map<K, V> map, Class<T> clazz) {
        return mapper.convertValue(map, clazz);
    }


    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }


    public static boolean isJsonFormat(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);
            jsonNode.fieldNames();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isJsonObject(String content) {
        try {
            JsonNode jsonNode = mapper.readValue(content, JsonNode.class);
            return jsonNode.isObject();
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean isJsonArray(String content) {
        try {
            JsonNode jsonNode = mapper.readValue(content, JsonNode.class);
            return jsonNode.isArray();
        } catch (Exception e) {
            return false;
        }
    }


}
