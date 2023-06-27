package com.github.hong.support.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hong.entity.support.SysConfig;
import com.github.hong.support.mapper.SysConfigMapper;
import com.github.hong.support.service.ISysConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@Service
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

}
