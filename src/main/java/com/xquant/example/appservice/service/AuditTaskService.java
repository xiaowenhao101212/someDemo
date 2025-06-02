package com.xquant.example.appservice.service;

import com.xquant.example.appservice.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.appservice.domain.page.PageResult;
import com.xquant.example.appservice.domain.vo.AuditTaskVO;

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
