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
@ApiModel("查询限额检查结果条件")
public class BizValidDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "审批任务ID不能为空")
    @ApiModelProperty(value = "审批任务ID", example = "6761159")
    private Long aTaskId;

    @ApiModelProperty("是否包含审批通过和交易执行的交易")
    private Boolean aIsIncludeConfirmed = true;

    @ApiModelProperty("是否包括审批中的交易")
    private Boolean aIsIncludeOrdered = true;
}

