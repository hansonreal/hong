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
 * 系统字典表
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_sys_dict")
@ApiModel(value = "SysDict对象", description = "系统字典表")
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("字典标识")
    @TableId(value = "sys_dict_id", type = IdType.AUTO)
    private String sysDictId;

    @ApiModelProperty("字典名称")
    @TableField("dict_name")
    private String dictName;

    @ApiModelProperty("字典编码")
    @TableField("dict_code")
    private String dictCode;

    @ApiModelProperty("字典描述")
    @TableField("dict_desc")
    private String dictDesc;

    @ApiModelProperty("删除状态 0-未删除, 1-已删除")
    @TableField("del_flag")
    private String delFlag;

    @ApiModelProperty("创建人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("更新人")
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @TableField("updated_at")
    private Date updatedAt;


}
