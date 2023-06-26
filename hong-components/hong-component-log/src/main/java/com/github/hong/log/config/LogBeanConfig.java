package com.github.hong.log.config;

import com.github.hong.common.config.BaseConfig;
import com.github.hong.common.thread.ThreadConfig;
import com.github.hong.common.thread.ThreadPoolTaskExecutorWrapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 日志模块配置类
 *
 * @author hanson
 * @since 2023/6/7
 */
@Configuration
@MapperScan("com.github.hong.log.mapper")
@ComponentScan("com.github.hong.log")
@EnableConfigurationProperties(LogConfigProperties.class)
public class LogBeanConfig extends BaseConfig {

    @Bean(name = "logAsyncServiceExecutor")
    public Executor orderAsyncServiceExecutor(
            LogConfigProperties properties) {
        ThreadConfig thread = properties.getThread();
        //SpringBoot项目，可使用Spring提供的对 ThreadPoolExecutor 封装的线程池 ThreadPoolTaskExecutor：
        // ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        ThreadPoolTaskExecutorWrapper executor = new ThreadPoolTaskExecutorWrapper();//自定义ThreadPoolTaskExecutor，会打印线程池情况
        //配置核心线程数
        executor.setCorePoolSize(thread.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(thread.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(thread.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(thread.getNamePrefix());
        /*
         * 当达到最大线程数时如何处理新任务
         * AbortPolicy:丢弃任务并抛出RejectedExecutionException异常
         * DiscardPolicy:丢弃任务，但是不抛出异常。如果线程队列已满，则后续提交的任务都会被丢弃，且是静默丢弃
         * DiscardOldestPolicy:丢弃队列最前面的任务，然后重新提交被拒绝的任务
         * CallerRunsPolicy:由调用线程处理该任务
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

}
