package com.github.hong.auth.config;

import com.github.hong.auth.context.properties.AuthConfigProperties;
import com.github.hong.auth.context.properties.CaptchaConfigProperties;
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
@Import({
        AuthBeanConfig.class
})
@EnableConfigurationProperties({
        AuthConfigProperties.class,
        CaptchaConfigProperties.class
})
@MapperScan("com.github.hong.auth.mapper")
@ComponentScan({"com.github.hong.auth"})
public class AuthAutoConfiguration {
}
