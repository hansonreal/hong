package com.github.hong.auth.context.security;

import com.github.hong.auth.context.properties.RsaKeyConfigProperties;
import com.github.hong.auth.context.security.access.CustomAccessDeniedHandler;
import com.github.hong.auth.context.security.authentication.AuthenticationEntryPointCustomizer;
import com.github.hong.auth.context.security.filter.JwtAuthenticationFilter;
import com.github.hong.auth.context.security.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
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

/**
 * @classname: SecurityConfig
 * @desc: SS配置类
 * @author: hanson
 * @history: 2023年06月11日 11时29分04秒
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new UserDetailsServiceImpl();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean())
                .passwordEncoder(passwordEncoderBean());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        RsaKeyConfigProperties keyConfigProperties = super.getApplicationContext().getBean(RsaKeyConfigProperties.class);
        httpSecurity.csrf().disable()  // 由于使用的是JWT，我们这里不需要csrf（跨站请求伪造）
                .cors()// 跨越配置支持，此处代码会自动加载一个bean名称为 corsFilter的Filter
                .and().exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPointCustomizer())// 认证失败处理方式
                .accessDeniedHandler(new CustomAccessDeniedHandler())//无权限操作异常处理
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 基于token，所以不需要session
                //.and().authorizeRequests().antMatchers(securityExt.getAnonymousUrls().toArray(new String[0])).permitAll()//可以匿名被访问的URL
                .and().authorizeRequests().anyRequest().authenticated()// 除上面外的所有请求全部需要鉴权认证
                .and().addFilterBefore(new JwtAuthenticationFilter(keyConfigProperties), UsernamePasswordAuthenticationFilter.class) // 添加JWT 认证过滤器filter
                .headers().cacheControl();// 禁用缓存
    }


}
