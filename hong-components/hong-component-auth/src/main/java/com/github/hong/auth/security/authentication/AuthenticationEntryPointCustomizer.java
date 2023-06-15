package com.github.hong.auth.security.authentication;

import com.github.hong.core.base.result.ErrorR;
import com.github.hong.core.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * 认证失败处理器
 *
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
public class AuthenticationEntryPointCustomizer implements AuthenticationEntryPoint, Serializable {

    public AuthenticationEntryPointCustomizer() {
        log.info("AuthenticationEntryPointCustomizer --- 构建了");
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        log.error("------------认证失败------------当前请求URL:{}", httpServletRequest.getRequestURL());
        e.printStackTrace();
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = httpServletResponse.getWriter();
        ErrorR errorR = new ErrorR();
        errorR.setRtnCode("401");
        errorR.setRtnMsg("认证失败，用户名/密码错误，请检查");
        writer.write(JsonUtil.serialize(errorR));
        writer.flush();
    }
}
