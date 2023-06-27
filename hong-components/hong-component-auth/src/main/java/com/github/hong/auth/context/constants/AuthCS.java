package com.github.hong.auth.context.constants;

/**
 * 认证服务常量类
 *
 * @author hanson
 * @since 2023/6/5
 */
public final class AuthCS {
    /**
     *
     */
    public static final String TOKEN_NAME = "token";
    /**
     * 用户id
     */
    public static final String JWT_KEY_USER_ID = "userid";
    /**
     * 用户名称
     */
    public static final String JWT_KEY_NAME = "name";
    /**
     * 创建时间, 格式：13位时间戳
     */
    public static final String JWT_KEY_CREATED = "created";
    /**
     * redis保存的key
     */
    public static final String JWT_KEY_CACHE_KEY = "cacheKey";


    /**
     * 缓存key: 当前用户所有用户的token集合  example: TOKEN_1001_HcNheNDqHzhTIrT0lUXikm7xU5XY4Q
     */
    public static final String CACHE_KEY_TOKEN = "token_%s_%s";
    /**
     * 验证码 缓存key
     **/
    public static final String CACHE_KEY_CAPTCHA_CODE = "captcha_%s";


    private AuthCS() {

    }

    public static String getCacheKeyToken(String userId, String uuid) {
        return String.format(CACHE_KEY_TOKEN, userId, uuid);
    }

    public static String getCacheKeyCaptcha(String captchaToken) {
        return String.format(CACHE_KEY_CAPTCHA_CODE, captchaToken);
    }
}
