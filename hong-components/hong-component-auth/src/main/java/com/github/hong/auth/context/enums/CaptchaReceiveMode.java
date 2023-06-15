package com.github.hong.auth.context.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码接收方式
 *
 * @author hanson
 * @since 2023/6/8
 */
@Getter
@AllArgsConstructor
public enum CaptchaReceiveMode {
    PHONE("01", "手机"),
    EMAIL("02", "邮箱"),
    WEB("03", "网页"),
    UNKNOWN("99", "暂不支持的验证码接收方式");

    private final String code;
    private final String desc;


    public static CaptchaReceiveMode getEnumByCode(String code) {
        for (CaptchaReceiveMode typeEnum : CaptchaReceiveMode.values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }
        return UNKNOWN;
    }


}
