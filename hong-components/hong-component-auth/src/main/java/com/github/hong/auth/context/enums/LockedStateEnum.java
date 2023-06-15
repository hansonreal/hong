package com.github.hong.auth.context.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 锁定状态
 *
 * @author hanson
 * @since 2023/6/5
 */
@Getter
@AllArgsConstructor
public enum LockedStateEnum {

    UNLOCKED("0"),
    LOCKED("1"),
    UNKNOWN("9");

    private final String state;

    public static LockedStateEnum getEnumByState(String state) {
        for (LockedStateEnum stateEnum : LockedStateEnum.values()) {
            if (stateEnum.state.equals(state)) {
                return stateEnum;
            }
        }
        return UNKNOWN;
    }

}
