package com.github.hong.feign.config;

import com.github.hong.common.config.BaseConfig;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author hanson
 * @since 2023/5/18
 */
@Slf4j
@Configuration
@ComponentScan("com.github.hong.feign")
public class FeignBeanConfig extends BaseConfig {

    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder() {
        return new SpringFormEncoder(new JacksonEncoder());
    }

    @Bean("feignEncoder")
    public Encoder feignEncoder() {
        return new JacksonEncoder();
    }

    @Bean("feignDecoder")
    public Decoder feignDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    public Client client(okhttp3.OkHttpClient okHttpClient) {
        return new feign.okhttp.OkHttpClient(okHttpClient);
    }


    @Bean
    public Contract contract() {
        return new Contract.Default();
    }

    @Bean
    public Logger.Level logLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(60L, TimeUnit.SECONDS, 300L, TimeUnit.SECONDS, true);
    }

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }
}
