package com.github.hong.log.context.event;

import com.github.hong.entity.log.SysLog;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class MethodLogEvent extends ApplicationEvent {

    private SysLog log;

    public MethodLogEvent(SysLog log) {
        super(log);
        this.log = log;
    }
}
