package com.xquant.example.appservice.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 05429
 */
@Data
public class AuditTask implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键
    private Long id;
    // 审批编号
    private String tid;
    // 节点编号
    private String nid;
    // 交易编号
    private String transactionId;
    // 交易方向
    private String transactionType;
    // 交易日期
    private Date transactionDate;
    // 资金流向
    private String cashDirection;
    // 交易对手
    private String counterparty;
    // 内证编号
    private String internalCertificate;
    // 交易市场
    private String market;
    // 债券代码
    private String bondCode;
    // 债券名称
    private String bondName;
    // 审批状态
    private String status;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
    // 创建人
    private String creator;
    // 审批人
    private String approver;
    // 审批时间
    private Date approvalTime;
    // 备注
    private String remark;

}
