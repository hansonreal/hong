package com.github.hong.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hong.entity.sys.Log;
import com.github.hong.log.mapper.LogMapper;
import com.github.hong.log.service.ILogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * RMS_MC系统日志表 服务实现类
 * </p>
 *
 * @author 35533
 * @since 2023-06-07
 */
@Service
public class LogService extends ServiceImpl<LogMapper, Log> implements ILogService {

    /**
     * 分页查询操作日期
     *
     * @param page   分页对象
     * @param sysLog 请求参数
     * @return 操作日志集合
     */
    @Override
    public Page<Log> querySysLogs(Page<Log> page, Log sysLog) {
        LambdaQueryWrapper<Log> payLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        payLogLambdaQueryWrapper.orderByDesc(Log::getLogId);
        return this.page(page, payLogLambdaQueryWrapper);
    }
}
