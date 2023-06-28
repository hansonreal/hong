package com.github.hong.common.config;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hong.common.undertow.UndertowServerFactoryCustomizer;
import com.github.hong.common.aspect.RedissonLockAspect;
import com.github.hong.common.aspect.RetryAspect;
import com.github.hong.core.utils.SpringBeanUtil;
import io.undertow.Undertow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 抽象配置类
 *
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
public abstract class BaseConfig {


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
}
