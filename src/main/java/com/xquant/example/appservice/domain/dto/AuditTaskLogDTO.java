package com.xquant.example.appservice.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 查询审核任务日志条件
 */
@Data
@ApiModel("查询审核任务日志条件")
public class AuditTaskLogDTO {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "任务id不能为空")
    @ApiModelProperty("关联的审核任务ID")
    private Long taskId;

    @ApiModelProperty("审核人")
    private String auditor;

    @ApiModelProperty("起始审核时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("结束审核时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("操作类型(1.通过 2.退回 3.拒绝)")
    private String operationType;
}
