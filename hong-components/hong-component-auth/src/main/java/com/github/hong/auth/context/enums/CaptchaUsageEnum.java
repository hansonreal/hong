package com.github.hong.auth.context.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码用途
 *
 * @author hanson
 * @since 2023/6/8
 */
@Getter
@AllArgsConstructor
public enum CaptchaUsageEnum {

    REGISTER("01", "注册"),
    LOGIN("02", "登录"),
    CHANGE_PD("03", "修改密码"),
    UNKNOWN("99", "改验证码用途暂未实现");

    private final String code;
    private final String desc;


    public static CaptchaUsageEnum getEnumByCode(String code) {
        for (CaptchaUsageEnum typeEnum : CaptchaUsageEnum.values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }
        return UNKNOWN;
    }


}
