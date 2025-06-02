package com.xquant.example.appservice.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "审核操作对象")
public class AuditTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "审核任务ID不能为空")
    @ApiModelProperty(value = "审核任务ID", required = true)
    private Long taskId;

    @NotBlank(message = "操作类型不能为空")
    @ApiModelProperty(value = "操作类型", required = true)
    private String operationType;

    @ApiModelProperty(value = "备注")
    private String remark;


}
