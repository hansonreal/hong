package com.github.hong.common.config;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hong.common.aspect.RedissonLockAspect;
import com.github.hong.common.aspect.RetryAspect;
import com.github.hong.common.thread.ThreadConfigProperties;
import com.github.hong.common.thread.ThreadPoolTaskExecutorWrapper;
import com.github.hong.common.undertow.UndertowServerFactoryCustomizer;
import com.github.hong.core.utils.SpringBeanUtil;
import com.github.hong.core.web.filter.RequestBodyFilter;
import io.undertow.Undertow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.servlet.Filter;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 抽象配置类
 *
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
public abstract class BaseConfig {


    @Bean
    public ThreadConfigProperties threadConfigProperties() {
        return new ThreadConfigProperties();
    }

    /**
     * 自定义线程池配置类。
     * 不要命名为 taskScheduler，与spring框架的bean重名。
     *
     * @return
     */
    @Bean(name = "hongAsyncExecutor")
    public Executor hongAsyncExecutor(
            ThreadConfigProperties properties) {
        //log.info("订单线程池配置信息:\n{}", JsonUtil.serialize(thread));
        //SpringBoot项目，可使用Spring提供的对 ThreadPoolExecutor 封装的线程池 ThreadPoolTaskExecutor：
        // ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        ThreadPoolTaskExecutorWrapper executor = new ThreadPoolTaskExecutorWrapper();//自定义ThreadPoolTaskExecutor，会打印线程池情况
        //配置核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(properties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(properties.getNamePrefix());
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


    @Bean
    @ConditionalOnMissingBean(RetryAspect.class)
    public RetryAspect retryAspect() {
        return new RetryAspect();
    }

    @Bean
    @ConditionalOnMissingBean(RedissonLockAspect.class)
    public RedissonLockAspect redissonLockAspect() {
        return new RedissonLockAspect();
    }

    @Bean
    @ConditionalOnMissingBean(SpringBeanUtil.class)
    public SpringBeanUtil getSpringUtils(ApplicationContext applicationContext) {
        SpringBeanUtil.setApplicationContext(applicationContext);
        return new SpringBeanUtil();
    }


    /**
     * 基于ORACLE 连接驱动
     *
     * @return 返回序列生成方式
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "oracle.jdbc.OracleDriver", matchIfMissing = true)
    @ConditionalOnMissingBean(OracleKeyGenerator.class)
    public IKeyGenerator oracleKeyGenerator() {
        return new OracleKeyGenerator();
    }


    /**
     * 定制 Undertow 服务
     *
     * @return UndertowServerFactoryCustomizer
     */
    @Bean
    @ConditionalOnClass(Undertow.class)
    public UndertowServerFactoryCustomizer getUndertowServerFactoryCustomizer() {
        return new UndertowServerFactoryCustomizer();
    }


    /**
     * redis模板类
     *
     * @param factory redis链接工厂
     * @return redis模板类
     */
    @Bean(name = {"redisTemplate", "stringRedisTemplate"})
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(factory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jacksonSeial = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jacksonSeial.setObjectMapper(om);

        // 值采用json序列化
        redisTemplate.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置hash key 和value序列化模式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jacksonSeial);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //1、多租户插件
        //TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor();
        //interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        //2、动态表名插件
        //DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        //interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        //3、分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(5000L);//限制最大的分页条数
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        /*4、乐观锁插件，实体类上需要有以下内容
         * @Version
         * private Integer version;
         */
        //OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        //interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);
        //5、sql 性能规范插件
        //  IllegalSQLInnerInterceptor illegalSQLInnerInterceptor = new IllegalSQLInnerInterceptor();
        //  interceptor.addInnerInterceptor(illegalSQLInnerInterceptor);
        //6、防止全表更新与删除插件
        //  BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        //  interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return interceptor;
    }


    /**
     * 实现可重复读Request
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<Filter> addRequestBodyFilter() {
        RequestBodyFilter requestBodyFilter = new RequestBodyFilter();
        FilterRegistrationBean<Filter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(requestBodyFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MAX_VALUE);
        return registrationBean;
    }
}
