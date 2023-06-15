package com.github.hong.core.base.result;

import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.base.code.IResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author hanson
 * @since 2023/6/5
 */
@Data
@AllArgsConstructor
@ApiModel(value = "基础响应结果", description = "基础响应结果，不带数据返回")
public class BaseR implements IR {

    @ApiModelProperty("响应时间")
    private Date timestamp;

    @ApiModelProperty("响应码")
    private String rtnCode = RTN_CODE;

    @ApiModelProperty("响应信息")
    private String rtnMsg = RTN_MSG;

    public BaseR() {
        timestamp = new Date();
    }

    public BaseR(IResultCode code) {
        this.rtnCode = code.getRtnCode();
        this.rtnMsg = code.getRtnMsg();
        this.timestamp = new Date();
    }

    public BaseR(String rtnCode, String rtnMsg) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
        this.timestamp = new Date();
    }

    /**
     * 成功返回
     *
     * @return 提供静态方法
     */
    public static BaseR ok() {
        return new BaseR(ApiCodeEnum.SUCCESS);
    }

    /**
     * 成功返回
     *
     * @return 提供静态方法
     */
    public static BaseR ok(String rtnMsg) {
        BaseR baseR = new BaseR(ApiCodeEnum.SUCCESS);
        baseR.setRtnMsg(rtnMsg);
        return baseR;
    }


    /**
     * 失败返回
     *
     * @return 提供静态方法
     */
    public static BaseR fail(IResultCode code) {
        return new BaseR(code);
    }


    /**
     * 失败返回
     *
     * @return 提供静态方法
     */
    public static BaseR fail(String rtnMsg) {
        BaseR baseR = new BaseR(ApiCodeEnum.UNKNOWN_EXP);
        baseR.setRtnMsg(rtnMsg);
        return baseR;
    }

    /**
     * 请求超时
     *
     * @param rtnMsg
     * @return 请求超时
     */
    public static IR timeout(String rtnMsg) {
        BaseR baseR = new BaseR(ApiCodeEnum.UNKNOWN_EXP);
        baseR.setRtnMsg(rtnMsg);
        return baseR;
    }

}
