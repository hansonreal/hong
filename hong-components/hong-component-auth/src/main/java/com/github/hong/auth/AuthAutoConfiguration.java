package com.github.hong.auth;

import com.github.hong.auth.config.AuthBeanConfig;
import com.github.hong.auth.config.AuthConfigProperties;
import com.github.hong.auth.config.CaptchaConfigProperties;
import com.github.hong.auth.config.RsaKeyConfigProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 认证自动配置类
 *
 * @author hanson
 * @since 2023/6/5
 */
@EnableConfigurationProperties({
        RsaKeyConfigProperties.class,
        AuthConfigProperties.class,
        CaptchaConfigProperties.class
})
@Import({
        AuthBeanConfig.class
})
@MapperScan("ls.ov.rms.auth.mapper")
@ComponentScan({"ls.ov.rms.auth"})
public class AuthAutoConfiguration {
}
