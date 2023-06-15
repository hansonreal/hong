package com.github.hong.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.hong.auth.service.dto.RegisterDto;
import com.github.hong.entity.auth.User;

/**
 * <p>
 * Website/app登陆用户信息 服务类
 * </p>
 *
 * @author 35533
 * @since 2023-06-05
 */
public interface IUserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param registerDto 注册参数
     * @return 用户信息
     */
    User register(RegisterDto registerDto);


    /**
     * 用户是否重复
     *
     * @param user 检测信息
     * @return true 重复 |  false 不重复
     */
    boolean isRepeated(User user);
}
