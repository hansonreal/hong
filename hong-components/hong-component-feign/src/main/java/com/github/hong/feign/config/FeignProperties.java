package com.github.hong.feign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * feign配置文件
 *
 * @author hanson
 * @since 2023/5/18
 */
@Data
@ConfigurationProperties(prefix = FeignProperties.FEIGN_PREFIX)
public class FeignProperties {

    public static final String FEIGN_PREFIX = "hong.feign";

    /**
     * 与服务地址建立连接的最大超时时间
     */
    private Duration connectTimeout = Duration.ofSeconds(60);

    /**
     * 请求最大超时时间,单位秒
     */
    private Duration readTimeout = Duration.ofSeconds(300);

    /**
     * 响应最大超时时间,单位秒
     */
    private Duration writeTimeout = Duration.ofSeconds(300);

    /**
     * 远程调用地址
     */
    private String rpcAddr;

}
