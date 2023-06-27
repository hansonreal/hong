package com.github.hong.auth.security.service.impl;

import com.github.hong.auth.context.enums.IDTypeEnum;
import com.github.hong.auth.context.enums.UserStateEnum;
import com.github.hong.auth.context.exception.RmsAuthenticationException;
import com.github.hong.auth.context.model.JwtUserDetails;
import com.github.hong.auth.service.auth.ISysUserAuthService;
import com.github.hong.auth.service.auth.ISysUserService;
import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.utils.RegUtil;
import com.github.hong.entity.auth.SysUser;
import com.github.hong.entity.auth.SysUserAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 为认证提供数据来源
 *
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserAuthService sysUserAuthService;


    /**
     * @param username the username identifying the user whose data is required.
     * @return UserDetails
     * @throws UsernameNotFoundException 用户名不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        IDTypeEnum idTypeEnum = IDTypeEnum.EMAIL;
        boolean email = RegUtil.isEmail(username);
        if (!email) {
            idTypeEnum = IDTypeEnum.MOBILE;
        }
        //首先根据登录类型 + 用户名得到 信息
        SysUserAuth sysUserAuth = sysUserAuthService.selectByLogin(username, idTypeEnum);
        if (ObjectUtils.isEmpty(sysUserAuth)) {
            RmsAuthenticationException.throwExp(ApiCodeEnum.LOGIN_EXP);
        }
        String sysUserId = sysUserAuth.getSysUserId();
        SysUser sysUser = sysUserService.getById(sysUserId);
        if (ObjectUtils.isEmpty(sysUser)) {
            RmsAuthenticationException.throwExp(ApiCodeEnum.LOGIN_EXP);
        }
        String state = sysUser.getUserState();
        UserStateEnum stateEnum = UserStateEnum.getEnumByState(state);
        if (stateEnum != UserStateEnum.UNLOCKED) {
            RmsAuthenticationException.throwExp(ApiCodeEnum.LOGIN_STATE_EXP);
        }
        return new JwtUserDetails(sysUser, sysUserAuth.getCredential());
    }
}
