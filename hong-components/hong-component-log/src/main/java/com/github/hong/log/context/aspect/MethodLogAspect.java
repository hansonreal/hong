package com.github.hong.log.context.aspect;

import cn.hutool.core.date.DateTime;
import com.github.hong.common.security.JwtUserDetails;
import com.github.hong.core.annotation.MethodLog;
import com.github.hong.core.exception.BizException;
import com.github.hong.core.utils.IPUtil;
import com.github.hong.core.utils.JsonUtil;
import com.github.hong.core.utils.SpringBeanUtil;
import com.github.hong.entity.auth.SysUser;
import com.github.hong.entity.log.SysLog;
import com.github.hong.log.context.event.MethodLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @className: MethodLogAspect
 * @description: 切面处理类
 * @author: hanson
 */
@Slf4j
@Aspect
@Component
public class MethodLogAspect {

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
        long durationTime = stopWatch.getTotalTimeMillis();
        //保存日志
        saveSysLog(point, durationTime, result);

        return result;
    }


    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void afterThrowing(JoinPoint point, Exception e) throws Throwable {
        log.error("调用{}的{}方法，发生异常:" + e, point.getTarget(), point.getSignature().getName());
        String result = e instanceof BizException ? e.getMessage() : "请求异常";
        // 保存异常日志
        saveSysLog(point, 0L, result);
    }


    private void saveSysLog(JoinPoint joinPoint, long durationTime, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MethodLog methodLog = method.getAnnotation(MethodLog.class);
        //注解上的描述,操作日志内容
        if (ObjectUtils.isEmpty(methodLog)) {
            return;
        }
        SysLog sysLog = new SysLog();
        // 创建时间
        DateTime now = DateTime.now();
        sysLog.setCreatedAt(now.toJdkDate());

        // 方法描述
        String methodDesc = methodLog.value();
        sysLog.setMethodDesc(methodDesc);

        // 用户名&用户标识
        JwtUserDetails jwtUserDetails = JwtUserDetails.getCurrentUserDetails();
        if (!ObjectUtils.isEmpty(jwtUserDetails)) {
            SysUser sysUser = jwtUserDetails.getSysUser();
            if (!ObjectUtils.isEmpty(sysUser)) {
                String sysUserId = sysUser.getSysUserId();
                sysLog.setSysUserId(sysUserId);
                sysLog.setLoginName(sysUser.getLoginName());
            }
        }
        //包名&方法名
        Object aThis = joinPoint.getThis();
        Class<?> aClass = aThis.getClass();
        Package aPackage = aClass.getPackage();
        String packageName = aPackage.getName();
        if (packageName.contains("$$EnhancerByCGLIB$$") || packageName.contains("$$EnhancerBySpringCGLIB$$")) { // 如果是CGLIB动态生成的类
            packageName = packageName.substring(0, packageName.indexOf("$$"));
        }
        String methodName = signature.getName();
        methodName = packageName + "#" + methodName;
        sysLog.setMethodName(packageName + "#" + methodName);

        // 请求IP&请求URL
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (!ObjectUtils.isEmpty(request)) {
            // IP地址
            String ipAddr = IPUtil.getIpAddr(request);
            sysLog.setUserIp(ipAddr);
            // 请求URL
            sysLog.setReqUrl(request.getRequestURL().toString());
        }
        // 请求参数
        boolean recordReqParam = methodLog.recordReqParam();
        if (recordReqParam) {
            Object[] args = joinPoint.getArgs();
            sysLog.setOptReqInfo(JsonUtil.serialize(args));
        }
        // 响应参数
        boolean recordRespParam = methodLog.recordRespParam();
        if (recordRespParam) {
            sysLog.setOptResInfo(JsonUtil.serialize(result));
        }

        // 方式执行损耗时长
        sysLog.setDurationTime(String.valueOf(durationTime));

        // 发布事件通知
        ApplicationContext applicationContext = SpringBeanUtil.getApplicationContext();
        applicationContext.publishEvent(new MethodLogEvent(sysLog));

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
