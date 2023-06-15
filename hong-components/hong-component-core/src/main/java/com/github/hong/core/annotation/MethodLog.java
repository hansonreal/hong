package com.github.hong.core.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {
    /**
     * 描述
     *
     * @return {String}
     */
    String value();

    /**
     * 记录执行参数
     *
     * @return true 记录（默认值） | false 不记录
     */
    boolean recordReqParam() default true;

    /**
     * 记录返回参数
     *
     * @return true 记录（默认值） | false 不记录
     */
    boolean recordRespParam() default true;
}

