package com.github.hong.auth.security.service.impl;

import cn.hutool.core.util.IdUtil;
import com.github.hong.auth.config.AuthConfigProperties;
import com.github.hong.auth.context.constants.AuthCS;
import com.github.hong.auth.context.exception.RmsAuthenticationException;
import com.github.hong.auth.context.model.JwtUserDetails;
import com.github.hong.auth.context.model.JwtUserInfo;
import com.github.hong.auth.context.model.Token;
import com.github.hong.auth.context.utils.JwtUtil;
import com.github.hong.auth.security.service.IAuthService;
import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.exception.BizException;
import com.github.hong.core.exception.BizExceptionCast;
import com.github.hong.entity.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

/**
 * 认证服务接口实现
 *
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
@Component
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthConfigProperties authConfigProperties;

    public AuthService(AuthenticationManager authenticationManager,
                       AuthConfigProperties authConfigProperties) {
        this.authenticationManager = authenticationManager;
        this.authConfigProperties = authConfigProperties;
    }


    /**
     * 认证
     *
     * @param username 登录账号
     * @param password 登录密钥
     * @return TOKEN 信息
     */
    @Override
    public Token auth(String username, String password) {
        //1.生成spring-security usernamePassword类型对象
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        //2.手动认证
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(upToken);
        } catch (RmsAuthenticationException rmsExp) {
            throw rmsExp.getBizException() == null ? new BizException(rmsExp.getMessage()) : rmsExp.getBizException();
        } catch (BadCredentialsException e) {
            throw new BizException(ApiCodeEnum.LOGIN_EXP);
        } catch (AuthenticationException e) {
            log.error("AuthenticationException:", e);
            throw BizExceptionCast.cast(e);
        }
        //3.验证通过
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        //4.这边可以处理权限信息，RMS 项目暂时没有

        //5.将信息放置到Spring-security context中，供后续使用（当前线程）
        UsernamePasswordAuthenticationToken
                authenticationRest =
                new UsernamePasswordAuthenticationToken(jwtUserDetails, null, jwtUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationRest);

        //6.生成token
        User sysUser = jwtUserDetails.getSysUser();
        long accessTokenExpSec = authConfigProperties.getAccessTokenExpSec();
        String cacheKey = AuthCS.getCacheKeyToken(sysUser.getUserId(), IdUtil.fastUUID());
        TokenService.processTokenCache(jwtUserDetails, cacheKey, accessTokenExpSec);

        //7.构建返回值
        PrivateKey privateKey = authConfigProperties.getPrivateKey();
        JwtUserInfo jwtUserInfo = new JwtUserInfo();
        jwtUserInfo.setUserId(sysUser.getUserId());
        jwtUserInfo.setCreated(System.currentTimeMillis());
        jwtUserInfo.setCacheKey(cacheKey);
        return JwtUtil.generateUserToken(jwtUserInfo,
                privateKey,
                accessTokenExpSec);
    }
}
