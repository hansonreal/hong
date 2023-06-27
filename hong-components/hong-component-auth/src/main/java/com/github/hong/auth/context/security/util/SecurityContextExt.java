package com.github.hong.auth.context.security.util;

import com.github.hong.core.annotation.AnonAccess;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author hanson
 * @since 2023/6/8
 */
@Component
public class SecurityContextExt implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    @PostConstruct
    public Set<String> getAnonymousUrls() {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonAccess.class);
            if (anonymousAccess != null && (infoEntry.getKey().getPatternsCondition() != null)) {
                    anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());

            }
        }
        return anonymousUrls;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
