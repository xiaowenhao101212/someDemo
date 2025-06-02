package com.xquant.example.appservice.service.impl;

import com.xquant.example.appservice.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.appservice.domain.entity.AuditTask;
import com.xquant.example.appservice.domain.page.PageResult;
import com.xquant.example.appservice.domain.vo.AuditTaskVO;
import com.xquant.example.appservice.mapper.AuditTaskMapper;
import com.xquant.example.appservice.service.AuditTaskService;
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
        // todo 伪造一些数据
        List<AuditTask> auditTasks = auditTaskMapper.list(queryAuditTaskDTO);
        return null;
    }
}
