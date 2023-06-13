package com.github.hong.auth.context.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @classname: AuthConfigProperties
 * @desc: Auth模块属性配置类
 * @author: hanson
 * @history:
 */
@ConfigurationProperties(prefix = AuthConfigProperties.AUTH_PREFIX)
public class AuthConfigProperties {
    public static final String AUTH_PREFIX = "hong.auth";
}
