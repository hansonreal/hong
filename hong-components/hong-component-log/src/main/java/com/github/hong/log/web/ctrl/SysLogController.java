package com.github.hong.log.web.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.hong.core.annotation.MethodLog;
import com.github.hong.core.base.result.PageR;
import com.github.hong.entity.log.SysLog;
import com.github.hong.log.service.ISysLogService;
import com.github.hong.log.web.request.QueryLogRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 35533
 * @since 2022/9/19 0:20
 */
@RestController
@RequestMapping("/log")
@Api(value = "日志接口", tags = {"日志管理"})
public class SysLogController {

    private final ISysLogService sysLogService;

    public SysLogController(ISysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }


    @PostMapping("/logs")
    @MethodLog(value = "日志查询",recordRespParam = false)
    @ApiOperation(value = "日志查询", notes = "日志查询")
    public PageR<SysLog> logs(
            @ApiParam(name = "logCmd", value = "日志查询对象", required = true)
            @Validated @RequestBody QueryLogRequest logCmd) throws Exception {
        Page<SysLog> page = new Page<>(logCmd.getCurrent(), logCmd.getSize());
        LambdaQueryWrapper<SysLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Page<SysLog> sysLogPage = sysLogService.page(page, lambdaQueryWrapper);
        return PageR.ok(sysLogPage.getTotal(), sysLogPage.getRecords());
    }
}
