package com.github.hong.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.hong.auth.context.enums.IDTypeEnum;
import com.github.hong.entity.auth.User;
import com.github.hong.entity.auth.UserAuth;

import java.util.List;

/**
 * <p>
 * 用户认证表 服务类
 * </p>
 *
 * @author 35533
 * @since 2023-06-05
 */
public interface IUserAuthService extends IService<UserAuth> {

    /**
     * 根据登录类型，登录名获取用户认证信息
     *
     * @param username   登录名
     * @param idTypeEnum 登录类型
     * @return 用户认证表
     */
    UserAuth selectByLogin(String username, IDTypeEnum idTypeEnum);

    /**
     * 构建默认的用户认证数据
     *
     * @param user 用户信息
     * @param pwdRaw  密码
     * @return 认证数据集合
     */
    List<UserAuth> buildDefaultUserAuth(User user, String pwdRaw);

}
