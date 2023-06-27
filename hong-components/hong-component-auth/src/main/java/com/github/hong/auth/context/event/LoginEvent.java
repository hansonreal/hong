package com.github.hong.auth.context.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 登录事件
 *
 * @author hanson
 * @since 2023/6/17
 */
@Getter
@Setter
public class LoginEvent extends ApplicationEvent {

    private String userId;


    public LoginEvent(String userId) {
        super(userId);
        this.userId = userId;
    }
}
