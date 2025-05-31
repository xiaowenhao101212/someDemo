package com.xquant.example.app_service.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.message.Message;

import java.io.Serializable;
import java.util.Date;

@Data
public class AuditTaskVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键
    @ApiModelProperty(value = "主键")
    private Long id;
    // 审批编号
    @ApiModelProperty(value = "审批编号")
    private String tid;
    // 节点编号
    @ApiModelProperty(value = "节点编号")
    private String nid;
    // 交易编号
    @ApiModelProperty(value = "交易编号")
    private String transactionId;
    // 交易方向
    @ApiModelProperty(value = "交易方向")
    private String transactionType;
    // 交易日期
    @ApiModelProperty(value = "交易日期")
    private Date transactionDate;
    // 资金流向
    @ApiModelProperty(value = "资金流向")
    private String cashDirection;
    // 交易对手
    @ApiModelProperty(value = "交易对手")
    private String counterparty;
    // 内证编号
    @ApiModelProperty(value = "内证编号")
    private String internalCertificate;
    // 交易市场
    @ApiModelProperty(value = "交易市场")
    private String market;
    // 债券代码
    @ApiModelProperty(value = "债券代码")
    private String bondCode;
    // 债券名称
    @ApiModelProperty(value = "债券名称")
    private String bondName;
    // 审批状态
    @ApiModelProperty(value = "审批状态")
    private String status;
    // 创建时间
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    // 更新时间
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    // 创建人
    @ApiModelProperty(value = "创建人")
    private String creator;
    // 审批人
    @ApiModelProperty(value = "审批人")
    private String approver;
    // 审批时间
    @ApiModelProperty(value = "审批时间")
    private Date approvalTime;
    // 备注
    @ApiModelProperty(value = "备注")
    private String remark;

}
