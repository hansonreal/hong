package com.github.hong.auth.web.ctrl;

import com.github.hong.auth.config.AuthConfigProperties;
import com.github.hong.auth.config.CaptchaConfigProperties;
import com.github.hong.auth.context.CaptchaContext;
import com.github.hong.auth.context.constants.AuthCS;
import com.github.hong.auth.context.enums.CaptchaReceiveMode;
import com.github.hong.auth.context.model.Captcha;
import com.github.hong.auth.context.model.Token;
import com.github.hong.auth.security.service.IAuthService;
import com.github.hong.auth.service.ICaptchaService;
import com.github.hong.auth.service.IUserService;
import com.github.hong.auth.service.dto.CaptchaDto;
import com.github.hong.auth.service.dto.RegisterDto;
import com.github.hong.auth.web.request.CaptchaRequest;
import com.github.hong.auth.web.request.LoginRequest;
import com.github.hong.auth.web.request.RegisterRequest;
import com.github.hong.core.annotation.MethodLog;
import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.base.result.DataR;
import com.github.hong.core.cache.RedisService;
import com.github.hong.core.exception.BizException;
import com.github.hong.core.utils.RsaUtil;
import com.github.hong.core.utils.SpringBeanUtil;
import com.github.hong.core.utils.StreamUtil;
import com.github.hong.entity.auth.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(value = "认证接口", tags = {"认证管理"})
public class AuthController {

    private final IAuthService authService;

    private final IUserService userService;

    private final AuthConfigProperties authConfigProperties;


    private final CaptchaConfigProperties captchaConfigProperties;

    public AuthController(IAuthService authService,
                          IUserService userService,
                          AuthConfigProperties authConfigProperties,
                          CaptchaConfigProperties captchaConfigProperties) {
        this.authService = authService;
        this.userService = userService;
        this.authConfigProperties = authConfigProperties;
        this.captchaConfigProperties = captchaConfigProperties;
    }

    @PostMapping("/login")
    @MethodLog(value = "登录认证")
    @ApiOperation(value = "认证接口", notes = "用于获取Access Token")
    public DataR<Token> login(
            @ApiParam(name = "loginRequest", value = "登录请求对象", required = true)
            @Validated @RequestBody LoginRequest loginRequest) throws BizException {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        // 是否开启验证码
        boolean enabledCaptcha = captchaConfigProperties.isEnabled();
        String captchaKey = null;
        if (enabledCaptcha) {
            captchaKey = loginRequest.getCaptchaKey();
            // 校验验证码信息
            String captchaCode = loginRequest.getCaptchaCode();
            validaCaptchaInfo(captchaKey, captchaCode);
        }
        // 处理认证
        Token accessToken = authService.auth(username, password);
        // 删除图形验证码缓存数据
        if (StringUtils.hasText(captchaKey)) {
            RedisService.del(AuthCS.getCacheKeyCaptcha(captchaKey));
        }
        return DataR.success(accessToken);
    }

    @PostMapping("/captcha")
    @MethodLog(value = "获取验证码")
    @ApiOperation(value = "验证码接口", notes = "用于获取验证码")
    public DataR<Captcha> captcha(
            @ApiParam(name = "captchaRequest", value = "验证码请求对象", required = true)
            @Validated @RequestBody CaptchaRequest captchaRequest) {
        CaptchaContext captchaContext = SpringBeanUtil.getBean(CaptchaContext.class);
        CaptchaDto captchaDto = captchaRequest.createCaptchaDto();
        captchaDto.setCaptchaExpire(captchaConfigProperties.getExpire());
        CaptchaReceiveMode receiveMode = captchaDto.getCaptchaReceiveMode();
        ICaptchaService captchaService = captchaContext.chooseCaptchaService(receiveMode);
        Captcha captcha = captchaService.obtainCaptcha(captchaDto);
        return DataR.success(captcha);
    }

    @GetMapping("/pek")
    @MethodLog(value = "获取公钥")
    @ApiOperation(value = "获取公钥", notes = "用于获取公钥，便于客户端加密数据")
    public DataR<String> obtainPublicKey() {
        PublicKey publicKey = authConfigProperties.getPublicKey();
        String publicKeyStr = RsaUtil.getPublicKeyStr(publicKey);
        return DataR.success(publicKeyStr);
    }

    @PostMapping("/encrypt")
    @MethodLog(value = "数据加密")
    @ApiOperation(value = "数据加密", notes = "接口调试时的加密方法，生产场景数据加密需要再客户端完成")
    public DataR<String> encrypt(HttpServletRequest request) {

        String encrypt = null;
        try {
            String unencryptedData = StreamUtil.getString(request.getInputStream());
            encrypt = RsaUtil.encrypt(unencryptedData, authConfigProperties.getPublicKey());
        } catch (Exception e) {
            BizException.throwExp(ApiCodeEnum.UNKNOWN_EXP, "数据加密发生异常");
        }
        return DataR.success(encrypt);
    }

    @PostMapping("/register")
    @MethodLog(value = "用户注册")
    @ApiOperation(value = "用户注册", notes = "系统用户注册")
    public DataR<Map<String, String>> register(
            @ApiParam(name = "request", value = "注册请求对象", required = true)
            @Validated @RequestBody RegisterRequest request) {
        // 校验验证码信息
        String captchaKey = request.getCaptchaKey();
        String captchaCode = request.getCaptchaCode();
        validaCaptchaInfo(captchaKey, captchaCode);
        RegisterDto registerDto = request.createRegisterDto();
        // 用户注册
        User register = userService.register(registerDto);
        Map<String, String> result = new HashMap<>();
        result.put("userId", String.valueOf(register.getUserId()));
        return DataR.success(result);
    }


    /**
     * 验证验证码信息
     *
     * @param captchaKey  验证码缓存KEY
     * @param captchaCode 验证码值
     */
    private void validaCaptchaInfo(String captchaKey, String captchaCode) {

        // 验证码不存在
        if (!StringUtils.hasText(captchaKey)) {
            throw new BizException(ApiCodeEnum.CAPTCHA_INFO_NOT_FOUND_EXP);
        }
        // 验证码不存在
        if (!StringUtils.hasText(captchaCode)) {
            throw new BizException(ApiCodeEnum.CAPTCHA_INFO_NOT_FOUND_EXP);
        }
        // 校验验证码是否过期
        String cacheCaptchaCode = RedisService.getString(AuthCS.getCacheKeyCaptcha(captchaKey));
        if (!StringUtils.hasText(cacheCaptchaCode)) {
            throw new BizException(ApiCodeEnum.CAPTCHA_EXPIRE_EXP);
        }
        // 校验验证码是否正确
        if (!cacheCaptchaCode.equalsIgnoreCase(captchaCode)) {
            throw new BizException(ApiCodeEnum.CAPTCHA_VALID_FAILED_EXP);
        }
    }


}
