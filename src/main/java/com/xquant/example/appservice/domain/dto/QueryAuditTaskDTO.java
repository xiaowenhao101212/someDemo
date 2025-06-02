package com.xquant.example.appservice.domain.dto;

import com.xquant.example.appservice.domain.page.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author 05429
 */
@Data
@ApiModel("查询审批任务条件")
@EqualsAndHashCode(callSuper = false)
public class QueryAuditTaskDTO extends PageParam{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "起始日期",  example = "2025-05-31")
    private Date aBegdate;

    @ApiModelProperty(value = "结束日期",  example = "2025-05-31")
    private Date aEnddate;

    @ApiModelProperty(value = "数据类型",  example = "1")
    private String aDataType;

    @ApiModelProperty(value = "审批单分组编号",  example = "1")
    private String aGroupCode;
}
