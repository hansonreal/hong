package com.github.hong.common.aspect;

import com.github.hong.core.annotation.RetryOnFailure;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Aspect
@Order(1)//优先于Transactional注解
public class RetryAspect {
    // 最大重试次数
    public static final AtomicInteger MAX_RETRY_TIMES = new AtomicInteger(10);//max retry times

    @Around("@annotation(retryOnFailure)")
    public Object doConcurrentOperation(ProceedingJoinPoint pjp,
                                        RetryOnFailure retryOnFailure) throws Throwable {
        int attempts = 0;
        do {
            attempts++;
            try {
                pjp.proceed();
            } catch (Exception e) {
//                if (e instanceof ObjectOptimisticLockingFailureException ||  e instanceof StaleObjectStateException) {
//                    log.info("retrying....times:{}", attempts);
//                    if (attempts > MAX_RETRY_TIMES) {
//                        log.info("retry excceed the max times..");
//                        throw e;
//                    }
//                }

            }
        } while (attempts < MAX_RETRY_TIMES.get());
        return null;
    }

}
