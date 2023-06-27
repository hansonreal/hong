package com.github.hong.support.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Support 模块 自动配置类
 *
 * @author hanson
 * @since 2023/6/26
 */
@Import({
        SupportBeanConfig.class
})
@MapperScan("com.github.hong.support.mapper")
@ComponentScan({"com.github.hong.support"})
public class SupportAutoConfiguration {
}
