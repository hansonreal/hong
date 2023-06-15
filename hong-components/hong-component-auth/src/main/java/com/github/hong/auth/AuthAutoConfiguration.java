package com.github.hong.auth;

import com.github.hong.auth.config.AuthBeanConfig;
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
public class AuthAutoConfiguration {
}
