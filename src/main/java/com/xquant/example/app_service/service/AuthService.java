package com.xquant.example.app_service.service;

import com.xquant.example.app_service.domain.dto.UserLoginDTO;
import com.xquant.example.app_service.domain.vo.UserLoginVO;

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
