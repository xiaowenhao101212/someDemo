package com.xquant.example.appservice.service;

import com.xquant.example.appservice.domain.dto.*;
import com.xquant.example.appservice.domain.page.PageV2VO;
import com.xquant.example.appservice.domain.page.PageVO;
import com.xquant.example.appservice.domain.vo.*;

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


    PageV2VO<AuditTaskPageVO> mobileGetTaskNodeLstByPage(@Valid AuditTaskPageDTO queryDTO);

    AuditTaskNodeVO mobileSingleTaskNodeTree(@Valid AuditTaskNodeDTO queryDTO);

    List<AuditTaskLogV2VO> mobileSingleTraceLog(@Valid AuditTaskLogV2DTO queryDTO);

    List<BizValidVO> mobileSingleLimitresult(@Valid BizValidDTO queryDTO);

    void mobileApproveTaskByNodeIdBatch(@Valid ApprovedBatchDTO approvedBatchDTO);

    List<TaskNodeGroupVO> mobileGetTaskNodeGroup(@Valid TaskNodeGroupDTO queryDTO);
}
