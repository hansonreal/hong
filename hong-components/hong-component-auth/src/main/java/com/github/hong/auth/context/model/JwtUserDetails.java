package com.github.hong.auth.context.model;

import com.github.hong.entity.auth.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author hanson
 * @since 2023/6/5
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JwtUserDetails implements UserDetails {
    /**
     *
     */
    private User sysUser;
    /**
     * 密码
     **/
    private String credential;

    /**
     * 角色+权限 集合   （角色必须以： ROLE_ 开头）
     **/
    private Collection<SimpleGrantedAuthority> authorities = Collections.emptyList();

    /**
     * 缓存标志
     **/
    private String cacheKey;

    /**
     * 登录IP
     **/
    private String loginIp;


    public JwtUserDetails(User sysUser, String credential) {
        this.setSysUser(sysUser);
        this.setCredential(credential);
        //做一些初始化操作
    }

    public static JwtUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        try {
            return (JwtUserDetails) authentication.getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return getCredential();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
