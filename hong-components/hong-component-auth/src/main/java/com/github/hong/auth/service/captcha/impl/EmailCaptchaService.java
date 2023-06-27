package com.github.hong.auth.service.captcha.impl;

import com.github.hong.auth.context.enums.CaptchaReceiveMode;
import com.github.hong.auth.context.model.Captcha;
import com.github.hong.auth.service.captcha.ICaptchaService;
import com.github.hong.auth.service.captcha.dto.CaptchaDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 邮箱验证码服务
 * @author hanson
 * @since 2023/6/20
 */
@Slf4j
@Component
public class EmailCaptchaService implements ICaptchaService {
    /**
     * 验证码接收方式
     *
     * @return CaptchaReceiveMode
     */
    @Override
    public CaptchaReceiveMode captchaReceive() {
        return null;
    }

    /**
     * 子类获取验证码方法
     *
     * @param captchaDto 验证码请求对象
     * @return 验证码对象
     */
    @Override
    public Captcha doObtainCaptcha(CaptchaDto captchaDto) {
        return null;
    }
}
