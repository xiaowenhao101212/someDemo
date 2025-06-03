package com.xquant.example.appservice.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("批量审批操作入参")
public class ApprovedBatchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "审批任务编号列表不能为空")
    @ApiModelProperty(value = "审批任务编号列表,英文逗号分隔", example = "6900864,6900823")
    private String aNodeIds;

    @ApiModelProperty(value = "审批类型",example = "OTCTRADE")
    private String targetType;

    @ApiModelProperty("审批动作")
    private String aAction;

    @ApiModelProperty("备注")
    private String actionNote;

    @ApiModelProperty("会签角色")
    private String aCCRoles;

    @ApiModelProperty("数据类型")
    private String aDataTypeCurrent;



}
