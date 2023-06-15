package com.github.hong.common.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.base.result.ErrorR;
import com.github.hong.core.exception.BizException;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.TypeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理，JSON处理
 */
@Slf4j
//@RestControllerAdvice
public abstract class DefaultGlobalExceptionHandler {
    //定义map的builder对象，去构建ImmutableMap
    public static ImmutableMap.Builder<Class<? extends Throwable>, ApiCodeEnum> builder = ImmutableMap.builder();
    //定义map，配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, ApiCodeEnum> EXCEPTIONS;

    static {
        //定义异常类型所对应的错误代码
        builder.put(HttpMessageNotReadableException.class, ApiCodeEnum.JSON_SERIALIZABLE_OCCURRED_EXP);
        builder.put(MethodArgumentNotValidException.class, ApiCodeEnum.VALID_METHOD_ARGS_OCCURRED_EXP);
        builder.put(InvalidFormatException.class, ApiCodeEnum.VALID_ARGS_FORMAT_OCCURRED_EXP);
        builder.put(HttpRequestMethodNotSupportedException.class, ApiCodeEnum.REQUEST_METHOD_NOT_SUPPORTED_EXP);
        builder.put(SQLIntegrityConstraintViolationException.class, ApiCodeEnum.SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXP);
        builder.put(TypeException.class, ApiCodeEnum.INVALID_COLUMN_TYPE_EXP);

    }


    @ExceptionHandler(BizException.class)
    public ErrorR handleBizException(BizException e) {
        log.error("进入统一异常处理（BizException）异常信息:" + e.getMsg());
        e.printStackTrace();
        return ErrorR.exp(e);
    }

    /**
     * 异常处理
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorR handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        ErrorR fail = ErrorR.fail(ApiCodeEnum.INVALID_PARAM);
        fail.setRtnMsg(message);
        return fail;
    }

    /**
     * 异常处理
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(Exception.class)
    public ErrorR handleException(Exception e) {
        log.error("catch exception:", e);
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();//EXCEPTIONS构建成功
        }
        //从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应500异常
        ApiCodeEnum codeEnum = EXCEPTIONS.get(e.getClass());
        // 构建返回结果
        ErrorR result = new ErrorR();
        if (codeEnum != null) {
            result.setRtnCode(codeEnum.getRtnCode());
            result.setRtnMsg(codeEnum.getRtnMsg() + e.getMessage());
        } else {
            ApiCodeEnum serverErrorCode = ApiCodeEnum.UNKNOWN_EXP;
            result.setRtnMsg(serverErrorCode.getRtnMsg());
            result.setRtnCode(serverErrorCode.getRtnCode());
        }
        return result;
    }

}
