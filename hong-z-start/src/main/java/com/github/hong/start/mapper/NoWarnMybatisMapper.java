package com.github.hong.start.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 解决控制台 如下警告
 * 2023-06-05 16:52:13.881  WARN 25652 --- [           main] o.m.s.mapper.ClassPathMapperScanner      :
 * No MyBatis mapper was found in '[ls.ov.rms.start]' package. Please check your configuration.
 *
 * @author hanson
 * @since 2023/6/5
 */
@Mapper
public interface NoWarnMybatisMapper {
}
