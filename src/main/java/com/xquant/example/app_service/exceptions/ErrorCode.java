package com.xquant.example.app_service.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 05429
 */
@AllArgsConstructor
@Getter
public class ErrorCode {

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误提示
     */
    private final String message;

}
