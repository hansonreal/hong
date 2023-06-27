package com.github.hong.entity.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_sys_user")
@ApiModel(value = "SysUser对象", description = "系统用户表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("系统用户ID")
    @TableId(value = "sys_user_id", type = IdType.AUTO)
    private String sysUserId;

    @ApiModelProperty("登录用户名")
    @TableField("login_name")
    private String loginName;

    @ApiModelProperty("电子邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("手机号")
    @TableField("tel_phone")
    private String telPhone;

    @ApiModelProperty("性别 0-未知, 1-男, 2-女")
    @TableField("sex")
    private String sex;

    @ApiModelProperty("头像地址")
    @TableField("avatar_url")
    private String avatarUrl;

    @ApiModelProperty("是否超管（超管拥有全部权限） 0-否 1-是")
    @TableField("admin_flag")
    private String adminFlag;

    @ApiModelProperty("状态 0-停用 1-启用")
    @TableField("user_state")
    private String userState;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @TableField("updated_at")
    private Date updatedAt;


}
