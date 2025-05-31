package com.xquant.example.app_service.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 05429
 */
@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单路径
     */
    private String menuPath;

    /**
     * 父菜单编码
     */
    private String parentCode;

    /**
     * 菜单排序
     */
    private Integer menuOrder;

    /**
     * 类型 0-按钮、1-菜单
     */
    private String menuType;

}
