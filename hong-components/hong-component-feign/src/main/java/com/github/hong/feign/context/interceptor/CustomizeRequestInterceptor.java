package com.github.hong.feign.context.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 自定义Feign请求拦截器(需要交给Spring容器)
 *
 * @author hanson
 * @since 2023/7/3
 */
@Slf4j
public class CustomizeRequestInterceptor implements RequestInterceptor {
    /**
     * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
     * <p>
     * 使用 RequestContextHolder.getRequestAttributes() 静态方法获得Request。 (但仅限于Feign不开启Hystrix支持时。)
     * 当Feign开启Hystrix支持时，获取值为null
     * 原因在于，Hystrix的默认隔离策略是THREAD 。而 RequestContextHolder 源码中，使用了两个ThreadLocal 。
     * 解决方案一：调整隔离策略 将隔离策略设为SEMAPHORE即可
     * hystrix.command.default.execution.isolation.strategy: SEMAPHORE
     * 这样配置后，Feign可以正常工作。但该方案不是特别好。原因是Hystrix官方强烈建议使用THREAD作为隔离策略！
     * <p>
     * 解决方案二：自定义并发策略
     * 既然Hystrix不太建议使用SEMAPHORE作为隔离策略，那么是否有其他方案呢？
     * 答案是自定义并发策略，目前，Spring Cloud Sleuth以及Spring Security都通过该方式传递 ThreadLocal 对象。
     * 编写自定义并发策略比较简单，只需编写一个类，让其继承HystrixConcurrencyStrategy ，并重写wrapCallable 方法即可。
     * </p>
     *
     * @param requestTemplate Feign请求
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("============================ [begin] feign api  ============================");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("--当前上下文对象为空--");
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        log.info("当前界面请求URI: {}", request.getRequestURI());
        log.info("Feign的请求URI: {}", requestTemplate.path());
        log.info("Feign的请求方式: {}", requestTemplate.method());

        // 请求头处理
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                // 拷贝当前URI请求头信息
                requestTemplate.header(name, values);
                log.info("{}: \t\t {}", name, values);
            }
        }
        // 请求行信息
        String queryLine = requestTemplate.queryLine();
        if (queryLine.startsWith("?")) {
            queryLine = queryLine.substring(1);
        }
        if (StringUtils.hasText(queryLine)) {
            log.info("Feign的请求行信息:{}", queryLine);
        }
        // 请求体信息
        byte[] body = requestTemplate.body();
        if (!ObjectUtils.isEmpty(body)) {
            String bodyStr = new String(body);
            log.info("Feign的请求体信息:{}", bodyStr);
        }
        log.info("============================ [end] feign api  ============================");
    }
}
