package com.xquant.example.appservice.service;

import com.xquant.example.appservice.domain.dto.UserLoginDTO;
import com.xquant.example.appservice.domain.vo.UserLoginVO;

/**
 * @author 05429
 */
public interface AuthService {

    /**
     * 用户登录接口
     *
     * @param userLoginDTO 用户参数
     * @return 认证结果
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
