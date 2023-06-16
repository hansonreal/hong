package com.github.hong.auth.config;

import com.github.hong.core.utils.RsaUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 认证服务配置类
 *
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = AuthConfigProperties.AUTH_PREFIX)
public class AuthConfigProperties {
    public static final String AUTH_PREFIX = "hong.auth";

    /**
     * access_token 名称
     */
    private String accessTokenName = "iToken";

    /**
     * token失效时间,单位：分钟, 默认60分钟
     */
    private long accessTokenExp = 60;

    /**
     * 是否允许跨域请求 [生产环境建议关闭， 若api与前端项目没有在同一个域名下时，应开启此配置或在nginx统一配置允许跨域]
     **/
    private boolean allowCrossOrigin = true;

    /**
     * 公钥文件路径
     */
    private String pubKeyFilePath;

    /**
     * 私钥文件路径
     */
    private String priKeyFilePath;

    /**
     * 公钥对象
     */
    private PublicKey publicKey;

    /**
     * 私钥对象
     */
    private PrivateKey privateKey;


    /**
     * @return 返回失效时间，单位秒
     */
    public Long getAccessTokenExpSec() {
        return accessTokenExp * 60;
    }


    @PostConstruct
    public void initRsaKey() throws Exception {
        log.info("pubKeyFile:{}", pubKeyFilePath);
        log.info("priKeyFile:{}", priKeyFilePath);
        ClassPathResource pubKeyFileResource = new ClassPathResource(pubKeyFilePath);
        InputStream pubKeyFileInputStream = pubKeyFileResource.getInputStream();
        publicKey = RsaUtil.getPublicKey(pubKeyFileInputStream);
        ClassPathResource priKeyFileResource = new ClassPathResource(priKeyFilePath);
        InputStream priKeyFileInputStream = priKeyFileResource.getInputStream();
        privateKey = RsaUtil.getPrivateKey(priKeyFileInputStream);
    }

}
