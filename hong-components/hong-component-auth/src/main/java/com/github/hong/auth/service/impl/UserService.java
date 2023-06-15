package com.github.hong.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hong.auth.config.RsaKeyConfigProperties;
import com.github.hong.auth.context.enums.LockedStateEnum;
import com.github.hong.auth.mapper.UserMapper;
import com.github.hong.auth.service.IUserAuthService;
import com.github.hong.auth.service.IUserService;
import com.github.hong.auth.service.dto.RegisterDto;
import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.exception.BizException;
import com.github.hong.core.utils.RegUtil;
import com.github.hong.core.utils.RsaUtil;
import com.github.hong.entity.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.PrivateKey;
import java.util.Date;

/**
 * <p>
 * Website/app登陆用户信息 服务实现类
 * </p>
 *
 * @author 35533
 * @since 2023-06-05
 */
@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {


    @Autowired
    private RsaKeyConfigProperties rsaKeyConfigProperties;

    @Autowired
    private IUserAuthService userAuthService;

    /**
     * 用户注册
     *
     * @param registerDto 注册参数
     * @return 用户信息
     */
    @Override
    public User register(RegisterDto registerDto) {

        // 用户名校验
        String name = registerDto.getName();
        if (!StringUtils.hasText(name)) {
            BizException.throwExp(ApiCodeEnum.NAME_NOT_BE_EMPTY_EXP);
        } else {
            User user = new User().setName(name);
            boolean repeated = isRepeated(user);
            if (repeated) {
                BizException.throwExp(ApiCodeEnum.NAME_REPEATED_EXP, name);
            }
        }

        // 密码校验
        String pwd = registerDto.getPwd();
        if (!StringUtils.hasText(pwd)) {
            BizException.throwExp(ApiCodeEnum.PWD_NOT_BE_EMPTY_EXP);
        } else {
            PrivateKey privateKey = rsaKeyConfigProperties.getPrivateKey();
            try {
                String pwdSrc = RsaUtil.decrypt(pwd, privateKey);
                // 密码规则校验
                log.info("前台键入的密码为:{}", pwdSrc);
            } catch (Exception e) {
                log.error("注册 -- 密码还原失败:", e);
                BizException.throwExp(ApiCodeEnum.REGISTER_FAILED_EXP);
            }
        }
        // 邮箱&手机校验
        String email = registerDto.getMail();
        String phone = registerDto.getPhone();
        if (!StringUtils.hasText(email) && !StringUtils.hasText(phone)) {
            BizException.throwExp(ApiCodeEnum.REGISTERED_ACCOUNT_CAN_NOT_BE_EMPTY_EXP);
        }
        // 邮箱格式、是否已使用校验
        if (StringUtils.hasText(email)) {
            boolean isMatch = RegUtil.isEmail(email);
            if (!isMatch) {
                BizException.throwExp(ApiCodeEnum.EMAIL_FORMAT_EXP, email);
            }
            User user = new User().setEmail(email);
            boolean repeated = isRepeated(user);
            if (repeated) {
                BizException.throwExp(ApiCodeEnum.EMAIL_REPEATED_EXP, email);
            }
        }
        // 手机格式、是否已使用校验
        if (StringUtils.hasText(phone)) {
            boolean isMatch = RegUtil.isCnMobile(phone);
            if (!isMatch) {
                BizException.throwExp(ApiCodeEnum.MOBILE_NUMBER_FORMAT_EXP, phone);
            }
            User user = new User().setPhone(phone);
            boolean repeated = isRepeated(user);
            if (repeated) {
                BizException.throwExp(ApiCodeEnum.MOBILE_REPEATED_EXP, phone);
            }
        }

        // 创建用户信息
        User user = new User()
                .setName(name)
                .setPhone(phone)
                .setEmail(email)
                //.setProfPicture()
                .setLocked(LockedStateEnum.UNLOCKED.getState())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        boolean save = save(user);
        if (save) {
            userAuthService.buildDefaultUserAuth(user, pwd);
        }
        return user;
    }

    /**
     * 用户是否重复
     *
     * @param user 检测信息
     * @return true 重复 |  false 不重复
     */
    @Override
    public boolean isRepeated(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(user.getName()), User::getName, user.getName())
                .eq(StringUtils.hasText(user.getPhone()), User::getPhone, user.getPhone())
                .eq(StringUtils.hasText(user.getEmail()), User::getEmail, user.getEmail());
        return this.baseMapper.exists(lambdaQueryWrapper);
    }


}
