package com.github.hong.auth.security.filter;

import com.github.hong.auth.config.AuthConfigProperties;
import com.github.hong.auth.context.model.JwtUserDetails;
import com.github.hong.auth.context.model.JwtUserInfo;
import com.github.hong.auth.context.utils.JwtUtil;
import com.github.hong.core.cache.RedisService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;

/**
 * JWT认证过滤器
 *
 * @author hanson
 * @since 2023/6/5
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private AuthConfigProperties authConfigProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("进入JWT认证过滤器");
        JwtUserDetails jwtUserDetails = commonFilter(request);

        if (ObjectUtils.isEmpty(jwtUserDetails)) {
            filterChain.doFilter(request, response);
            return;
        }
        //将信息放置到Spring-security context中
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(jwtUserDetails, null, jwtUserDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }


    private JwtUserDetails commonFilter(HttpServletRequest request) {

        String authToken = request.getHeader(authConfigProperties.getAccessTokenName());
        if (!StringUtils.hasLength(authToken)) {
            authToken = request.getParameter(authConfigProperties.getAccessTokenName());
        }
        if (!StringUtils.hasLength(authToken)) {
            return null; //放行,并交给UsernamePasswordAuthenticationFilter进行验证,返回公共错误信息.
        }

        PublicKey publicKey = authConfigProperties.getPublicKey();
        JwtUserInfo jwtPayload = JwtUtil.getJwtFromToken(authToken, publicKey);
        //token字符串解析失败
        if (ObjectUtils.isEmpty(jwtPayload)) {
            return null;
        }
        String cacheKey = jwtPayload.getCacheKey();
        if (!StringUtils.hasLength(cacheKey)) {
            return null;
        }
        //根据用户名查找数据库
        JwtUserDetails jwtUserDetails = RedisService.getObject(cacheKey, JwtUserDetails.class);
        if (jwtUserDetails == null) {
            RedisService.del(cacheKey);
            return null; //数据库查询失败，删除redis
        }
        //续签时间
        RedisService.expire(cacheKey, authConfigProperties.getAccessTokenExpSec());
        return jwtUserDetails;
    }
}
