package com.github.hong.log.context.aspect;

import com.github.hong.core.annotation.MethodLog;
import com.github.hong.entity.log.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @className: MethodLogAspect
 * @description: 切面处理类
 * @author: hanson
 */
@Slf4j
@Aspect
@Component
public class MethodLogAspect {

    /**
     * 异步处理线程池
     */
    private final static ScheduledExecutorService scheduledThreadPool
            = Executors.newScheduledThreadPool(10);

    private static final ThreadLocal<SysLog> THREAD_LOCAL = new ThreadLocal<>();


    private SysLog get() {
        SysLog sysLog = THREAD_LOCAL.get();
        if (sysLog == null) {
            return new SysLog();
        }
        return sysLog;
    }



    @Pointcut("@annotation(com.github.hong.core.annotation.MethodLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //执行方法
        Object result = point.proceed();
        stopWatch.stop();
        //执行时长(毫秒)
        long time = stopWatch.getTotalTimeMillis();
        log.info("耗时:{}毫秒", time);
        //保存日志
        scheduledThreadPool.execute(() -> {
            saveSysLog(point, time, result);
        });
        return result;
    }


    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void afterThrowing(JoinPoint point, Exception e) throws Throwable {
        log.error("调用{}的{}方法，发生异常:" + e, point.getTarget(), point.getSignature().getName());
    }


    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, Object obj) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MethodLog methodLog = method.getAnnotation(MethodLog.class);
        //注解上的描述,操作日志内容
//        if (autoLog != null) {
//
//        }
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("Class:{},Method:{}", className, methodName);


    }


    /**
     * 获取请求参数
     *
     * @param request   请求
     * @param joinPoint 连接点
     * @return String
     */
    private String getRequestParams(HttpServletRequest request, JoinPoint joinPoint) {
        return null;
    }

}
