package com.github.hong.feign;

import com.github.hong.feign.config.FeignBeanConfig;
import com.github.hong.feign.context.hystrix.RequestAttributeHystrixConcurrencyStrategy;
import com.github.hong.feign.context.interceptor.CustomizeRequestInterceptor;
import com.github.hong.feign.context.properties.FeignProperties;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Feign自动配置类
 *
 * @author hanson
 * @since 2023/5/18
 */
@Import({
        FeignBeanConfig.class
})
@EnableConfigurationProperties(FeignProperties.class)
public class FeignAutoConfiguration {

    @Bean
    @ConditionalOnClass(OkHttpClient.class)
    @ConditionalOnMissingBean(OkHttpClient.class)
    public OkHttpClient okHttpClient(FeignProperties feignProperties) {
        long connectTimeout = feignProperties.getConnectTimeout();
        long readTimeout = feignProperties.getReadTimeout();
        long writeTimeout = feignProperties.getWriteTimeout();
        return new OkHttpClient().newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
    }


    @Bean
    public RequestAttributeHystrixConcurrencyStrategy hystrixRequestAutoConfiguration() {
        return new RequestAttributeHystrixConcurrencyStrategy();
    }


    @Bean
    public RequestInterceptor requestInterceptor() {
        return new CustomizeRequestInterceptor();
    }

    @Bean
    public HystrixFeign.Builder builder(Encoder feignEncoder,
                                        Decoder feignDecoder,
                                        Client client,
                                        Contract contract,
                                        List<RequestInterceptor> requestInterceptors,
                                        Logger.Level logLevel,
                                        Request.Options options,
                                        Retryer retryer) {
        return HystrixFeign.builder()
                .decoder(feignDecoder)
                .encoder(feignEncoder)
                .client(client)
                .contract(contract)
                .logLevel(logLevel)
                .requestInterceptors(requestInterceptors)
                .options(options)
                .retryer(retryer);
    }


}
