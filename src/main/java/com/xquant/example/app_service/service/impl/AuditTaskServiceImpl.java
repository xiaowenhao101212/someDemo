package com.xquant.example.app_service.service.impl;

import com.xquant.example.app_service.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.app_service.domain.entity.AuditTask;
import com.xquant.example.app_service.domain.page.PageResult;
import com.xquant.example.app_service.domain.vo.AuditTaskVO;
import com.xquant.example.app_service.mapper.AuditTaskMapper;
import com.xquant.example.app_service.service.AuditTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 05429
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditTaskServiceImpl implements AuditTaskService {

    private final AuditTaskMapper auditTaskMapper;

    @Override
    public PageResult<AuditTaskVO> pageAuditTask(QueryAuditTaskDTO queryAuditTaskDTO) {
        List<AuditTask> list = auditTaskMapper.list(queryAuditTaskDTO);
        return null;
    }
}
