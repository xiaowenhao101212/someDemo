package com.xquant.example.appservice.controller.aspect;

import com.xquant.example.appservice.controller.response.ResponseModel;
import com.xquant.example.appservice.controller.response.ResponseStatusEnum;
import com.xquant.example.appservice.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static ResponseModel<?> fromBindingResult(BindingResult bindingResult) {
        String message = "参数异常";
        if (bindingResult != null) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getDefaultMessage();
            }
        }
        return ResponseModel.fail(ResponseStatusEnum.PARAMETER_EXCEPTION.getCode(), message);
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseModel<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        if (log.isWarnEnabled()) {
            log.warn("参数校验异常", e);
        }
        return fromBindingResult(e.getBindingResult());
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseModel<?> beanPropertyBindingNotValidExceptionHandler(BindException e) {
        if (log.isWarnEnabled()) {
            log.warn("参数校验异常", e);
        }
        return fromBindingResult(e.getBindingResult());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseModel<?> businessException(BusinessException e) {
        if (log.isWarnEnabled()) {
            log.warn("业务异常:[{}-{}]", e.getErrorNo(), e.getMessage());
        }
        return ResponseModel.fail(e.getErrorNo(), e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseModel<?> globalExceptionHandler(Throwable e) {
        if (log.isErrorEnabled()) {
            log.error("系统出现异常", e);
        }

        return ResponseModel.fail(ResponseStatusEnum.SYSTEM_EXCEPTION.getCode(), "系统异常");
    }

    /**
     * 非法数据异常
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseModel<?> illegalArgumentException(IllegalArgumentException e) {
        if (log.isErrorEnabled()) {
            log.error("数据验证出现异常", e);
        }
        return ResponseModel.fail(ResponseStatusEnum.SYSTEM_EXCEPTION.getCode(), e.getMessage());
    }
}
