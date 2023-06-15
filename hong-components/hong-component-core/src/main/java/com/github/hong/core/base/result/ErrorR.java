package com.github.hong.core.base.result;

import com.github.hong.core.base.code.ApiCodeEnum;
import com.github.hong.core.exception.BizException;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 异常返回结果
 *
 * @author 35533
 * @since 2023/2/14
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@ApiModel(value = "异常返回结果", description = "异常返回结果")
public class ErrorR extends BaseR {


    public ErrorR(String rtnCode, String rtnMsg) {
        super.setRtnCode(rtnCode);
        super.setRtnMsg(rtnMsg);
        super.setTimestamp(new Date());
    }

    public static ErrorR exp(BizException exp) {
        String code = exp.getCode();
        String msg = exp.getMsg();
        return new ErrorR(code, msg);
    }

    public static ErrorR fail(ApiCodeEnum codeEnum) {
        return new ErrorR(codeEnum.getRtnCode(), codeEnum.getRtnMsg());
    }
}
