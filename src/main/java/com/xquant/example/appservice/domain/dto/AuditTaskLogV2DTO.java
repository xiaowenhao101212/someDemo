package com.xquant.example.appservice.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 05429
 */
@Data
@ApiModel("单笔审批单审批日志查询条件")
public class AuditTaskLogV2DTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "审批任务ID不能为空")
    @ApiModelProperty(value = "审批任务ID", example = "6761159")
    private Long tid;
}
