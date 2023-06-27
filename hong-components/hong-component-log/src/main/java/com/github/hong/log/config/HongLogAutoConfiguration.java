package com.github.hong.log.config;

import org.springframework.context.annotation.Import;

/**
 * RMS 日志自动配置类
 *
 * @author hanson
 * @since 2023/6/7
 */
@Import({
        LogBeanConfig.class
})
public class HongLogAutoConfiguration {
}
