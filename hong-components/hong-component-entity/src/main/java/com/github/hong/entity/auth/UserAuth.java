package com.github.hong.entity.auth;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户认证表
 * </p>
 *
 * @author 35533
 * @since 2023-06-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("AW_USER_AUTH")
@KeySequence("SEQ_AW_USER_AUTH")
public class UserAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户认证唯一标识
     */
    @TableId(value = "AUTH_ID", type = IdType.NONE)
    private Long authId;

    /**
     * 用户标识
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 登陆类型/AW_IDENTITY_TYPE
     * 01:邮箱
     * 02:手机号码
     */
    @TableField("IDENTITY_TYPE")
    private String identityType;

    /**
     * 认证标识
     */
    @TableField("IDENTIFIER")
    private String identifier;

    /**
     * 密码凭证
     */
    @TableField("CREDENTIAL")
    private String credential;

    /**
     * 加密盐
     */
    @TableField("SALT")
    private String salt;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;


}
