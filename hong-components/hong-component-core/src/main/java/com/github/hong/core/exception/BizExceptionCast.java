package com.github.hong.core.exception;


import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.base.code.IResultCode;

/**
 * 异常转换
 */
public class BizExceptionCast {
    public static BizException cast(IResultCode code) {
        return new BizException(code);
    }

    public static BizException cast(Exception exp) {

        if (exp instanceof BizException) {
            return (BizException) exp;
        } else {
            return new BizException(ApiCodeEnum.UNKNOWN_EXP);
        }
    }

}
