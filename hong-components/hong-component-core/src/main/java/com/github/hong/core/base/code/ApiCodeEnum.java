package com.github.hong.core.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hanson
 * @since 2023/6/9
 */
@Getter
@AllArgsConstructor
public enum ApiCodeEnum implements IResultCode {
    SUCCESS("200", "操作成功"),
    LOGIN_EXP("401", "用户名或密码错误"),
    LOGIN_STATE_EXP("402", "用户状态异常"),
    JWT_TOKEN_EXPIRED_EXP("403", "会话超时，请重新登录"),
    JWT_GEN_TOKEN_FAIL("404", "生成token失败"),
    JWT_SIGNATURE_FAIL("405", "不合法的token，请认真比对 token 的签名"),
    JWT_ILLEGAL_ARGUMENT("406", "缺少token参数"),
    JWT_PARSER_TOKEN_FAIL("407", "解析token失败"),
    AUTH_SERVER_EXP("408", "认证服务发生异常，请联系管理员！"),
    TIMEOUT_EXP("409", "请求超时，请联系管理员检查服务状态！"),
    UNKNOWN_EXP("999", "未知异常，请联系管理员！"),
    ;
    final String rtnCode;
    final String rtnMsg;

    public static ApiCodeEnum getEnumByCode(String rtnCode) {
        for (ApiCodeEnum apiCodeEnum : ApiCodeEnum.values()) {
            if (apiCodeEnum.getRtnCode().equals(rtnCode)) {
                return apiCodeEnum;
            }
        }
        return UNKNOWN_EXP;
    }

    /**
     * @return 操作代码
     */
    @Override
    public String getRtnCode() {
        return rtnCode;
    }

    /**
     * @return 提示信息
     */
    @Override
    public String getRtnMsg() {
        return rtnMsg;
    }
}
