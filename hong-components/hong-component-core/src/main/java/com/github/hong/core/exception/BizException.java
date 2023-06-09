package com.github.hong.core.exception;

import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.base.code.IResultCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 业务异常对象
 * @author hanson
 * @since 2023/6/9
 */
@Getter
@Setter
public class BizException extends RuntimeException {
    private String code;

    private String msg;

    public BizException(String msg) {
        super(msg);
    }

    public BizException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public BizException(String msg, String code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public BizException(String msg, String code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public BizException(IResultCode codeEnum) {
        super(codeEnum.getRtnMsg());
        this.msg = codeEnum.getRtnMsg();
        this.code = codeEnum.getRtnCode();
    }

    public BizException(IResultCode codeEnum, Throwable e) {
        super(codeEnum.getRtnMsg(), e);
        this.msg = codeEnum.getRtnMsg();
        this.code = codeEnum.getRtnCode();
    }

    /**
     * 携有占位符 异常处理
     *
     * @param codeEnum 异常码枚举类
     * @param params   可变参数
     */
    public BizException(IResultCode codeEnum, Object... params) {
        this.code = codeEnum.getRtnCode();
        String rtnMsg = codeEnum.getRtnMsg();
        if (StringUtils.hasText(rtnMsg) && (params != null && params.length > 0)) {
                rtnMsg = MessageFormat.format(rtnMsg, params);

        }
        this.msg = rtnMsg;
    }

    /**
     * 携有占位符 异常处理,不建议使用避免导致异常码错乱
     *
     * @param msg    异常信息
     * @param code   异常码
     * @param params 可变参数
     */
    public BizException(String msg, String code, Object... params) {
        this.code = code;
        ApiCodeEnum enumByCode = ApiCodeEnum.getEnumByCode(code);
        if (null == enumByCode) {
            if (StringUtils.hasText(msg) && (params != null && params.length > 0)) {
                msg = MessageFormat.format(msg, Arrays.toString(params));
            }
        } else {
            msg = enumByCode.getRtnMsg();
            if (StringUtils.hasText(msg) && (params != null && params.length > 0)) {
                msg = MessageFormat.format(msg, Arrays.toString(params));
            }
        }
        this.msg = msg;
    }

    public static BizException throwExp(IResultCode codeEnum) {
        throw new BizException(codeEnum);
    }

    public static BizException throwExp(IResultCode codeEnum, Object... params) {
        throw new BizException(codeEnum, params);
    }

    public static BizException throwExp(String msg, String code) {
        throw new BizException(msg, code);
    }

    public static BizException throwExp(String msg, String code, Object... params) {
        throw new BizException(msg, code, params);
    }
}