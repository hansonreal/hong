package com.github.hong.core.base.result;

import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.base.code.IResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 分页返回结果
 *
 * @author hanson
 * @since 2023/6/9
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@ApiModel(value = "分页响应结果", description = "分页响应结果，携带数据返回")
public class PageR<E> implements IR {
    @ApiModelProperty("响应码")
    private String rtnCode = RTN_CODE;

    @ApiModelProperty("响应信息")
    private String rtnMsg = RTN_MSG;

    @ApiModelProperty("响应时间")
    private Date timestamp;

    @ApiModelProperty("记录总数")
    private Long total;

    @ApiModelProperty("当前页的结果集")
    private List<E> rows;

    public PageR(IResultCode codeEnum, Long total, List<E> rows) {
        this.rtnCode = codeEnum.getRtnCode();
        this.rtnMsg = codeEnum.getRtnMsg();
        this.timestamp = new Date();
        this.total = total;
        this.rows = rows;
    }


    public static <T> PageR<T> ok(Long total, List<T> rows) {
        return new PageR<>(ApiCodeEnum.SUCCESS, total, rows);
    }

}
