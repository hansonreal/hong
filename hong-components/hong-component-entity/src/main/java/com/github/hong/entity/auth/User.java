package com.github.hong.entity.auth;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Website/app登陆用户信息
 * </p>
 *
 * @author 35533
 * @since 2023-06-05
 */
@Getter
@Setter
@TableName("AW_USER")
@Accessors(chain = true)
@KeySequence("SEQ_AW_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户标识
     */
    @TableId(value = "USER_ID", type = IdType.NONE)
    private Long userId;

    /**
     * 姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 手机
     */
    @TableField("PHONE")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 头像
     */
    @TableField("PROF_PICTURE")
    private String profPicture;

    /**
     * 0否1是
     */
    @TableField("LOCKED")
    private String locked;

    /**
     * 锁定时间
     */
    @TableField("LOCKED_TIME")
    private Date lockedTime;

    /**
     * 最近登录时间
     */
    @TableField("LAST_LOGIN_TIME")
    private Date lastLoginTime;

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
