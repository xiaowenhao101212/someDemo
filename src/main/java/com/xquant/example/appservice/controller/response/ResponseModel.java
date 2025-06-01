package com.xquant.example.appservice.controller.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseModel<T> implements Serializable {

    private static final long serialVersionUID = -1263032784821448286L;

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public ResponseModel() {
        this(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMessage(), null);
    }

    public ResponseModel(T data) {
        this(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMessage(), data);
    }

    public ResponseModel(int code, String msg) {
        this(code, msg, null);
    }

    public ResponseModel(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseModel<T> ok() {
        return new ResponseModel<>();
    }

    public static <T> ResponseModel<T> ok(T data) {
        return new ResponseModel<>(data);
    }

    public static <T> ResponseModel<T> of(int code, String msg, T data) {
        return new ResponseModel<>(code, msg, data);
    }

    public static <T> ResponseModel<T> fail(int code, String msg) {
        return new ResponseModel<>(code, msg);
    }
}
