package com.github.hong.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.hong.auth.context.enums.CaptchaReceiveMode;
import com.github.hong.auth.context.enums.CaptchaTypeEnum;
import com.github.hong.auth.context.enums.CaptchaUsageEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 验证码传输对象
 *
 * @author hanson
 * @since 2023/6/12
 */
@Data
@Accessors(chain = true)
public class CaptchaDto {

    /**
     * 验证码类型，不能为空。01-数字验证码 02-图形验证码
     */
    private String captchaType;

    /**
     * 验证码用途，不能为空。01-注册 02-登录 03-修改密码
     */
    private String captchaUsage;

    /**
     * 验证码接收方式，不能为空。01-手机 02-邮箱 03-网站
     */
    private String receiveMode;

    /**
     * 验证码接收人，注意与验证码接收方式配合，网站方式请任意输入一个值，服务端会返回一个经过BASE64的图片字符串
     */
    private String receiver;

    /**
     * 验证码位数
     */
    private int codeCount;

    /**
     * 图形验证码宽度
     */
    private int imgCaptchaWidth = 137;

    /**
     * 图形验证码高度
     */
    private int imgCaptchaHeight = 40;

    /**
     * 验证码有效期
     */
    private long captchaExpire = 60;

    /**
     * 获取验证码类型
     *
     * @return 验证码类型
     */
    public CaptchaTypeEnum getCaptchaTypeEnum() {
        return CaptchaTypeEnum.getEnumByCode(this.captchaType);
    }

    /**
     * 获取验证码用途
     *
     * @return 验证码用途
     */
    public CaptchaUsageEnum getCaptchaUsageEnum() {
        return CaptchaUsageEnum.getEnumByCode(this.captchaUsage);
    }

    /**
     * 获取验证码接收方式
     *
     * @return 验证码用途
     */
    @JsonIgnore
    public CaptchaReceiveMode getCaptchaReceiveMode() {
        return CaptchaReceiveMode.getEnumByCode(this.receiveMode);
    }


}
