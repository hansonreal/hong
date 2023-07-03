package com.github.hong.feign.context.handler;

import com.github.hong.common.handler.DefaultGlobalExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author hanson
 * @since 2023/7/3
 */
@ResponseBody
@ControllerAdvice
public class FeignExceptionHandler extends DefaultGlobalExceptionHandler {
}
