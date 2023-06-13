package com.github.hong.auth.context.security.filter;

import com.github.hong.auth.context.properties.RsaKeyConfigProperties;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @classname: JwtAuthorization
 * @desc: JWT 认证过滤器
 * @author: hanson
 * @history: 2023年06月11日 11时17分37秒
 * @link 科普 https://www.cnblogs.com/liaojie970/p/8459628.html
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private RsaKeyConfigProperties rsaKeyConfigProperties;

    public JwtAuthenticationFilter(RsaKeyConfigProperties rsaKeyConfigProperties) {
        this.rsaKeyConfigProperties = rsaKeyConfigProperties;
    }

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

    }
}
