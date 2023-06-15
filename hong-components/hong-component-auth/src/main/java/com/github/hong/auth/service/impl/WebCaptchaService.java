package com.github.hong.auth.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import com.github.hong.auth.context.constants.AuthCS;
import com.github.hong.auth.context.enums.CaptchaReceiveMode;
import com.github.hong.auth.context.model.Captcha;
import com.github.hong.auth.service.ICaptchaService;
import com.github.hong.auth.service.dto.CaptchaDto;
import com.github.hong.core.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WEB 验证码
 *
 * @author hanson
 * @since 2023/6/12
 */
@Slf4j
@Component
public class WebCaptchaService implements ICaptchaService {


    /**
     * 验证码接收方式
     *
     * @return CaptchaReceiveMode
     */
    @Override
    public CaptchaReceiveMode captchaReceive() {
        return CaptchaReceiveMode.WEB;
    }

    /**
     * 子类获取验证码方法
     *
     * @param captchaDto 验证码请求对象
     * @return 验证码对象
     */
    @Override
    public Captcha doObtainCaptcha(CaptchaDto captchaDto) {
        int imgCaptchaWidth = captchaDto.getImgCaptchaWidth();
        int imgCaptchaHeight = captchaDto.getImgCaptchaHeight();
        int codeCount = captchaDto.getCodeCount();
        long captchaExpire = captchaDto.getCaptchaExpire();
        //定义图形验证码的长和宽 以及多少位验证码
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(imgCaptchaWidth, imgCaptchaHeight, codeCount, 80);
        lineCaptcha.createCode(); //生成code
        String lineCaptchaCode = lineCaptcha.getCode();
        log.info("图形验证码值:{}", lineCaptchaCode);
        //redis
        String captchaToken = UUID.fastUUID().toString();
        String cacheKeyCaptcha = AuthCS.getCacheKeyCaptcha(captchaToken);
        RedisService.setString(cacheKeyCaptcha, lineCaptchaCode, captchaExpire);//图片验证码缓存时间: 1分钟
        Captcha captcha = new Captcha();
        captcha.setVerCode(lineCaptchaCode);
        captcha.setExpire(captchaExpire);
        captcha.setVerCodeKey(captchaToken);
        captcha.setImageBase64Data(lineCaptcha.getImageBase64Data());
        return captcha;
    }
}
