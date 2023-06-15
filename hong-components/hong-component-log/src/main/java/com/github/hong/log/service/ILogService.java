package com.github.hong.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.hong.entity.sys.Log;

/**
 * <p>
 * RMS_MC系统日志表 服务类
 * </p>
 *
 * @author 35533
 * @since 2023-06-07
 */
public interface ILogService extends IService<Log> {

    /**
     * 分页查询操作日期
     *
     * @param page   分页对象
     * @param sysLog 请求参数
     * @return 操作日志集合
     */
    Page<Log> querySysLogs(Page<Log> page, Log sysLog);
}
