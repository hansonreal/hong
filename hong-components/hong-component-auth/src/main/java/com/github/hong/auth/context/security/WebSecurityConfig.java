package com.github.hong.auth.context.security;

import com.github.hong.auth.context.properties.AuthConfigProperties;
import com.github.hong.auth.context.security.authentication.AuthenticationEntryPointCustomizer;
import com.github.hong.auth.context.security.filter.JwtAuthenticationFilter;
import com.github.hong.auth.context.security.handler.AccessDeniedHandlerCustomizer;
import com.github.hong.auth.context.security.service.impl.UserDetailsServiceImpl;
import com.github.hong.auth.context.security.util.SecurityContextExt;
import com.github.hong.core.utils.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Set;

/**
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private AuthConfigProperties authConfigProperties;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 允许跨域请求
     **/
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if (authConfigProperties.isAllowCrossOrigin()) {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);   //带上cookie信息
            config.addAllowedOrigin(CorsConfiguration.ALL);  //允许跨域的域名， *表示允许任何域名使用
            config.addAllowedOriginPattern(CorsConfiguration.ALL);  //使用addAllowedOriginPattern 避免出现 When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
            config.addAllowedHeader(CorsConfiguration.ALL);   //允许任何请求头
            config.addAllowedMethod(CorsConfiguration.ALL);   //允许任何方法（post、get等）
            source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        }
        return new CorsFilter(source);
    }


    /**
     * Override this method to expose a {@link UserDetailsService} created from
     * {@link #configure(AuthenticationManagerBuilder)} as a bean. In general only the
     * following override should be done of this method:
     *
     * <pre>
     * &#064;Bean(name = &quot;myUserDetailsService&quot;)
     * // any or no name specified is allowed
     * &#064;Override
     * public UserDetailsService userDetailsServiceBean() throws Exception {
     * 	return super.userDetailsServiceBean();
     * }
     * </pre>
     * <p>
     * To change the instance returned, developers should change
     * {@link #userDetailsService()} instead
     *
     * @return the {@link UserDetailsService}
     * @throws Exception
     * @see #userDetailsService()
     */
    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new UserDetailsServiceImpl();
    }

    /**
     * 获取认证管理器
     *
     * @return 认证管理器
     * @throws Exception 异常信息
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 使用DaoAuthenticationProvider 来进行认证
     * 需要提供数据来源，密码加密方式
     *
     * @param auth 认证管理器构建起
     * @throws Exception 异常信息
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean())
                .passwordEncoder(passwordEncoder());
    }


    /**
     * 配置认证
     *
     * @param httpSecurity the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        SecurityContextExt securityContextExt = SpringBeanUtil.getBean(SecurityContextExt.class);
        Set<String> anonymousUrls = securityContextExt.getAnonymousUrls();
        httpSecurity
                .csrf().disable()  // 由于使用的是JWT，这里不需要csrf（跨站请求伪造）
                .cors()// 跨越配置支持，此处代码会自动加载一个bean名称为 corsFilter的Filter
                .and().exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPointCustomizer(authConfigProperties))// 认证失败处理方式
                .and().exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerCustomizer())//无权限操作异常处理
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 基于token，所以不需要session
                .and().authorizeRequests().antMatchers(anonymousUrls.toArray(new String[0])).permitAll()//可以匿名被访问的URL
                .and().authorizeRequests().anyRequest().authenticated()// 除上面外的所有请求全部需要鉴权认证
                .and().addFilterBefore(
                        new JwtAuthenticationFilter(authConfigProperties),
                        UsernamePasswordAuthenticationFilter.class) // 添加JWT 认证过滤器filter
                .headers().cacheControl();// 禁用缓存
    }

    /**
     * WebSecurity 不走过滤链的放行  即不通过security 完全对外的/最大级别的放行
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore文件 ： 无需进入spring security 框架
        // 1.允许对于网站静态资源的无授权访问
        // 2.对于获取token的rest api要允许匿名访问
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                //.and().ignoring().antMatchers(anonymousUrls.toArray(new String[0]))
                .and().ignoring()
                .antMatchers(
                        "/",
                        "*.html",
                        "*.css",
                        "*.js",
                        "*.png",
                        "*.jpg",
                        "*.jpeg",
                        "*.svg",
                        "*.ico",
                        "*.webp",
                        "*.xls",
                        "*.mp4",
                        "swagger/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/doc.html",
                        "/favicon.ico",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/webjars/",
                        "/service-worker.js",
                        "/static/",
                        "/webjars/**",
                        "/actuator/**",
                        "/error",
                        "/**/*.mp4"   //支持mp4格式的文件匿名访问
                )
                .antMatchers(
                        "/auth/**" //匿名访问接口
                );
    }


}
