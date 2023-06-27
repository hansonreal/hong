package com.github.hong.auth.service.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.hong.auth.context.enums.IDTypeEnum;
import com.github.hong.entity.auth.SysUser;
import com.github.hong.entity.auth.SysUserAuth;

import java.util.List;

/**
 * <p>
 * 系统用户认证表 服务类
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
public interface ISysUserAuthService extends IService<SysUserAuth> {
    /**
     * 根据登录类型，登录名获取用户认证信息
     *
     * @param username   登录名
     * @param idTypeEnum 登录类型
     * @return 用户认证表
     */
    SysUserAuth selectByLogin(String username, IDTypeEnum idTypeEnum);

    /**
     * 构建默认的用户认证数据
     *
     * @param user 用户信息
     * @param pwdRaw  密码
     * @return 认证数据集合
     */
    List<SysUserAuth> buildDefaultUserAuth(SysUser user, String pwdRaw);
}
