package com.github.hong.auth.config;

import ls.ov.rms.auth.context.handler.AuthExceptionHandler;
import ls.ov.rms.common.config.BaseConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 认证配置类
 *
 * @author hanson
 * @since 2023/6/5
 */
@Configuration
public class AuthBeanConfig extends BaseConfig {


    @Bean
    public AuthExceptionHandler authExceptionHandlerBean() {
        return new AuthExceptionHandler();
    }

}
