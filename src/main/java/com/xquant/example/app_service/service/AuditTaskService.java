package com.xquant.example.app_service.service;

import com.xquant.example.app_service.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.app_service.domain.page.PageResult;
import com.xquant.example.app_service.domain.vo.AuditTaskVO;

/**
 * 审批任务接口
 *
 * @author 05429
 */
public interface AuditTaskService {

    /**
     * 分页查询审批任务
     *
     * @param queryAuditTaskDTO 查询条件
     * @return 分页列表
     */
    PageResult<AuditTaskVO> pageAuditTask(QueryAuditTaskDTO queryAuditTaskDTO);
}
