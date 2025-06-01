package com.xquant.example.appservice.controller.aspect;

import com.alibaba.fastjson.JSON;
import com.xquant.example.appservice.controller.response.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Web 请求日志切面
 *
 */
@Slf4j
@Aspect
@Component
@Order(-5)
public class WebLogAspect {

    private static final String UNKNOWN = "unknown";

    private static final long MIN_WARN_COST_TIME = 1000L;

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (null == ip || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (null == ip || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (null == ip || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (null == ip || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (null == ip || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object getInterceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return process(proceedingJoinPoint);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object postInterceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return process(proceedingJoinPoint);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public Object putInterceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return process(proceedingJoinPoint);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object deleteInterceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return process(proceedingJoinPoint);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object requestInterceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return process(proceedingJoinPoint);
    }

    private Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String uri = request.getRequestURI();
            String method = request.getMethod();
            String remoteAddress = getIpAddress(request);
            String args = "";
            if (null != proceedingJoinPoint.getArgs() && 0 < proceedingJoinPoint.getArgs().length) {
                Object arg = proceedingJoinPoint.getArgs()[0];
                if (!(arg instanceof ServletRequest) && !(arg instanceof ServletResponse)
                        && !(arg instanceof MultipartFile)) {
                    args = "with inputs: " + JSON.toJSONString(arg);
                }
            }
            if (log.isInfoEnabled()) {
                log.info("Request:{}\t{}\t{}\t{}", remoteAddress, method, uri, args);
            }

            result = proceedingJoinPoint.proceed();
            long end = System.currentTimeMillis();
            long cost = end - start;

            String output = "";
            if (result instanceof ResponseModel) {
                ResponseModel<?> model = (ResponseModel<?>) result;
                output = " code=" + model.getCode() + " \t  msg=\"" + model.getMsg() + "\"";
                if (null != model.getData()) {
                    output += " \t data=" + JSON.toJSONString(model.getData());
                }
            }
            if (MIN_WARN_COST_TIME <= cost) {
                if (log.isWarnEnabled()) {
                    log.warn("Response:{}\t{}\t{}\t{}ms\t{}\t{}", remoteAddress, method, uri, cost,
                            args, output);
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("Response:{}\t{}\t{}\t{}ms\t{}\t{}", remoteAddress, method, uri, cost,
                            args, output);
                }
            }
        } else {
            result = proceedingJoinPoint.proceed();
        }
        return result;
    }
}
