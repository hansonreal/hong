package com.github.hong.auth.context.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hanson
 * @since 2023/6/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserInfo {
    private Long userId;       //登录用户ID
    private String name;       // 用户名
    private Long created;      //创建时间, 格式：13位时间戳
    private String cacheKey;   //redis保存的key
}
