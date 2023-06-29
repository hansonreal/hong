package com.github.hong.common.thread;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程配置
 * 阿里规约：线程资源必须通过线程池提供，不允许在应用中自行显式创建线程
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = ThreadConfigProperties.THREAD_PREFIX)
public class ThreadConfigProperties {

    public static final String THREAD_PREFIX = "hong.thread";
    /**
     * 配置核心线程数
     */
    private int corePoolSize = 5;

    /**
     * 配置最大线程数
     */
    private int maxPoolSize = 10;

    /**
     * 配置队列大小
     */
    private int queueCapacity = 100;


    /**
     * 配置线程池中的线程的名称前缀
     */
    private String namePrefix = "hong-async-";

}