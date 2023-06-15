package com.github.hong.auth.context;

import com.github.hong.auth.context.enums.CaptchaReceiveMode;
import com.github.hong.auth.service.ICaptchaService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 验证码上下文对象
 *
 * @author hanson
 * @since 2023/6/12
 */
@Component
public class CaptchaContext {

    private final List<ICaptchaService> captchaServiceList;

    public CaptchaContext(List<ICaptchaService> captchaServiceList) {
        this.captchaServiceList = captchaServiceList;
    }

    public ICaptchaService chooseCaptchaService(CaptchaReceiveMode receiveMode) {
        Optional<ICaptchaService> captchaServiceOptional =
                captchaServiceList.stream().filter(e -> e.captchaReceive() == receiveMode).findAny();
        return captchaServiceOptional.orElseThrow(() -> new IllegalArgumentException("暂不支持该接收类型的验证码方式"));

    }
}
