package com.github.hong.core.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hanson
 * @since 2023/5/11
 */
@Getter
@AllArgsConstructor
public enum ApiCodeEnum implements IResultCode {

    SUCCESS("200", "操作成功"),

    // =============== 认证服务异常码 ===============
    LOGIN_EXP("401", "用户名或密码错误"),
    LOGIN_STATE_EXP("402", "用户状态异常"),
    JWT_TOKEN_EXPIRED_EXP("403", "会话超时，请重新登录"),

    JWT_GEN_TOKEN_FAIL("404", "生成token失败"),
    JWT_SIGNATURE_FAIL("405", "不合法的token，请认真比对token的签名"),
    JWT_ILLEGAL_ARGUMENT("406", "缺少token参数"),
    JWT_PARSER_TOKEN_FAIL("407", "解析token失败"),

    CAPTCHA_EXPIRE_EXP("408", "验证码已经失效"),
    CAPTCHA_VALID_FAILED_EXP("409", "验证码错误"),
    CAPTCHA_INFO_NOT_FOUND_EXP("410", "验证码信息不能为空"),
    REGISTERED_ACCOUNT_CAN_NOT_BE_EMPTY_EXP("411", "注册账号不可为空"),

    MOBILE_NUMBER_FORMAT_EXP("412", "手机号{0}格式错误"),
    EMAIL_FORMAT_EXP("413", "邮箱{0}格式错误"),
    EMAIL_REPEATED_EXP("414", "邮箱地址{0}已被使用，请更换"),

    MOBILE_REPEATED_EXP("415", "手机号{0}已被使用，请更换"),

    NAME_REPEATED_EXP("416", "用户名{0}已被使用，请更换"),

    NAME_NOT_BE_EMPTY_EXP("417", "用户名不能为空"),

    PWD_NOT_BE_EMPTY_EXP("418", "密码不能为空"),

    REGISTER_FAILED_EXP("419", "注册失败，请联系管理员！"),

    AUTH_SERVER_EXP("499", "认证服务发生异常，请联系管理员！"),
    INVALID_PARAM("500", "非法参数"),
    VALID_METHOD_ARGS_OCCURRED_EXP("501", "校验方法参数发生异常"),
    JSON_SERIALIZABLE_OCCURRED_EXP("502", "JSON序列化发生异常"),
    VALID_ARGS_FORMAT_OCCURRED_EXP("503", "请求参数格式不正确"),
    REQUEST_METHOD_NOT_SUPPORTED_EXP("504", "不支持的请求方式"),
    SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXP("505", "违反唯一约束条件"),
    INVALID_COLUMN_TYPE_EXP("506", "无效的列类型"),

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
