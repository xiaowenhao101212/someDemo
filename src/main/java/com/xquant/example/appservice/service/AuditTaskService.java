package com.xquant.example.appservice.service;

import com.xquant.example.appservice.domain.dto.AuditTaskDTO;
import com.xquant.example.appservice.domain.dto.AuditTaskLogDTO;
import com.xquant.example.appservice.domain.dto.BizVerificationDTO;
import com.xquant.example.appservice.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.appservice.domain.page.PageVO;
import com.xquant.example.appservice.domain.vo.AuditTaskLogVO;
import com.xquant.example.appservice.domain.vo.AuditTaskVO;
import com.xquant.example.appservice.domain.vo.BizVerificationVO;
import org.apache.ibatis.annotations.Param;

import javax.validation.Valid;
import java.util.List;

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
    PageVO<AuditTaskVO> pageAuditTask(QueryAuditTaskDTO queryAuditTaskDTO);

    /**
     * 通过任务id查询详细信息
     *
     * @param taskId
     * @return
     */
    AuditTaskVO get(Long taskId);

    void auditTask(AuditTaskDTO auditTaskDTO);

    List<AuditTaskLogVO> listLog(@Valid AuditTaskLogDTO auditTaskLogDTO);

    BizVerificationVO bizVerification(@Valid BizVerificationDTO bizVerificationDTO);
}
