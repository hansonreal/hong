package com.github.hong.core.base.code;

/**
 *
 * @author hanson
 * @since 2023/6/9
 */
public interface IResultCode {
    /**
     * @return 操作代码
     */
    String getRtnCode();

    /**
     * @return 提示信息
     */
    String getRtnMsg();
}
