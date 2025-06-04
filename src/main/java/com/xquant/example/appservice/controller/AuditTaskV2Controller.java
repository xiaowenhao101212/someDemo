package com.xquant.example.appservice.controller;


import com.xquant.example.appservice.annotation.PreventDuplicateClick;
import com.xquant.example.appservice.controller.response.ResponseModel;
import com.xquant.example.appservice.domain.dto.*;
import com.xquant.example.appservice.domain.page.PageV2VO;
import com.xquant.example.appservice.domain.vo.*;
import com.xquant.example.appservice.service.AuditTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 05429
 */
@Api(tags = "审批任务API")
@Slf4j
@RestController
@RequestMapping("/mobile/task")
@RequiredArgsConstructor
public class AuditTaskV2Controller {

    private final AuditTaskService auditTaskService;

    @ApiOperation("分页查询审批数据列表信息")
    @PostMapping("/page")
    public ResponseModel<PageV2VO<AuditTaskPageVO>> page(@RequestBody @Valid AuditTaskPageDTO queryDTO) {

        return ResponseModel.ok(auditTaskService.mobileGetTaskNodeLstByPage(queryDTO));
    }


    @ApiOperation("查询单笔审批单审批流程图")
    @PostMapping("/flow/get")
    public ResponseModel<AuditTaskNodeVO> getFlowInfo(@RequestBody @Valid AuditTaskNodeDTO queryDTO) {

        return ResponseModel.ok(auditTaskService.mobileSingleTaskNodeTree(queryDTO));
    }

    @ApiOperation("查询单笔审批单审批日志")
    @PostMapping("/log/get")
    public ResponseModel<List<AuditTaskLogV2VO>> getFlowInfo(@RequestBody @Valid AuditTaskLogV2DTO queryDTO) {

        return ResponseModel.ok(auditTaskService.mobileSingleTraceLog(queryDTO));
    }

    @ApiOperation("查询限额检查结果")
    @PostMapping("/biz/valid")
    public ResponseModel<List<BizValidVO>> bizValid(@RequestBody @Valid BizValidDTO queryDTO) {

        return ResponseModel.ok(auditTaskService.mobileSingleLimitresult(queryDTO));
    }


    @ApiOperation("批量审批操作")
    @PostMapping("/approvedBatch")
    @PreventDuplicateClick()
    public ResponseModel<?> approvedBatch(@RequestBody @Valid ApprovedBatchDTO approvedBatchDTO) {
        auditTaskService.mobileApproveTaskByNodeIdBatch(approvedBatchDTO);
        return ResponseModel.ok();
    }


    @ApiOperation("查询审批数据分组信息")
    @PostMapping("/listTaskNodeGroup")
    public ResponseModel<List<TaskNodeGroupVO>> listTaskNodeGroup(@RequestBody @Valid TaskNodeGroupDTO queryDTO) {
        return ResponseModel.ok(auditTaskService.mobileGetTaskNodeGroup(queryDTO));
    }
}
