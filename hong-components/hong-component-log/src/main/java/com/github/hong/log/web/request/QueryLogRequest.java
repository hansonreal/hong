package com.github.hong.log.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 35533
 * @since 2022/9/19 9:19
 */
@Data
@NoArgsConstructor
@ApiModel(value = "查询日志对象", description = "用于包装查询日志时所有参数")
public class QueryLogRequest {

    @JsonProperty("current")
    @ApiModelProperty(value = "当前页数", example = "1")
    private long current = 1;

    @JsonProperty("size")
    @ApiModelProperty(value = "每页数量", example = "50")
    private long size = 50;
}
