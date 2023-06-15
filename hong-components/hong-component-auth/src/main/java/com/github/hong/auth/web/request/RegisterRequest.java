package com.github.hong.auth.web.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.hong.auth.service.dto.RegisterDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册请求
 *
 * @author hanson
 * @since 2023/6/12
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(value = "注册参数", description = "注册参数对象")
public class RegisterRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名，不能为空", required = true, example = "张三")
    @Length(max = 64, message = "用户名长度不得超过64位")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号，如果填写了后续可以用来登录", example = "18911154321")
    @Length(max = 32, message = "手机号不得超过32位")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "电子邮箱，如果填写了后续可以用来登录", example = "zhangsan@outlook.com")
    @Length(max = 64, message = "手机号不得超过64位")
    private String mail;

    /**
     * 密码凭证
     */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "手机号，如果填写了后续可以用来登录", example = "12345678")
    //@Length(min = 8, max = 12, message = "密码长度最少8位最大12位")
    //@Max(value = 12, message = "密码长度最大12位")
    //@Min(value = 8, message = "密码长度最少8位")
    private String pwd;

    @NotNull(message = "验证码KEY不能为空")
    @ApiModelProperty(value = "验证码Key", example = "00")
    private String captchaKey;

    @NotNull(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", example = "653301")
    private String captchaCode;


    @JsonIgnore
    public RegisterDto createRegisterDto() {
        return new RegisterDto().setMail(this.mail)
                .setPhone(this.phone)
                .setPwd(this.pwd)
                .setName(this.name);
    }
}
