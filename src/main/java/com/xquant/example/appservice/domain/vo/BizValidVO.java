package com.xquant.example.appservice.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 05429
 */
@Data
@ApiModel("查询限额检查结果条件")
public class BizValidVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "限额名称")
    private String na;

    @ApiModelProperty("限额类型")
    private String ty;

    @ApiModelProperty("交易前结果")
    private String bf;

    @ApiModelProperty("交易后结果")
    private String af;

    @ApiModelProperty("差值")
    private String df;

    @ApiModelProperty("风险情况")
    private String ca;

    @ApiModelProperty("限额级别")
    private String le;

    @ApiModelProperty("交易编号")
    private String sid;

    @ApiModelProperty("指标名称")
    private String me;
}

