package com.xquant.example.appservice.domain.vo;

import com.xquant.example.appservice.domain.page.PageV2VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 05429
 */
@Data
@ApiModel("单条审核任务详细信息")
public class AuditTaskDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审批编号")
    private String tid;

    @ApiModelProperty(value = "审批节点编号")
    private String nid;

    @ApiModelProperty(value = "审批角色编号，审批用户")
    private String gr;

    @ApiModelProperty(value = "交易编号")
    private String id;

    @ApiModelProperty(value = "是否会签角色 0-正常角色、1-会签角色")
    private String ccroleFlag;

    @ApiModelProperty(value = "交易方向")
    private String ty;

    @ApiModelProperty(value = "资金流向")
    private String cashDirection;

    @ApiModelProperty(value = "交易日期")
    private String or;

    @ApiModelProperty(value = "交易对手")
    private String partyName;

    @ApiModelProperty(value = "列信息")
    private List<PageV2VO.ColumnInfo> columnInfos;

}
