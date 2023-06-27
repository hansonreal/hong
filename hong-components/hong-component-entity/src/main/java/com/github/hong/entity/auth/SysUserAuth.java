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
 * 系统用户认证表
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_sys_user_auth")
@ApiModel(value = "SysUserAuth对象", description = "系统用户认证表")
public class SysUserAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("系统用户认证标识")
    @TableId(value = "sys_auth_id", type = IdType.AUTO)
    private String sysAuthId;

    @ApiModelProperty("系统用户标识")
    @TableField("sys_user_id")
    private String sysUserId;

    @ApiModelProperty("登录类型  1-登录账号 2-手机号 3-邮箱  10-微信  11-QQ 12-支付宝 13-微博")
    @TableField("identity_type")
    private String identityType;

    @ApiModelProperty("认证标识 ( 用户名 | open_id )")
    @TableField("identifier")
    private String identifier;

    @ApiModelProperty("密码凭证")
    @TableField("credential")
    private String credential;

    @ApiModelProperty("salt")
    @TableField("salt")
    private String salt;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @TableField("updated_at")
    private Date updatedAt;


}
