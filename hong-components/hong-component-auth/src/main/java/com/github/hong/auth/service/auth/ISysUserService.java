package com.github.hong.auth.service.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.hong.auth.service.auth.dto.RegisterDto;
import com.github.hong.entity.auth.SysUser;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 用户注册
     *
     * @param registerDto 注册参数
     * @return 用户信息
     */
    SysUser register(RegisterDto registerDto);


    /**
     * 用户是否重复
     *
     * @param user 检测信息
     * @return true 重复 |  false 不重复
     */
    boolean isRepeated(SysUser user);
}
