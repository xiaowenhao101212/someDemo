package com.xquant.example.appservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缓存枚举
 *
 * @author 05429
 */
@Getter
@AllArgsConstructor
public enum CacheEnum {

    USER_SESSION("user:session", "用户认证信息");

    private final String code;

    private final String msg;

}
