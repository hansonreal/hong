package com.github.hong.entity.log;

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
 * 系统操作日志表
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-28
 */
@Getter
@Setter
@TableName("t_sys_log")
@Accessors(chain = true)
@ApiModel(value = "SysLog对象", description = "系统操作日志表")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("系统日志标识")
    @TableId(value = "sys_log_id", type = IdType.AUTO)
    private String sysLogId;

    @ApiModelProperty("系统用户标识")
    @TableField("sys_user_id")
    private String sysUserId;

    @ApiModelProperty("登录姓名")
    @TableField("login_name")
    private String loginName;

    @ApiModelProperty("用户IP")
    @TableField("user_ip")
    private String userIp;

    @ApiModelProperty("方法名")
    @TableField("method_name")
    private String methodName;

    @ApiModelProperty("方法描述")
    @TableField("method_desc")
    private String methodDesc;

    @ApiModelProperty("请求地址")
    @TableField("req_url")
    private String reqUrl;

    @ApiModelProperty("操作请求参数")
    @TableField("opt_req_info")
    private String optReqInfo;

    @ApiModelProperty("操作响应结果")
    @TableField("opt_res_info")
    private String optResInfo;

    @ApiModelProperty("方法执行持续时间，单位毫秒")
    @TableField("duration_time")
    private String durationTime;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private Date createdAt;


}
