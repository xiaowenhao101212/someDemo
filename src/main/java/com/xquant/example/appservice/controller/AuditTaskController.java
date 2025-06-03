package com.xquant.example.appservice.controller;


import com.xquant.example.appservice.annotation.PreventDuplicateClick;
import com.xquant.example.appservice.controller.response.ResponseModel;
import com.xquant.example.appservice.domain.dto.AuditTaskDTO;
import com.xquant.example.appservice.domain.dto.AuditTaskLogDTO;
import com.xquant.example.appservice.domain.dto.BizVerificationDTO;
import com.xquant.example.appservice.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.appservice.domain.page.PageVO;
import com.xquant.example.appservice.domain.vo.AuditTaskLogVO;
import com.xquant.example.appservice.domain.vo.AuditTaskVO;
import com.xquant.example.appservice.domain.vo.BizVerificationVO;
import com.xquant.example.appservice.enums.AuditCommitType;
import com.xquant.example.appservice.service.AuditTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 05429
 */
@Api(tags = "审批任务相关API")
@Slf4j
@RestController
@RequestMapping("/audit/task")
@RequiredArgsConstructor
public class AuditTaskController {

    private final AuditTaskService auditTaskService;

    @ApiOperation("分页查询审批任务")
    @GetMapping("/page")
    public ResponseModel<PageVO<AuditTaskVO>> page(@Valid QueryAuditTaskDTO queryAuditTaskDTO) {

        return ResponseModel.ok(auditTaskService.pageAuditTask(queryAuditTaskDTO));
    }


    @ApiOperation("查询单条审批任务详细信息")
    @GetMapping("/get")
    public ResponseModel<AuditTaskVO> get(@RequestParam(required = true, name = "taskId") Long taskId) {
        Validate.notNull(taskId, "taskId未传");
        return ResponseModel.ok(auditTaskService.get(taskId));
    }


    @ApiOperation("审批任务")
    @PostMapping("/auditTask")
    @PreventDuplicateClick()
    public ResponseModel<Void> auditTask(@Valid @RequestBody AuditTaskDTO auditTaskDTO) {
        // 参数验证
        AuditCommitType auditCommitType = AuditCommitType.ofValue(auditTaskDTO.getOperationType());
        Validate.notNull(auditCommitType, "非系统定义的操作类型");
        if (auditCommitType.equals(AuditCommitType.ABORT)) {
            Validate.notEmpty(auditTaskDTO.getRemark(), "驳回操作必需录入原因");
        }
        auditTaskService.auditTask(auditTaskDTO);
        return ResponseModel.ok();
    }

    @ApiOperation("查询审批日志")
    @GetMapping("/log/list")
    public ResponseModel<List<AuditTaskLogVO>> listLog(@Valid AuditTaskLogDTO auditTaskLogDTO) {

        return ResponseModel.ok(auditTaskService.listLog(auditTaskLogDTO));
    }

    @ApiOperation("业务验证")
    @GetMapping("/bizVerification")
    public ResponseModel<BizVerificationVO> bizVerification(@Valid BizVerificationDTO bizVerificationDTO) {

        return ResponseModel.ok(auditTaskService.bizVerification(bizVerificationDTO));
    }
}
