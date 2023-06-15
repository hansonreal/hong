package com.github.hong.auth.context.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证类型
 * AW_IDENTITY_TYPE
 * 01:邮箱
 * 02:手机号码
 *
 * @author hanson
 * @since 2023/6/5
 */
@Getter
@AllArgsConstructor
public enum IDTypeEnum {
    EMAIL("01"),
    MOBILE("02");

    private final String idType;
}
