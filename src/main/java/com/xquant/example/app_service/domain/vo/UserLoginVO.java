package com.xquant.example.app_service.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 05429
 */
@Data
@ApiModel("登录认证信息")
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 登录用户代码
    @ApiModelProperty(value = "用户名称", required = true, example = "肖文豪")
    private String userName;

    // 登录用户密码
    @ApiModelProperty(value = "认证码", required = true, example = "123456")
    private String authToken;

    @ApiModelProperty(value = "用户关联菜单列表")
    private List<UserMenuVO> userMenus;
}
