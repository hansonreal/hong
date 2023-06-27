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
 * 系统字典分项表
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_sys_dict_item")
@ApiModel(value = "SysDictItem对象", description = "系统字典分项表")
public class SysDictItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("字典分项标识")
    @TableId(value = "sys_dict_item_id", type = IdType.AUTO)
    private String sysDictItemId;

    @ApiModelProperty("字典标识")
    @TableField("sys_dict_id")
    private String sysDictId;

    @ApiModelProperty("字典项文本")
    @TableField("item_text")
    private String itemText;

    @ApiModelProperty("字典项值")
    @TableField("item_value")
    private String itemValue;

    @ApiModelProperty("描述")
    @TableField("item_desc")
    private String itemDesc;

    @ApiModelProperty("排序")
    @TableField("sort_order")
    private String sortOrder;

    @ApiModelProperty("状态 1-启用 0-不启用")
    @TableField("item_state")
    private String itemState;

    @TableField("create_by")
    private String createBy;

    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @TableField("updated_at")
    private Date updatedAt;


}
