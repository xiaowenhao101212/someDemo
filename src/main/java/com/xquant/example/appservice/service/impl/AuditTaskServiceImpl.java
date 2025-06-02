package com.xquant.example.appservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.xquant.example.appservice.domain.dto.AuditTaskDTO;
import com.xquant.example.appservice.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.appservice.domain.entity.AuditTask;
import com.xquant.example.appservice.domain.entity.AuditTaskLog;
import com.xquant.example.appservice.domain.page.PageVO;
import com.xquant.example.appservice.domain.vo.AuditTaskVO;
import com.xquant.example.appservice.domain.vo.UserLoginVO;
import com.xquant.example.appservice.enums.AuditCommitType;
import com.xquant.example.appservice.enums.AuditStatus;
import com.xquant.example.appservice.exceptions.BusinessException;
import com.xquant.example.appservice.mapper.AuditTaskLogMapper;
import com.xquant.example.appservice.mapper.AuditTaskMapper;
import com.xquant.example.appservice.service.AuditTaskService;
import com.xquant.example.appservice.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 05429
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditTaskServiceImpl implements AuditTaskService {

    private final AuditTaskMapper auditTaskMapper;
    private final AuditTaskLogMapper auditTaskLogMapper;

    @Override
    public PageVO<AuditTaskVO> pageAuditTask(QueryAuditTaskDTO queryAuditTaskDTO) {
        try (Page<AuditTaskVO> pageVO = PageMethod.startPage(queryAuditTaskDTO.getPageNum(), queryAuditTaskDTO.getPageSize())) {
            List<AuditTask> dbAuditTasks = auditTaskMapper.list(queryAuditTaskDTO);
            // 转换为 VO 列表
            List<AuditTaskVO> auditTaskVOList = dbAuditTasks.stream()
                    .map(v -> BeanUtil.copyProperties(v, AuditTaskVO.class))
                    .collect(Collectors.toList());
            pageVO.clear();
            pageVO.addAll(auditTaskVOList);
            return PageVO.wrap(pageVO);
        }
    }

    @Override
    public AuditTaskVO get(Long taskId) {
        AuditTask auditTask = auditTaskMapper.get(taskId);
        if (Objects.isNull(auditTask)) {
            throw new BusinessException(-1, "数据不存在");
        }
        return BeanUtil.copyProperties(auditTask, AuditTaskVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditTask(AuditTaskDTO auditTaskDTO) {
        // 用户信息
        UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
        if (Objects.isNull(userLoginVO)) {
            throw new BusinessException(-1, "用户信息不存在");
        }
        // 审批任务信息
        AuditTask auditTask = auditTaskMapper.get(auditTaskDTO.getTaskId());
        if (Objects.isNull(auditTask)) {
            throw new BusinessException(-1, "数据不存在");
        }

        try {
            int lock = auditTaskMapper.lock(auditTaskDTO.getTaskId());
            if (lock == 0) {
                throw new BusinessException(-1, "数据已被其他人审批");
            }
            // 审核人
            auditTask.setApprover(userLoginVO.getUserCode());
            // 如果是通过，需要处理是否下一个节点操作，如果是驳回，直接置为7 结束流程
            if (AuditCommitType.ACCEPT.getValue().equals(auditTaskDTO.getOperationType())) {
                // 这里需要根据实际业务需求处理，目前仅模拟4
                auditTask.setStatus(AuditStatus.AUDITED.getCode());
            } else if (AuditCommitType.ABORT.getValue().equals(auditTaskDTO.getOperationType())) {
                auditTask.setStatus(AuditStatus.SIGNED.getCode());
            }
            // 更新审核任务
            auditTaskMapper.update(auditTask);

            // 组装日志表
            AuditTaskLog auditLog = new AuditTaskLog();
            auditLog.setTaskId(auditTaskDTO.getTaskId());
            auditLog.setAuditor(userLoginVO.getUserCode());
            auditLog.setOperationType(auditTaskDTO.getOperationType());
            auditLog.setRemark(auditTaskDTO.getRemark());
            // 审核前节点
            auditLog.setCurrentNode(auditTask.getStatus());
            // 保存审批日志
            auditTaskLogMapper.insert(auditLog);

        } catch (Exception e) {
            log.error("审批异常", e);
            throw new BusinessException(-1, "审批失败");
        } finally {
            auditTaskMapper.unlock(auditTaskDTO.getTaskId());
        }
    }

}
