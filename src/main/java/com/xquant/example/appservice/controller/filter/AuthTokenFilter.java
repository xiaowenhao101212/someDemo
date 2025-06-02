package com.xquant.example.appservice.controller.filter;

import cn.hutool.core.util.StrUtil;
import com.xquant.example.appservice.config.AppProperties;
import com.xquant.example.appservice.domain.vo.UserLoginVO;
import com.xquant.example.appservice.enums.CacheEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenFilter implements Filter {

    private final RedissonClient redissonClient;
    private final AppProperties appProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestPath = httpRequest.getRequestURI();

        // 1. 检查是否为排除路径
        if (isExcludedPath(requestPath)) {
            log.debug("跳过认证检查: {}", requestPath);
            chain.doFilter(request, response);
            return;
        }

        // 2. 获取 authToken
        String authToken = getAuthTokenFromRequest(httpRequest);
        if (StrUtil.isBlank(authToken)) {
            sendErrorResponse(httpResponse, "用户未登录", 401);
            return;
        }

        // 3. 判断缓存中token是否过期，以及自动续期操作
        boolean notExpired = checkAndRenewSession(authToken);
        if (!notExpired) {
            sendErrorResponse(httpResponse, "用户登录过期", 402);
            return;
        }

        chain.doFilter(request, response);
    }

    // 检查是否为排除路径
    private boolean isExcludedPath(String requestPath) {
        if(StrUtil.containsAny(requestPath,"swagger","doc","webjars")){
            return true;
        }
        String[] excludePaths = appProperties.getAuth().getExcludePaths().split(",");
        return Arrays.stream(excludePaths)
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }

    // 从请求中获取 authToken
    private String getAuthTokenFromRequest(HttpServletRequest request) {
        // 尝试从参数中获取
        String token = request.getParameter(appProperties.getAuth().getTokenName());

        // 如果参数中没有，尝试从Header中获取
        if (token == null) {
            token = request.getHeader(appProperties.getAuth().getTokenName());
        }

        // 如果Header中也没有，尝试从Cookie中获取
        if (token == null && request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                if (appProperties.getAuth().getTokenName().equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        return StringUtils.hasText(token) ? token : null;
    }

    // 发送错误响应
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
                "{\"code\": %d, \"msg\": \"%s\", \"timestamp\": %d}",
                status, message, System.currentTimeMillis()
        ));
    }

    private boolean checkAndRenewSession(String authToken) {
        RMapCache<String, UserLoginVO> userCache = redissonClient.getMapCache(CacheEnum.USER_SESSION.getCode());
        if (!userCache.containsKey(authToken) || Objects.isNull(userCache.get(authToken))) {
            return false;
        }
        long ttl = userCache.remainTimeToLive(authToken);
        if (ttl < 0) {
            return false;
        }
        // 小于五分钟 自动续期
        if (ttl < 5 * 60 * 1000) {
            UserLoginVO vo = userCache.get(authToken);
            userCache.remove(authToken);
            userCache.put(authToken, vo, 30, TimeUnit.MINUTES);
            log.info("用户[{}]会话续期成功", userCache.get(authToken).getUserName());
        }
        return true;
    }
}
