package com.github.hong.core.web.filter;

import com.github.hong.core.web.servlet.RepeatedlyReadRequestWrapper;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Order(Integer.MIN_VALUE)
//@Component
//@WebFilter(urlPatterns = "/*", filterName = "requestBodyFilter")
public class RequestBodyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String contentType = req.getContentType();
        ServletRequest requestWrapper = null;
        if (!MediaType.MULTIPART_FORM_DATA_VALUE.equals(contentType)) {// 文件上传时不可以过滤器包装request，会报错Required request part 'file' is not present
            requestWrapper = new RepeatedlyReadRequestWrapper(req);
        }
        if (null == requestWrapper) {// 过滤器包装request不需要，将返回原来的request
            chain.doFilter(request, response);
        } else {// 过滤器包装request成功
            chain.doFilter(requestWrapper, response);
        }
    }
}
