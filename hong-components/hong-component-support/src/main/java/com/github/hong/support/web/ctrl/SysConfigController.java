package com.github.hong.support.web.ctrl;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@RestController
@RequestMapping("/support/sys-config")
@Api(value = "参数接口", tags = {"参数管理"})
public class SysConfigController {

}
