package com.xquant.example.appservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统错误枚举类
 *
 * @author 05429
 */
@Getter
@AllArgsConstructor
public enum SystemErrorCodeConstants {

    AUTH_USERNAME_NOT_EXISTS("A0001", "账号不存在");

    private final String code;

    private final String message;

}
