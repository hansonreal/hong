package com.github.hong.support.web.ctrl;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统字典表 前端控制器
 * </p>
 *
 * @author hansonreal
 * @since 2023-06-27
 */
@RestController
@RequestMapping("/support/sys-dict")
@Api(value = "字典接口", tags = {"字典管理"})
public class SysDictController {

}
