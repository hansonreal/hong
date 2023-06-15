package com.github.hong.auth.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ls.ov.rms.utils.RsaUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import static ls.ov.rms.auth.config.RsaKeyConfigProperties.RSA_PREFIX;

/**
 * RSA密钥配置类
 * @author hanson
 * @since 2023/6/6
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = RSA_PREFIX)
public class RsaKeyConfigProperties {

    public static final String RSA_PREFIX = "rms.rsa.key";

    private String pubKeyFilePath;
    private String priKeyFilePath;

    private PublicKey publicKey;
    private PrivateKey privateKey;

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
