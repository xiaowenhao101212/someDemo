package com.xquant.example.appservice.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("查询审批数据分组条件")
@Data
public class TaskNodeGroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审批类型", example = "OTCTRADE")
    private String aTargetType;

    @ApiModelProperty(value = "起始日期", example = "2025-06-01")
    private String aBegdate;

    @ApiModelProperty(value = "结束日期", example = "2025-06-02")
    private String aEnddate;

    @ApiModelProperty(value = "数据类型", example = "0")
    private Integer aDataType;
}
