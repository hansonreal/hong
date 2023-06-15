package com.github.hong.auth.context.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hanson
 * @since 2023/6/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "TOKEN对象", description = "TOKEN对象")
public class Token implements Serializable {
    private static final long serialVersionUID = -8482946147572784305L;
    /**
     * token
     */
    @ApiModelProperty(value = "accessToken")
    private String accessToken;
    /**
     * 有效时间：单位：秒
     */
    @ApiModelProperty(value = "有效期，单位秒")
    private Long expire;

}
