package com.xquant.example.appservice.controller;


import com.xquant.example.appservice.domain.dto.QueryAuditTaskDTO;
import com.xquant.example.appservice.domain.page.PageResult;
import com.xquant.example.appservice.domain.vo.AuditTaskVO;
import com.xquant.example.appservice.service.AuditTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/page")
    public ResponseEntity<PageResult<AuditTaskVO>> page(@RequestBody @Validated QueryAuditTaskDTO queryAuditTaskDTO) {
        
        return ResponseEntity.ok(auditTaskService.pageAuditTask(queryAuditTaskDTO));
    }

}
