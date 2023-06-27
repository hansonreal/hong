package com.github.hong.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hong.entity.log.SysLog;
import com.github.hong.log.mapper.SysLogMapper;
import com.github.hong.log.service.ISysLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统操作日志表 服务实现类
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@Service
public class SysLogService extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

}
