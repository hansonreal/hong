package com.github.hong.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.hong.entity.auth.SysUserAuth;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户认证表 Mapper 接口
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
public interface SysUserAuthMapper extends BaseMapper<SysUserAuth> {
    SysUserAuth selectByLogin(@Param("identifier") String username, @Param("idType") String idType);
}
