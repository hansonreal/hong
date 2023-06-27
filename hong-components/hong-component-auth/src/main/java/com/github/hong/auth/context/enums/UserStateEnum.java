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
public enum UserStateEnum {

    UNLOCKED("0"),
    LOCKED("1"),
    UNKNOWN("9");

    private final String state;

    public static UserStateEnum getEnumByState(String state) {
        for (UserStateEnum stateEnum : UserStateEnum.values()) {
            if (stateEnum.state.equals(state)) {
                return stateEnum;
            }
        }
        return UNKNOWN;
    }

}
