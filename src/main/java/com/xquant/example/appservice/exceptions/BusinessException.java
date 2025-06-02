package com.xquant.example.appservice.exceptions;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1;

    private int errorNo;

    public BusinessException(int errorNo, String message) {
        super(message);
        this.errorNo = errorNo;
    }

    public BusinessException(int errorNo, String format, Object... args) {
        super(String.format(format, args));
        this.errorNo = errorNo;
    }

    public int getErrorNo() {
        return errorNo;
    }

}
