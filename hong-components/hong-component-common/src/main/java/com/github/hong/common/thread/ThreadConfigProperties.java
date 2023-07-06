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
     * 线程池维护线程的最少数量，根据机器的配置进行添加
     */
    private int corePoolSize = 20;

    /**
     * 线程池维护线程的最大数量
     */
    private int maxPoolSize = 300;

    /**
     * 缓存队列
     */
    private int queueCapacity = 10;

    /**
     * 允许的空闲时间
     */
    private int keepAliveSeconds = 60;

    /**
     * 配置线程池中的线程的名称前缀
     */
    private String namePrefix = "hong-async-";

}