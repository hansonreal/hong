package com.github.hong.auth.config;

import com.github.hong.auth.context.handler.AuthExceptionHandler;
import com.github.hong.common.config.BaseConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 认证配置类
 *
 * @author hanson
 * @since 2023/6/5
 */
@EnableConfigurationProperties({
        RsaKeyConfigProperties.class,
        AuthConfigProperties.class,
        CaptchaConfigProperties.class
})
@Configuration
@MapperScan("com.github.hong.auth.mapper")
@ComponentScan({"com.github.hong.auth"})
public class AuthBeanConfig extends BaseConfig {


    @Bean
    public AuthExceptionHandler authExceptionHandlerBean() {
        return new AuthExceptionHandler();
    }

}
