package com.github.hong.log.context.event;

import com.github.hong.entity.log.SysLog;
import org.springframework.context.ApplicationEvent;

public class MethodLogEvent extends ApplicationEvent {

    public MethodLogEvent(SysLog log) {
        super(log);
    }
}
