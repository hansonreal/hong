package com.github.hong.common.thread;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 线程配置
 * 阿里规约：线程资源必须通过线程池提供，不允许在应用中自行显式创建线程
 *
 * @author 35533
 * @since 2023/3/20
 */
@Data
@Accessors(chain = true)
public class ThreadConfig {
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
    private String namePrefix = "rms-async-";

}
