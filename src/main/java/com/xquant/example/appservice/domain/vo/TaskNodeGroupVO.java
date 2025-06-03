package com.xquant.example.appservice.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("查询审批数据分组结果")
@Data
public class TaskNodeGroupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组合编")
    private String c;

    @ApiModelProperty(value = "组合名称")
    private String m;

    @ApiModelProperty(value = "项目编号")
    private String p;

    @ApiModelProperty(value = "组合里面的数量")
    private String ct;
}
