package com.xquant.example.app_service.mapper;

import com.xquant.example.app_service.domain.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 05429
 */
@Mapper
public interface MenuMapper {

    /**
     * 仅包含状态为有效的菜单
     *
     * @return 菜单集合
     */
    List<Menu> listAll();

    /**
     * 查询用户拥有权限的菜单列表
     * @param userAccount 用户账号
     * @return 菜单列表
     */
    List<String> listAllPermissionMenu(String userAccount);
}
