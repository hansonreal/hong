package com.github.hong.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * RMS_MC系统日志表
 * </p>
 *
 * @author 35533
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("SYS_LOG")
@Accessors(chain = true)
@KeySequence("SEQ_SYS_LOG")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志标识
     */
    @TableId(value = "LOG_ID", type = IdType.NONE)
    private Long logId;

    /**
     * 日志年月
     */
    @TableField("LOG_YM")
    private String logYm;

    /**
     * 用户ID，AW_USER.USER_ID
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 用户姓名，AW_USER.NAME
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 用户IP
     */
    @TableField("USER_IP")
    private String userIp;

    /**
     * 所属模块： SUPPORT-支撑, CRM-业扩, MDM-电能数据, BILLING-抄核, IPAY-收费
     */
    @TableField("BIZ_MODE")
    private String bizMode;

    /**
     * 方法名
     */
    @TableField("METHOD_NAME")
    private String methodName;

    /**
     * 方法描述
     */
    @TableField("METHOD_DESC")
    private String methodDesc;

    /**
     * 请求地址
     */
    @TableField("REQU_URL")
    private String requUrl;

    /**
     * 操作请求参数
     */
    @TableField("REQU_INFO")
    private String requInfo;

    /**
     * 操作响应结果
     */
    @TableField("RESP_INFO")
    private String respInfo;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 持续时间，单位毫秒
     */
    @TableField("DURATION_TIME")
    private Long durationTime;


}
