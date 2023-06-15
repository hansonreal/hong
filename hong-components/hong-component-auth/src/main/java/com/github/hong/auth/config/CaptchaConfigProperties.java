package com.github.hong.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static ls.ov.rms.auth.config.CaptchaConfigProperties.CAPTCHA_PREFIX;

/**
 * 验证码配置参数对象
 * @author hanson
 * @since 2023/6/12
 */
@Data
@ConfigurationProperties(prefix = CAPTCHA_PREFIX)
public class CaptchaConfigProperties {
    public static final String CAPTCHA_PREFIX = "rms.captcha";

    /**
     * 是否开启验证码
     */
    private boolean enabled = true;

    /**
     * 验证码有效期,单位：秒, 默认1分钟
     */
    private long expire = 300;

}
