package com.xquant.example.appservice.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "审批日志查询结果")
@Data
public class AuditTaskLogV2VO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("业务编号")
    private String ti;

    @ApiModelProperty("类型")
    private String ly;

    @ApiModelProperty("名义审批人")
    private String na;

    @ApiModelProperty("实际审批人")
    private String on;

    @ApiModelProperty("审批角色")
    private String rn;

    @ApiModelProperty("审批类型")
    private String sn;

    @ApiModelProperty("审批状态")
    private String an;

    @ApiModelProperty("时间")
    private String ot;

    @ApiModelProperty("审批意见")
    private String oe;
}