package com.github.hong.auth.context.exception;

import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.exception.BizException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * RMS 认证异常
 *
 * @author hanson
 * @since 2023/6/6
 */
@Getter
@Setter
public class RmsAuthenticationException extends InternalAuthenticationServiceException {

    private BizException bizException;

    public RmsAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RmsAuthenticationException(String message) {
        super(message);
    }

    public static RmsAuthenticationException build(String msg) {
        return build(new BizException(msg));
    }


    public static RmsAuthenticationException build(BizException ex) {
        RmsAuthenticationException result = new RmsAuthenticationException(ex.getMessage());
        result.setBizException(ex);
        return result;
    }

    public static RmsAuthenticationException build(ApiCodeEnum codeEnum) {
        BizException bizExp = new BizException(codeEnum);
        RmsAuthenticationException result = new RmsAuthenticationException(bizExp.getMessage());
        result.setBizException(bizExp);
        return result;
    }
    public static RmsAuthenticationException throwExp(ApiCodeEnum codeEnum) {
        throw RmsAuthenticationException.build(codeEnum);
    }


}
