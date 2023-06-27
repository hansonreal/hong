package com.github.hong.auth.context.security.handler;

import com.github.hong.core.base.result.ErrorR;
import com.github.hong.core.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 没有授权异常处理
 *
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
public class AccessDeniedHandlerCustomizer implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        log.warn("------------无权操作------------");
        ErrorR errorR = new ErrorR();
        errorR.setRtnCode("403");
        errorR.setRtnMsg("无权操作");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(JsonUtil.serialize(errorR));
        writer.flush();
    }
}
