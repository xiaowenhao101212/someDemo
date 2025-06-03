package com.xquant.example.appservice.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author 05429
 */
@Data
@ApiModel("分页查询审批数据列表条件")
public class AuditTaskPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(value = 1, message = "页码最小值为 1")
    @ApiModelProperty(value = "页数,从1开始", example = "1")
    private Integer aPage = 1;

    @ApiModelProperty(value = "审批类型", example = "OTCTRADE")
    private String aTargetType;

    @ApiModelProperty(value = "起始日期", example = "2025-06-01")
    private String aBegdate;

    @ApiModelProperty(value = "结束日期", example = "2025-06-02")
    private String aEnddate;

    @ApiModelProperty(value = "数据类型", example = "0")
    private Integer aDataType;

    @ApiModelProperty(value = "审批单分组编号", example = "0")
    private Integer aGroupCode;
}
