package com.xquant.example.appservice.controller.response;

import lombok.Getter;

@Getter
public enum ResponseStatusEnum {

    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 参数错误
     */
    PARAMETER_EXCEPTION(20, "参数错误"),

    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION(10, "系统异常");


    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String message;

    ResponseStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
