package com.github.hong.common.aspect;

import com.github.hong.core.annotation.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Order(1)
public class RedissonLockAspect {

    @Resource
    private Redisson redisson;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
        //方法内的所有参数
        Object[] params = joinPoint.getArgs();
        int lockIndex = redissonLock.lockIndex();
        //取得方法名
        String key = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        //-1代表锁整个方法，而非具体锁哪条数据
        if (lockIndex != -1) {
            key += params[lockIndex];
        }
        log.info("Redisson分布式锁名称:【{}】", key);
        //多久会自动释放，默认10秒
        int leaseTime = redissonLock.leaseTime();
        log.debug("{}秒后Redisson分布式锁会自动释放，默认10秒", leaseTime);
        int waitTime = 5;
        RLock rLock = redisson.getLock(key);
        boolean res = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        Object obj;
        if (res) {
            log.info("▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔取到锁▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔");
            obj = joinPoint.proceed();
            if (rLock.isHeldByCurrentThread()) {// 查询当前线程是否持有此锁定
                rLock.unlock();// 此处可能出现 IllegalMonitorStateException: attempt to unlock lock，原因是超时锁被自动释放，此时再去手动释放就会抛出异常
            }
            log.info("▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁释放锁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁");
        } else {
            log.warn("----------没有获得锁----------抛出异常不让目标方法执行");
            throw new RuntimeException("没有获得锁");
        }
        return obj;
    }
}
