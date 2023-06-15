package com.github.hong.auth.context.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码类型
 *
 * @author hanson
 * @since 2023/6/8
 */
@Getter
@AllArgsConstructor
public enum CaptchaTypeEnum {

    NUMBER("01", "数字验证码"),
    IMG("02", "图片验证码"),
    UNKNOWN("99", "不持支该类型的验证码");

    private final String code;
    private final String desc;


    public static CaptchaTypeEnum getEnumByCode(String code) {
        for (CaptchaTypeEnum typeEnum : CaptchaTypeEnum.values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }
        return UNKNOWN;
    }


}
