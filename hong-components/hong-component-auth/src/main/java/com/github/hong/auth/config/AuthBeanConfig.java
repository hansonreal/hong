package com.github.hong.auth.config;

import com.github.hong.auth.context.properties.AuthConfigProperties;
import com.github.hong.auth.context.properties.RsaKeyConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @classname: AuthBeanConfig
 * @desc: Auth模块Bean配置类
 * @author: hanson
 * @history:
 */
@Configuration
@EnableConfigurationProperties({
        AuthConfigProperties.class,
        RsaKeyConfigProperties.class
})
@ComponentScan("com.github.hong.auth")
public class AuthBeanConfig {


}
