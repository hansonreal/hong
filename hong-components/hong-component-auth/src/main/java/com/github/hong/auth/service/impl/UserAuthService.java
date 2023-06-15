package com.github.hong.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hong.auth.context.enums.IDTypeEnum;
import com.github.hong.auth.mapper.UserAuthMapper;
import com.github.hong.auth.service.IUserAuthService;
import com.github.hong.core.utils.SpringBeanUtil;
import com.github.hong.core.utils.StringUtil;
import com.github.hong.entity.auth.User;
import com.github.hong.entity.auth.UserAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户认证表 服务实现类
 * </p>
 *
 * @author 35533
 * @since 2023-06-05
 */
@Slf4j
@Service
public class UserAuthService extends ServiceImpl<UserAuthMapper, UserAuth> implements IUserAuthService {

    /**
     * 根据登录类型，登录名获取用户认证信息
     *
     * @param username   登录名
     * @param idTypeEnum 登录类型
     * @return 用户认证表
     */
    @Override
    public UserAuth selectByLogin(String username, IDTypeEnum idTypeEnum) {
        return baseMapper.selectByLogin(username, idTypeEnum.getIdType());
    }

    /**
     * 构建默认的用户认证数据
     *
     * @param user   用户信息
     * @param pwdRaw 密码
     * @return 认证数据集合
     */
    @Override
    public List<UserAuth> buildDefaultUserAuth(User user, String pwdRaw) {
        String salt = StringUtil.getUUID(6); //6位随机数
        BCryptPasswordEncoder bCryptPasswordEncoder = SpringBeanUtil.getBean(BCryptPasswordEncoder.class);
        String encode = bCryptPasswordEncoder.encode(pwdRaw);
        Long userId = user.getUserId();
        String phone = user.getPhone();
        List<UserAuth> userAuthList = new ArrayList<>();
        if (StringUtils.hasText(phone)) {
            // 创建默认的认证信息
            UserAuth userAuth = new UserAuth()
                    .setUserId(userId)
                    .setIdentityType(IDTypeEnum.MOBILE.getIdType())
                    .setIdentifier(phone)
                    .setCredential(encode)
                    .setSalt(salt)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());
            boolean save = save(userAuth);
            if (save) {
                userAuthList.add(userAuth);
            }
        }
        String email = user.getEmail();
        if (StringUtils.hasText(email)) {
            // 创建默认的认证信息
            UserAuth userAuth = new UserAuth()
                    .setUserId(userId)
                    .setIdentityType(IDTypeEnum.EMAIL.getIdType())
                    .setIdentifier(email)
                    .setCredential(encode)
                    .setSalt(salt)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());
            boolean save = save(userAuth);
            if (save) {
                userAuthList.add(userAuth);
            }
        }
        return userAuthList;
    }
}
