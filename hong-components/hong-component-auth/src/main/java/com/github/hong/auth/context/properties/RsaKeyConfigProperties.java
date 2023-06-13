package com.github.hong.auth.context.properties;

import com.github.hong.common.utils.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @classname: RsaKeyConfigProperties
 * @author: hanson
 * @history: 2023年06月11日 15时15分59秒
 */
@Slf4j
@ConfigurationProperties(prefix = RsaKeyConfigProperties.RSA_KEY_PREFIX)
public class RsaKeyConfigProperties {
    public static final String RSA_KEY_PREFIX = "hong.rsa.key";


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
