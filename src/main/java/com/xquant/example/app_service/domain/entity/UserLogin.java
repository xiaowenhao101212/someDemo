package com.xquant.example.app_service.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 05429
 */

@Data
public class UserLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    // 登录用户代码
    private String aUserCode;

    // 登录用户密码
    private String aPwd;

    // 密码加密盐
    private String salt;

    // 登录手机MAC地址 默認為1
    private String aMac;
}
