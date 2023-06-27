package com.github.hong.log.context.properties;

import com.github.hong.common.thread.ThreadConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author hanson
 * @since 2023/6/7
 */
@Data
@ConfigurationProperties(prefix = LogConfigProperties.HONG_LOG_PREFIX)
public class LogConfigProperties {
    public static final String HONG_LOG_PREFIX = "hong.log";

    /**
     * 日志线程池配置
     */
    private ThreadConfig thread = new ThreadConfig().setNamePrefix("hong-async-log");

}
