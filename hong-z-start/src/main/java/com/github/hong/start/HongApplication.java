package com.github.hong.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Hello world!
 */
@Slf4j
@SpringBootApplication
public class HongApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HongApplication.class, args);
        ConfigurableEnvironment env = context.getEnvironment();
        try {
            String appName = env.getProperty("spring.application.name");
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");
            String path = env.getProperty("server.servlet.context-path");
            if (StringUtils.hasText(path)){
                if (path.endsWith("/")) {
                    path = path.substring(0, path.length() - 1);
                }
            }else{
                path = "";
            }
            log.info("\n----------------------------------------------------------------------------------------------------\n\t" +
                    "Application " + appName + " is running! Access URLs:\n\t" +
                    "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                    "External:\thttp://" + ip + ":" + port + path + "/\n\t" +
                    "Local Swagger API Doc: \t\thttp://localhost:" + port + path + "/doc.html\n" +
                    "----------------------------------------------------------------------------------------------------");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
