package com.github.hong.core.base.result;

import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.base.code.IResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@ApiModel(value = "分页响应结果", description = "分页响应结果，携带数据返回")
public class PageR<T> extends BaseR {
    @ApiModelProperty("总数")
    private Long total;
    @ApiModelProperty("结果集")
    private List<T> rows;

    public PageR(IResultCode codeEnum) {
        super(codeEnum);
    }

    public PageR(IResultCode codeEnum, Long total, List<T> rows) {
        super(codeEnum);
        this.total = total;
        this.rows = rows;
    }

    public static <T> PageR<T> ok(Long total, List<T> rows) {
        return new PageR<>(ApiCodeEnum.SUCCESS, total, rows);
    }

}
