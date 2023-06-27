package com.github.hong.entity.support;

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
 * 系统配置表
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_sys_config")
@ApiModel(value = "SysConfig对象", description = "系统配置表")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配置KEY")
    @TableId(value = "config_key", type = IdType.NONE)
    private String configKey;

    @ApiModelProperty("配置名称")
    @TableField("config_name")
    private String configName;

    @ApiModelProperty("描述信息")
    @TableField("config_desc")
    private String configDesc;

    @ApiModelProperty("分组key")
    @TableField("group_key")
    private String groupKey;

    @ApiModelProperty("分组名称")
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty("配置内容项")
    @TableField("config_val")
    private String configVal;

    @ApiModelProperty("类型: text-输入框, textarea-多行文本, uploadImg-上传图片, switch-开关")
    @TableField("type")
    private String type;

    @ApiModelProperty("显示顺序")
    @TableField("sort_num")
    private String sortNum;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @TableField("updated_at")
    private Date updatedAt;


}
