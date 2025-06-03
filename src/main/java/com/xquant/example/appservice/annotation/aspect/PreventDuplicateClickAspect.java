package com.xquant.example.appservice.annotation.aspect;

import com.xquant.example.appservice.annotation.PreventDuplicateClick;
import com.xquant.example.appservice.domain.vo.UserLoginVO;
import com.xquant.example.appservice.exceptions.BusinessException;
import com.xquant.example.appservice.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 05429
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PreventDuplicateClickAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(preventDuplicateClick)")
    public Object around(ProceedingJoinPoint joinPoint, PreventDuplicateClick preventDuplicateClick) throws Throwable {
        // 获取用户信息
        UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
        if (Objects.isNull(userLoginVO)) {
            throw new BusinessException(-1, "用户登录过期");
        }
        String authToken = userLoginVO.getAuthToken();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String args = StringUtils.arrayToDelimitedString(joinPoint.getArgs(), ",");

        String redisKey = String.format("duplicate:click:%s:%s:%s", authToken, methodName, args);

        RLock lock = redissonClient.getLock(redisKey);
        try {
            // 尝试获取锁，最多等待100ms，锁定preventDuplicateClick.interval()时间
            boolean acquired = lock.tryLock(100, preventDuplicateClick.interval(), TimeUnit.MILLISECONDS);
            if (!acquired) {
                throw new RuntimeException(preventDuplicateClick.message());
            }
            return joinPoint.proceed();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
