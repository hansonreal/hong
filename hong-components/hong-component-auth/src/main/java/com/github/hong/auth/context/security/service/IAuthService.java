package com.github.hong.auth.context.security.service;


import com.github.hong.auth.context.model.Token;

/**
 * 认证服务接口
 *
 * @author hanson
 * @since 2023/6/5
 */
public interface IAuthService{
    /**
     * 认证
     * @param username 登录账号
     * @param password 登录密钥
     * @return TOKEN 信息
     */
    Token auth(String username, String password);
}
