package com.xquant.example.appservice.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("批量审批操作结果")
public class ApprovedBatchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审批类型", example = "OTCTRADE")
    private String targetType;
}
