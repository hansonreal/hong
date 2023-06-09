package com.github.hong.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @className: AnonAccess
 * @description: 用于配置可匿名访问的方法
 * 实现原理：拦截所有标注 @AnonAccess注解的method handler
 * 获取其配置的 映射路径 转换成数组
 * 配置到@see WebSecurityConfiguration#configure(HttpSecurity)方法中
 * @author: hanson
 * @version: V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonAccess {
}
