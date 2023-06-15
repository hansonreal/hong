package com.github.hong.core.utils;

import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author hanson
 * @since 2023/6/5
 */
public class StringUtil {

    public static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (Exception e) {
            return "";
        }
    }

    public static String decode(String value) {
        try {
            return URLDecoder.decode(value, "utf-8");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getOrDef(String val, String def) {
        return StringUtils.hasText(val) ? def : val;
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "") + Thread.currentThread().getId();
    }

    public static String getUUID(int endAt){
        return getUUID().substring(0, endAt);
    }

}
