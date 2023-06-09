package com.github.hong.core.base.result;

import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.base.code.IResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author hanson
 * @since 2023/6/9
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "响应结果", description = "响应结果，携带数据返回")
public class DataR<T> implements IR {


    @ApiModelProperty("响应码")
    private String rtnCode = RTN_CODE;

    @ApiModelProperty("响应信息")
    private String rtnMsg = RTN_MSG;

    @ApiModelProperty("响应时间")
    private Date timestamp;

    @ApiModelProperty("响应数据")
    private T data;

    public DataR() {
        this.timestamp = new Date();
    }

    public DataR(IResultCode code) {
        this.rtnCode = code.getRtnCode();
        this.rtnMsg = code.getRtnMsg();
        this.timestamp = new Date();
    }

    public DataR(String rtnCode, String rtnMsg) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
        this.timestamp = new Date();
    }

    public DataR(IResultCode code, T data) {
        this.rtnCode = code.getRtnCode();
        this.rtnMsg = code.getRtnMsg();
        this.timestamp = new Date();
        this.data = data;
    }


    /**
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 返回数据
     */
    public static <T> DataR<T> ok(T data) {
        return new DataR<>(ApiCodeEnum.SUCCESS, data);
    }

    /**
     * @param code 异常码
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 返回数据
     */
    public static <T> DataR<T> ok(IResultCode code, T data) {
        return new DataR<>(code, data);
    }


    /**
     * @param code 异常码
     * @param <T>  数据类型
     * @return 返回数据
     */
    public static <T> DataR<T> fail(IResultCode code) {
        return new DataR<>(code);
    }

    /**
     * @param code   异常码
     * @param rtnMsg 异常信息
     * @param <T>    数据类型
     * @return 返回数据
     */
    public static <T> DataR<T> fail(IResultCode code, String rtnMsg) {
        DataR<T> dataR = new DataR<>(code);
        dataR.setRtnMsg(rtnMsg);
        return dataR;
    }


    /**
     * 超时异常
     *
     * @param <T> 数据类型
     * @return 返回数据
     */
    public static <T> DataR<T> timeout() {
        return new DataR<>(ApiCodeEnum.TIMEOUT_EXP);
    }

    /**
     * 超时异常
     *
     * @param rtnMsg 自定义超时返回消息
     * @param <T>    数据类型
     * @return 返回数据
     */
    public static <T> DataR<T> timeout(String rtnMsg) {
        DataR<T> dataR = new DataR<>(ApiCodeEnum.TIMEOUT_EXP);
        dataR.setRtnMsg(rtnMsg);
        return dataR;
    }

}
