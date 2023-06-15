package com.github.hong.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hanson
 * @since 2023/5/19
 */
@Slf4j
public class RequestBodyInterceptor implements RequestInterceptor {
    /**
     * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("============================ [begin] feign api url ============================");
        /**
         * 使用 RequestContextHolder.getRequestAttributes() 静态方法获得Request。 (但仅限于Feign不开启Hystrix支持时。)
         * 当Feign开启Hystrix支持时，获取值为null
         * 原因在于，Hystrix的默认隔离策略是THREAD 。而 RequestContextHolder 源码中，使用了两个ThreadLocal 。
         * 解决方案一：调整隔离策略 将隔离策略设为SEMAPHORE即可
         * hystrix.command.default.execution.isolation.strategy: SEMAPHORE
         * 这样配置后，Feign可以正常工作。但该方案不是特别好。原因是Hystrix官方强烈建议使用THREAD作为隔离策略！
         *
         * 解决方案二：自定义并发策略
         * 既然Hystrix不太建议使用SEMAPHORE作为隔离策略，那么是否有其他方案呢？
         * 答案是自定义并发策略，目前，Spring Cloud Sleuth以及Spring Security都通过该方式传递 ThreadLocal 对象。
         * 编写自定义并发策略比较简单，只需编写一个类，让其继承HystrixConcurrencyStrategy ，并重写wrapCallable 方法即可。
         *
         */

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            log.info("Current request path: {}", request.getRequestURI());
            log.info("Feign request path: {}", requestTemplate.path());
            log.info("Feign request method: {}", requestTemplate.method());
            String queryLine = requestTemplate.queryLine();
            if (queryLine.startsWith("?")) {
                queryLine = queryLine.substring(1);
            }
            log.info("Feign request queryLine: \n{}", queryLine);
            byte[] body = requestTemplate.body();
            if (!ObjectUtils.isEmpty(body)) {
                String bodyStr = new String(body);
                log.info("Feign request body: \n{}", bodyStr);
            }
        }
        log.info("============================ [end] feign api url ============================");
    }
}
