package com.github.hong.core.utils;

/**
 * 正则工具
 *
 * @author hanson
 * @since 2023/6/5
 */
public class RegUtil {

    public static final String REG_CN_MOBILE = "^(?:0|86|\\+86)?1[3-9]\\d{9}$"; //判断是否是手机号

    public static final String REG_EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

    public static final String REG_NP_MOBILE = "^(?:0|977|\\+977)?\\d{6,}$";

    public static boolean isCnMobile(String str) {
        return match(str, REG_CN_MOBILE);
    }

    public static boolean isNpMobile(String str) {
        return match(str, REG_NP_MOBILE);
    }

    public static boolean isEmail(String str) {
        return match(str, REG_EMAIL);
    }

    /**
     * 正则验证
     */
    public static boolean match(String text, String reg) {
        if (text == null) {
            return false;
        }
        return text.matches(reg);
    }


}
