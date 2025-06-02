package com.xquant.example.appservice.mapper;

import com.xquant.example.appservice.domain.dto.AuditTaskLogDTO;
import com.xquant.example.appservice.domain.entity.AuditTaskLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 审核任务日志数据访问接口
 */
@Mapper
public interface AuditTaskLogMapper {

    /**
     * 根据任务ID查询日志
     *
     * @param taskId 任务ID
     * @return 日志列表
     */
    List<AuditTaskLog> getByTaskId(Long taskId);

    /**
     * 分页查询日志
     *
     * @param dto 查询条件
     * @return 日志列表
     */
    List<AuditTaskLog> list(AuditTaskLogDTO dto);

    /**
     * 插入日志记录
     *
     * @param log 日志对象
     */
    void insert(AuditTaskLog log);
}
