package com.github.hong.log.context.listener;

import com.github.hong.entity.sys.Log;
import com.github.hong.log.context.event.MethodLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MethodLogListener implements ApplicationListener<MethodLogEvent> {

    private final ILogService logService;

    public MethodLogListener(ILogService logService) {
        this.logService = logService;
    }

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(MethodLogEvent event) {
        Log logSource = (Log) event.getSource();
        logService.save(logSource);
    }
}
