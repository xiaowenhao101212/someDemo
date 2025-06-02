package com.xquant.example.appservice.util;

import com.xquant.example.appservice.domain.vo.UserLoginVO;
import com.xquant.example.appservice.enums.CacheEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class CookieUtil {

    // 静态变量用于存储注入的 redissonClient
    private static RedissonClient redissonClient;

    @Autowired
    private RedissonClient injectedRedissonClient;

    @PostConstruct
    public void init() {
        redissonClient = injectedRedissonClient;
    }

    /**
     * 从请求的Cookie中获取authToken参数值，并从Redis中获取用户信息
     *
     * @return 用户信息对象，如果不存在则返回null
     */
    public static UserLoginVO getUserInfoFromCookie() {
        HttpServletRequest request = getRequest();
        if (request == null || request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if ("authToken".equals(cookie.getName())) {
                // 从Redis中获取用户信息
                String authToken = cookie.getValue();
                RMapCache<String, UserLoginVO> userCache = redissonClient.getMapCache(CacheEnum.USER_SESSION.getCode());
                return userCache.get(authToken);
            }
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        try {
            return getRequestAttributes().getRequest();
        } catch (Exception var1) {
            return null;
        }
    }

    public static ServletRequestAttributes getRequestAttributes() {
        try {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        } catch (Exception var1) {
            return null;
        }
    }
}