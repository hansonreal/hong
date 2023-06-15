package com.github.hong.auth.context.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码对象
 *
 * @author hanson
 * @since 2023/6/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Captcha对象", description = "验证码对象")
public class Captcha {

    @ApiModelProperty(value = "验证码KEY")
    private String verCodeKey;

    @ApiModelProperty(value = "验证码")
    private String verCode;

    @ApiModelProperty(value = "有效期，单位秒")
    private Long expire;

    @ApiModelProperty(value = "图形验证码")
    private String imageBase64Data;
}
