package com.xquant.example.appservice.mapper;

import com.xquant.example.appservice.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.appservice.domain.entity.AuditTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuditTaskMapper {

    List<AuditTask> list(QueryAuditTaskDTO queryDTO);

    AuditTask get(Long taskId);

    int lock(Long taskId);

    int unlock(Long taskId);

    int update(AuditTask auditTask);
}
