package com.github.hong.auth.context.handler;

import com.github.hong.common.handler.DefaultGlobalExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hanson
 * @since 2023/6/5
 */
@ResponseBody
@ControllerAdvice
public class AuthExceptionHandler extends DefaultGlobalExceptionHandler {
}
