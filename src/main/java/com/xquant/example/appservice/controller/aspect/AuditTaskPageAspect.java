package com.xquant.example.appservice.controller.aspect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 05429
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditTaskPageAspect {

    private final RedissonClient redissonClient;
    private final AuditTaskService auditTaskService;

    @AfterReturning(pointcut = "execution(* com.xquant.example.appservice.controller.response.AuditTaskV2Controller.page(..))", returning = "result")
    public void cacheNextPageData(JoinPoint joinPoint, ResponseModel<PageV2VO<AuditTaskPageVO>> result) {
        if (result != null && result.getData() != null) {
            PageV2VO<AuditTaskPageVO> pageData = result.getData();
            // 当前页码和每页大小
            int currentPage = pageData.getPageNum();
            // 计算下一页的查询参数
            int nextPage = currentPage + 1;
            if (nextPage > result.getData().getPages()) {
                return;
            }
            // 获取用户信息
            UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
            if (Objects.isNull(userLoginVO) || StrUtil.isBlank(userLoginVO.getAuthToken())) {
                return;
            }
            // 当前查询条件
            AuditTaskPageDTO currentPageDTO = (AuditTaskPageDTO) joinPoint.getArgs()[0];

            // 构建下一页的缓存键
            String cacheKey = String.format("user:%s:%s:%s:%s:%s:%s:%s",
                    userLoginVO.getAuthToken(),
                    currentPageDTO.getAPage(),
                    currentPageDTO.getATargetType(),
                    currentPageDTO.getABegdate(),
                    currentPageDTO.getAEnddate(),
                    currentPageDTO.getADataType(),
                    currentPageDTO.getAGroupCode());

            cacheKey = MD5.create().digestHex16(cacheKey);

            // 下一页 查询条件
            AuditTaskPageDTO nextPageDTO = BeanUtil.copyProperties(currentPageDTO, AuditTaskPageDTO.class);
            nextPageDTO.setAPage(nextPage);
            // 检查缓存中是否存在
            RMapCache<String, PageV2VO<AuditTaskPageVO>> pageCache = redissonClient.getMapCache(CacheEnum.AUDIT_TASK_PAGE.getCode());
            PageV2VO<AuditTaskPageVO> nextPageData = pageCache.get(cacheKey);
            // 已经有了就不放了
            if (Objects.nonNull(nextPageData)) {
                return;
            }
            // 查询下一页数据
            nextPageData = auditTaskService.mobileGetTaskNodeLstByPage(nextPageDTO);
            if (Objects.isNull(nextPageData)) {
                return;
            }
            pageCache.put(cacheKey, nextPageData, 30, TimeUnit.MINUTES);
            log.info("用户[{}]下一页审批数据缓存成功，key:[{}]", userLoginVO.getUserName(), cacheKey);
        }
    }
}
