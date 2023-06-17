package com.github.hong.auth.context.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 验证码配置参数对象
 * @author hanson
 * @since 2023/6/12
 */
@Data
@ConfigurationProperties(prefix = CaptchaConfigProperties.CAPTCHA_PREFIX)
public class CaptchaConfigProperties {
    public static final String CAPTCHA_PREFIX = "hong.captcha";

    /**
     * 是否开启验证码
     */
    private boolean enabled = true;

    /**
     * 验证码有效期,单位：秒, 默认1分钟
     */
    private long expire = 300;

}
