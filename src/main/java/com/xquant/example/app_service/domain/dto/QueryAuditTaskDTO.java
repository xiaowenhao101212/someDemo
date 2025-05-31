package com.xquant.example.app_service.domain.dto;

import com.xquant.example.app_service.domain.page.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author 05429
 */
@Data
@ApiModel("查询审批任务条件")
@EqualsAndHashCode(callSuper = true)
public class QueryAuditTaskDTO extends PageParam{

    @NotBlank(message = "用户未登录")
    @ApiModelProperty(value = "用户登录token", example = "123456")
    private String authToken;

    @ApiModelProperty(value = "起始日期",  example = "123456")
    private String aBegdate;

    @ApiModelProperty(value = "结束日期",  example = "1")
    private String aEnddate;

    @ApiModelProperty(value = "数据类型",  example = "1")
    private String aDataType;

    @ApiModelProperty(value = "审批单分组编号",  example = "1")
    private String aGroupCode;
}
