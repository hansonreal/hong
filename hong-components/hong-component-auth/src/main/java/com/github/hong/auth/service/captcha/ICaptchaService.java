package com.github.hong.auth.service.captcha;


import com.github.hong.auth.context.enums.CaptchaReceiveMode;
import com.github.hong.auth.context.model.Captcha;
import com.github.hong.auth.service.captcha.dto.CaptchaDto;

/**
 * 验证码服务接口
 *
 * @author hanson
 * @since 2023/6/12
 */
public interface ICaptchaService {


    /**
     * 验证码接收方式
     *
     * @return CaptchaReceiveMode
     */
    CaptchaReceiveMode captchaReceive();


    /**
     * 获取验证码
     *
     * @param captchaDto 验证码请求对象
     * @return 验证码对象
     */
    default Captcha obtainCaptcha(CaptchaDto captchaDto) {
        validCaptchaParam(captchaDto);
        return doObtainCaptcha(captchaDto);
    }

    /**
     * 校验参数
     *
     * @param captchaDto 验证码请求对象
     */
    default void validCaptchaParam(CaptchaDto captchaDto) {

    }

    /**
     * 子类获取验证码方法
     *
     * @param captchaDto 验证码请求对象
     * @return 验证码对象
     */
    Captcha doObtainCaptcha(CaptchaDto captchaDto);
}
