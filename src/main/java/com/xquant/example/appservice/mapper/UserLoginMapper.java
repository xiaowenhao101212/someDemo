package com.xquant.example.appservice.mapper;

import com.xquant.example.appservice.domain.entity.UserLogin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 05429
 */
@Mapper
public interface UserLoginMapper {

    // 根据用户代码查询
    UserLogin selectByUserCode(@Param("aUserCode") String aUserCode);
}
