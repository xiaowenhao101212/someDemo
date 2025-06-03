package com.xquant.example.appservice.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author 05429
 */

@Data
@ApiModel("管理登录 DTO")
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 登录用户代码
    @ApiModelProperty(value = "登录用户代码", required = true, example = "Apptest")
    @NotBlank(message = "登陆账号不能为空")
    @Length(min = 3, max = 16, message = "账号长度为 3-16 位")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    private String aUserCode;

    // 登录用户密码
    @ApiModelProperty(value = "登录用户密码", required = true, example = "123456")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 3, max = 16, message = "密码长度为 3-16 位")
    private String aPwd;

    // 登录手机MAC地址 默認為1
    @ApiModelProperty(value = "登录手机MAC地址", required = true, example = "1")
    private String aMac;
}
