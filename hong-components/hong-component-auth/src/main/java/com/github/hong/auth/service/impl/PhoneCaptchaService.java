package com.github.hong.auth.service.impl;

import com.github.hong.auth.context.enums.CaptchaReceiveMode;
import com.github.hong.auth.context.model.Captcha;
import com.github.hong.auth.service.ICaptchaService;
import com.github.hong.auth.service.dto.CaptchaDto;
import org.springframework.stereotype.Component;

/**
 * 手机验证码处理服务
 *
 * @author hanson
 * @since 2023/6/12
 */
@Component
public class PhoneCaptchaService implements ICaptchaService {


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
     * 校验参数
     *
     * @param captchaDto 验证码请求对象
     */
    @Override
    public void validCaptchaParam(CaptchaDto captchaDto) {

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
