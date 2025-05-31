package com.xquant.example.app_service.controller;


import com.xquant.example.app_service.domain.dto.UserLoginDTO;
import com.xquant.example.app_service.domain.vo.UserLoginVO;
import com.xquant.example.app_service.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 05429
 */
@Api(tags = "登录认证相关API")
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation("账号密码登陆")
    @PostMapping("/mobileLogon")
    public ResponseEntity<UserLoginVO> mobileLogon(@RequestBody @Validated UserLoginDTO userLoginDTO) {

        return ResponseEntity.ok(authService.login(userLoginDTO));
    }

}
