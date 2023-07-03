package com.github.hong.feign.context.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    private long connectTimeout = 60;

    /**
     * 请求最大超时时间,单位秒
     */
    private long readTimeout = 300;

    /**
     * 响应最大超时时间,单位秒
     */
    private long writeTimeout = 300;

    /**
     * 远程调用地址
     */
    private String rpcAddr;

}
