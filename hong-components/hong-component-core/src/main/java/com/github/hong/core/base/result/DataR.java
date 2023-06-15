package com.github.hong.core.base.result;

import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.base.code.IResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 响应结果，携带数据返回
 *
 * @author hanson
 * @since 2023/5/11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "响应结果", description = "响应结果，携带数据返回")
public class DataR<T> extends BaseR {
    @ApiModelProperty("响应数据")
    private T data;


    public DataR(IResultCode code, T data) {
        super(code);
        this.data = data;
    }

    public DataR(IResultCode code, String msg) {
        super(code);
        this.setRtnMsg(msg);
    }

    /**
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 返回数据
     */
    public static <T> DataR<T> success(T data) {
        return new DataR<>(ApiCodeEnum.SUCCESS, data);
    }

    public static <T> DataR<T> failure(String errorMsg) {
        DataR<T> dataR = new DataR<>(ApiCodeEnum.UNKNOWN_EXP, null);
        dataR.setRtnMsg(errorMsg);
        return dataR;
    }

    public static <T> DataR<T> failure() {
        return new DataR<>(ApiCodeEnum.UNKNOWN_EXP, null);
    }

    public static <T> DataR<T> failure(IResultCode code, String rtnMsg) {
        return new DataR<>(code, rtnMsg);
    }


}
