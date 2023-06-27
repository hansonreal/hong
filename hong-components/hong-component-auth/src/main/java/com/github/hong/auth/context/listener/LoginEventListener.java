package com.github.hong.auth.context.listener;

import com.github.hong.auth.context.listener.event.LoginEvent;
import com.github.hong.auth.service.auth.ISysUserService;
import com.github.hong.entity.auth.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 登录事件监听器
 *
 * @author hanson
 * @since 2023/6/17
 */
@Slf4j
@Component
public class LoginEventListener implements ApplicationListener<LoginEvent> {

    private final ISysUserService sysUserService;

    public LoginEventListener(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public void onApplicationEvent(LoginEvent loginEvent) {
        String userId = loginEvent.getUserId();
        SysUser byId = sysUserService.getById(userId);
        if (!ObjectUtils.isEmpty(byId)) {
            log.info(" 用户【{}】登陆了系统", byId.getLoginName());
        }
    }
}
