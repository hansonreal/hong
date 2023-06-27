package com.github.hong.auth.web.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.hong.auth.context.enums.CaptchaReceiveMode;
import com.github.hong.auth.context.enums.CaptchaTypeEnum;
import com.github.hong.auth.context.enums.CaptchaUsageEnum;
import com.github.hong.auth.service.captcha.dto.CaptchaDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author hanson
 * @since 2023/6/7
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(value = "验证码参数", description = "获取验证码参数对象")
public class CaptchaRequest {

    @NotBlank(message = "验证码类型不能为空")
    @ApiModelProperty(value = "验证码类型，不能为空。01-数字验证码 02-图形验证码（主要用于网页上的注册与登录）", required = true, example = "02")
    @Length(max = 32, message = "验证码类型长度不得超过4位")
    private String captchaType;

    @NotBlank(message = "验证码用途不能为空")
    @ApiModelProperty(value = "验证码用途，不能为空。01-注册 02-登录 03-修改密码", required = true, example = "01")
    @Length(max = 32, message = "验证码用途长度不得超过4位")
    private String captchaUsage;

    @NotBlank(message = "验证码接收方式不能为空")
    @ApiModelProperty(value = "验证码接收方式，不能为空。01-手机 02-邮箱 03-网站", required = true, example = "03")
    @Length(max = 32, message = "验证码接收方式长度不得超过4位")
    private String receiveMode;

    @NotBlank(message = "验证码接收人不能为空")
    @ApiModelProperty(value = "验证码接收人，注意与验证码接收方式配合，网站方式请任意输入一个值，服务端会返回一个经过BASE64的图片字符串", required = true, example = "")
    @Length(max = 256, message = "验证码接收方式长度不得超过4位")
    private String receiver;

    @NotBlank(message = "验证码位数不能为空")
    @ApiModelProperty(value = "生成多少位数的验证码，取值范围位4-6，默认值为4位，即生成四位数验证码", required = true, example = "4")
    @Min(value = 4, message = "验证码位数，最低为4位")
    @Max(value = 6, message = "验证码位数，最大为6位")
    private String codeCount;


    @ApiModelProperty(value = "图形验证码宽度，默认值137，仅在验证码接收方式为03（网站）时有效", example = "137")
    private String imgCaptchaWidth = "137";

    @ApiModelProperty(value = "图形验证码高度，默认值40，仅在验证码接收方式为03（网站）时有效", example = "40")
    private String imgCaptchaHeight = "40";


    /**
     * 获取验证码类型
     *
     * @return 验证码类型
     */
    @JsonIgnore
    public CaptchaTypeEnum getCaptchaTypeEnum() {
        return CaptchaTypeEnum.getEnumByCode(this.captchaType);
    }

    /**
     * 获取验证码用途
     *
     * @return 验证码用途
     */
    @JsonIgnore
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

    /**
     * 构建 CaptchaDto 对象
     *
     * @return CaptchaDto
     */
    @JsonIgnore
    public CaptchaDto createCaptchaDto() {
        return new CaptchaDto().setCaptchaUsage(captchaUsage)
                .setCaptchaType(captchaType)
                .setImgCaptchaWidth(Integer.parseInt(imgCaptchaWidth))
                .setImgCaptchaHeight(Integer.parseInt(imgCaptchaHeight))
                .setReceiver(receiver)
                .setCodeCount(Integer.parseInt(codeCount))
                .setReceiveMode(receiveMode);
    }

}
