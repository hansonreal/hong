package com.github.hong.log.context.listener;

import com.github.hong.entity.log.SysLog;
import com.github.hong.log.context.event.MethodLogEvent;
import com.github.hong.log.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MethodLogListener implements ApplicationListener<MethodLogEvent> {

    private final ISysLogService sysLogService;

    public MethodLogListener(ISysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(MethodLogEvent event) {
        SysLog logSource = event.getLog();
        sysLogService.save(logSource);
    }
}
