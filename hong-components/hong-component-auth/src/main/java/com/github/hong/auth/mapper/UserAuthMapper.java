package com.github.hong.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.hong.entity.auth.UserAuth;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户认证表 Mapper 接口
 * </p>
 *
 * @author 35533
 * @since 2023-06-05
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    UserAuth selectByLogin(@Param("identifier") String username, @Param("idType") String idType);
}
