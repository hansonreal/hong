package com.github.hong.log.context.event;

import com.github.hong.entity.sys.Log;
import org.springframework.context.ApplicationEvent;

public class MethodLogEvent extends ApplicationEvent {

    public MethodLogEvent(Log log) {
        super(log);
    }
}
