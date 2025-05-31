package com.xquant.example.app_service.mapper;

import com.xquant.example.app_service.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.app_service.domain.entity.AuditTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuditTaskMapper {

    List<AuditTask> list(QueryAuditTaskDTO queryDTO);
}
