package com.github.hong.auth.service.auth.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 注册请求传输对象
 *
 * @author hanson
 * @since 2023/6/12
 */
@Data
@Accessors(chain = true)
public class RegisterDto {

    /**
     * 用户名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 密码
     */
    private String pwd;


}
