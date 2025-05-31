package com.xquant.example.app_service.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 05429
 */
@Data
@ApiModel("用户关联菜单信息")
public class UserMenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单代码")
    private String menuCode;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单路径")
    private String menuPath;

    @ApiModelProperty(value = "父菜单编码")
    private String parentCode;

    @ApiModelProperty(value = "菜单排序")
    private Integer menuOrder;

    @ApiModelProperty(value = "菜单权限 0-无、1-有")
    private String menuRight;
}
