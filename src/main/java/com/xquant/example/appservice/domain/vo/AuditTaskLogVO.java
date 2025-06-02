package com.xquant.example.appservice.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核任务流转日志视图对象
 */
@Data
public class AuditTaskLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日志主键ID")
    private Long logId;

    @ApiModelProperty("关联的审核任务ID")
    private Long taskId;

    @ApiModelProperty("审核人")
    private String auditor;

    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    @ApiModelProperty("当前节点编码")
    private String currentNode;

    @ApiModelProperty("当前节点名称")
    private String currentNodeName;

    @ApiModelProperty("前一个节点编码")
    private String previousNode;

    @ApiModelProperty("下一个节点编码")
    private String nextNode;

    @ApiModelProperty("操作类型(1.通过 2.退回 3.拒绝)")
    private Byte operationType;

    @ApiModelProperty("审核意见")
    private String remark;
}