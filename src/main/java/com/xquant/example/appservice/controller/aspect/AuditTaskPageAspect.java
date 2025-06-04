package com.xquant.example.appservice.controller.aspect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.xquant.example.appservice.controller.response.ResponseModel;
import com.xquant.example.appservice.domain.dto.AuditTaskPageDTO;
import com.xquant.example.appservice.domain.page.PageV2VO;
import com.xquant.example.appservice.domain.vo.AuditTaskPageVO;
import com.xquant.example.appservice.domain.vo.UserLoginVO;
import com.xquant.example.appservice.enums.CacheEnum;
import com.xquant.example.appservice.service.AuditTaskService;
import com.xquant.example.appservice.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 05429
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditTaskPageAspect {

    private final AuditTaskService auditTaskService;
    private final RedisTemplate<String, String> redisTemplate;
    private final ExecutorService executorService;

    @AfterReturning(pointcut = "execution(* com.xquant.example.appservice.controller.response.AuditTaskV2Controller.page(..))", returning = "result")
    public void cacheNextPageData(JoinPoint joinPoint, ResponseModel<PageV2VO<AuditTaskPageVO>> result) {
        if (result != null && result.getData() != null) {
            // 获取用户信息
            UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
            if (Objects.isNull(userLoginVO) || StrUtil.isBlank(userLoginVO.getAuthToken())) {
                return;
            }
            String authToken = userLoginVO.getAuthToken();
            executorService.submit(() -> {
                PageV2VO<AuditTaskPageVO> pageData = result.getData();
                // 当前页码和每页大小
                int currentPage = pageData.getPageNum();
                // 计算下一页的查询参数
                int nextPage = currentPage + 1;
                if (nextPage > result.getData().getPages()) {
                    return;
                }
                // 当前查询条件
                AuditTaskPageDTO currentPageDTO = (AuditTaskPageDTO) joinPoint.getArgs()[0];

                // 构建下一页的缓存键
                String cacheKey = String.format("%s:%s:%s:%s:%s:%s:%s",
                        authToken,
                        nextPage,
                        currentPageDTO.getATargetType(),
                        currentPageDTO.getABegdate(),
                        currentPageDTO.getAEnddate(),
                        currentPageDTO.getADataType(),
                        currentPageDTO.getAGroupCode());

                cacheKey = CacheEnum.AUDIT_TASK_PAGE.getCode() + ":" + MD5.create().digestHex16(cacheKey);

                // 下一页 查询条件
                AuditTaskPageDTO nextPageDTO = BeanUtil.copyProperties(currentPageDTO, AuditTaskPageDTO.class);
                nextPageDTO.setAPage(nextPage);
                // 检查缓存中是否存在
                String cachePageDateStr = redisTemplate.opsForValue().get(cacheKey);
                // 已经有了就不放了
                if (StrUtil.isNotBlank(cachePageDateStr)) {
                    return;
                }
                // 查询下一页数据
                nextPageDTO.setUserCode(userLoginVO.getUserCode());
                nextPageDTO.setAuthToken(userLoginVO.getAuthToken());
                PageV2VO<AuditTaskPageVO> nextPageData = auditTaskService.mobileGetTaskNodeLstByPage(nextPageDTO);

                // 没有数据，不处理
                if (Objects.isNull(nextPageData) || CollectionUtil.isEmpty(nextPageData.getList())) {
                    return;
                }
                log.info("缓存下一页数据，key:[{}]", cacheKey);
                redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(nextPageData), 30, TimeUnit.MINUTES);
            });
            log.info("分页数据返回");
        }
    }
}
