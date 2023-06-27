package com.github.hong.support.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hong.entity.support.SysDict;
import com.github.hong.support.mapper.SysDictMapper;
import com.github.hong.support.service.ISysDictService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统字典表 服务实现类
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@Service
public class SysDictService extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

}
