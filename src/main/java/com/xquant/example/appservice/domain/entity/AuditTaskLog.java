package com.xquant.example.appservice.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核任务流转日志实体类
 */
@Data
public class AuditTaskLog implements Serializable {

    private static final long serialVersionUID = 1L;

    // 日志主键ID
    private Long logId;
    // 关联的审核任务ID
    private Long taskId;
    // 审核人
    private String auditor;
    // 审核时间
    private Date auditTime;
    // 当前节点编码
    private String currentNode;
    // 当前节点名称
    private String currentNodeName;
    // 前一个节点编码
    private String previousNode;
    // 下一个节点编码
    private String nextNode;
    // 操作类型(1.通过 2.退回 3.拒绝)
    private String operationType;
    // 审核意见
    private String remark;
}
