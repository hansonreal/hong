package com.github.hong.auth.context.utils;

import com.github.hong.auth.context.constants.AuthCS;
import com.github.hong.auth.context.model.JwtUserInfo;
import com.github.hong.auth.context.model.Token;
import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.exception.BizException;
import com.github.hong.core.utils.NumberUtil;
import com.github.hong.core.utils.StringUtil;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtil {

    /**
     * 生成用户token
     *
     * @param jwtInfo    待处理数据
     * @param privateKey 私钥
     * @param expSeconds 有效期，单位秒
     * @throws BizException 异常
     */
    public static Token generateUserToken(JwtUserInfo jwtInfo, PrivateKey privateKey, long expSeconds) throws BizException {
        JwtBuilder jwtBuilder = Jwts.builder()
                //设置主题
                .setSubject(String.valueOf(jwtInfo.getUserId()))
                .claim(AuthCS.TOKEN_NAME, jwtInfo.getName())
                .claim(AuthCS.JWT_KEY_CREATED, jwtInfo.getCreated())
                .claim(AuthCS.JWT_KEY_CACHE_KEY, jwtInfo.getCacheKey());
        return generateToken(jwtBuilder, privateKey, expSeconds);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     token
     * @param publicKey 公钥
     * @return JwtUserInfo
     */
    public static JwtUserInfo getJwtFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        String strUserId = body.getSubject();
        String name = StringUtil.getObjectValue(body.get(AuthCS.JWT_KEY_NAME));
        String srtCreated = StringUtil.getObjectValue(body.get(AuthCS.JWT_KEY_CREATED));
        String cacheKey = StringUtil.getObjectValue(body.get(AuthCS.JWT_KEY_CACHE_KEY));
        Long created = NumberUtil.longValueOf0(srtCreated);
        return new JwtUserInfo(strUserId, name, created, cacheKey);
    }

    /**
     * 生成token
     *
     * @param builder    JWT构建器
     * @param privateKey 私钥
     * @param expSeconds 有效期，单位秒
     * @throws BizException 异常
     */
    public static Token generateToken(JwtBuilder builder, PrivateKey privateKey, long expSeconds) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime expireLocalDateTime = localDateTime.plusSeconds(expSeconds);
        Date expireDate = Date.from(expireLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        //jwt串
        String compactJws =
                builder.setExpiration(expireDate)
                        //设置算法（必须）
                        .signWith(privateKey, SignatureAlgorithm.RS256)
                        .compact();
        return new Token(compactJws, expSeconds);
    }

    /**
     * 公钥解析token
     *
     * @param token     待解析的JWT字符串
     * @param publicKey 公钥
     * @return 返回结果
     * @throws BizException 异常信息
     */
    public static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        try {
            return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            //过期
            throw new BizException(ApiCodeEnum.JWT_TOKEN_EXPIRED_EXP);
        } catch (SecurityException ex) {
            //签名错误
            throw new BizException(ApiCodeEnum.JWT_SIGNATURE_FAIL);
        } catch (IllegalArgumentException ex) {
            //token 为空
            throw new BizException(ApiCodeEnum.JWT_ILLEGAL_ARGUMENT);
        } catch (Exception e) {
            log.error("errcode:{}, message:{}", ApiCodeEnum.JWT_PARSER_TOKEN_FAIL, e.getMessage());
            throw new BizException(ApiCodeEnum.JWT_PARSER_TOKEN_FAIL);
        }
    }


    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public static String refreshToken(String token, PublicKey publicKey) {
        return null;
    }
}