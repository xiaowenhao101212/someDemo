package com.xquant.example.appservice.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 审核任务流转日志视图对象
 */
@ApiModel(value = "审批流程图查询结果")
@Data
public class AuditTaskNodeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("节点编号")
    private String id;

    @ApiModelProperty("名称")
    private String tx;

    @ApiModelProperty("编号")
    private String va;

    @ApiModelProperty("")
    private String in;

    @ApiModelProperty("节点类型")
    private String ty;

    @ApiModelProperty("操作状态")
    private String le;

    @ApiModelProperty("节点图标显示类型")
    private String lc;

    @ApiModelProperty("详细节点信息")
    private List<AuditTaskNodeVO> childNodes;
}