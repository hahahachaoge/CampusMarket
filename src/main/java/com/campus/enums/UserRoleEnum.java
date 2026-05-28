package com.campus.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    USER(1,"普通用户"),
    ADMIN(2,"管理员");

    private final Integer code;
    private final String desc;
}
