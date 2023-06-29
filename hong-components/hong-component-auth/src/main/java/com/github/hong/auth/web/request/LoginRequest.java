package com.github.hong.auth.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求对象
 *
 * @author hanson
 * @since 2023/6/5
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(value = "登录参数", description = "登录参数对象")
public class LoginRequest {

    @NotBlank(message = "登录名不能为空")
    @ApiModelProperty(value = "登录名，不能为空", required = true, example = "18039876985")
    @Length(max = 32, message = "登录名长度不得超过32位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码，需要先加密，不能为空", required = true, example = "00")
    private String password;

    @ApiModelProperty(value = "验证码Key", example = "00")
    private String verCodeKey;

    @ApiModelProperty(value = "验证码值", example = "653301")
    private String verCodeVal;
}