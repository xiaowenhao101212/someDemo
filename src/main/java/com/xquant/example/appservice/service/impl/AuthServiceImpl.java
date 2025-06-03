package com.xquant.example.appservice.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.jayway.jsonpath.JsonPath;
import com.xquant.example.appservice.client.WebApiClient;
import com.xquant.example.appservice.domain.dto.UserLoginDTO;
import com.xquant.example.appservice.domain.vo.UserLoginVO;
import com.xquant.example.appservice.domain.vo.UserMenuVO;
import com.xquant.example.appservice.enums.CacheEnum;
import com.xquant.example.appservice.enums.WebApiEnum;
import com.xquant.example.appservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author 05429
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedissonClient redissonClient;
    private final WebApiClient webApiClient;


    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 調用webapi 接口 MobileLogon 處理登錄認證
        Map<String, Object> userLoginMap = new HashMap<>();
        userLoginMap.put("aUserCode", userLoginDTO.getAUserCode());
        userLoginMap.put("aPwd", userLoginDTO.getAPwd());
        userLoginMap.put("aMAC", userLoginDTO.getAMac());

        String responseStr = webApiClient.postApi(WebApiEnum.API_MOBILELOGON.getEndPoint(), userLoginMap);

        // 构建出参对象
        UserLoginVO vo = new UserLoginVO();
        vo.setUserCode(userLoginDTO.getAUserCode());
        vo.setUserName(JsonPath.read(responseStr, "$.Data.rt.RE.RM"));
        String authToken = JsonPath.read(responseStr, "$.Data.rt.RE.KEY");
        if (StrUtil.isBlank(authToken)) {
            authToken = UUID.fastUUID().toString(true);
        }
        vo.setAuthToken(authToken);

        // 处理菜单
        List<Map<String, String>> originalMenus = JsonPath.read(responseStr, "$.Data.rt.M");

        if (!CollectionUtils.isEmpty(originalMenus)) {
            List<UserMenuVO> menus = originalMenus.stream().filter(MapUtil::isNotEmpty).map(v -> {
                UserMenuVO menu = new UserMenuVO();
                menu.setMenuCode(v.get("C"));
                menu.setMenuName(v.get("N"));
                menu.setMenuPath(v.get("URL"));
                menu.setMenuRight(StrUtil.equalsIgnoreCase(v.get("I"), "True") ? "1" : "0");
                return menu;
            }).collect(Collectors.toList());

            vo.setUserMenus(menus);
        }
//        // 数据库是否存在该用户
//        UserLogin dbUser = userLoginMapper.selectByUserCode(userLoginDTO.getAUserCode());
//        if (Objects.isNull(dbUser)) {
//            throw new BusinessException(-1, "账号不存在");
//        }
//
//        // 校验密码是否正确，可以补充错误次数上限
//        String encodedPassword = BCrypt.hashpw(userLoginDTO.getAPwd(), dbUser.getSalt());
//        log.info("用户[{}]计算后密码为[{}],入库密码为[{}]", userLoginDTO.getAUserCode(), encodedPassword, dbUser.getAPwd());
//        if (!StrUtil.equals(encodedPassword, dbUser.getAPwd())) {
//            throw new BusinessException(-1, "密码不正确");
//        }


//        vo.setUserName(dbUser.getUserName());
//        vo.setUserCode(userLoginDTO.getAUserCode());
        // 用一个随机的uuid 代替认证token
//        vo.setAuthToken(UUID.fastUUID().toString(true));

//        // 权限、菜单处理
//        List<Menu> menus = menuMapper.listAll();
//        if (!CollectionUtils.isEmpty(menus)) {
//            // 查询用户有权限的菜单列表
//            List<String> userPermissionMenus = menuMapper.listAllPermissionMenu(userLoginDTO.getAUserCode());
//            vo.setUserMenus(menus.stream().map(v -> {
//                UserMenuVO userMenuVO = BeanUtil.copyProperties(v, UserMenuVO.class);
//                userMenuVO.setMenuRight("0");
//                if (userPermissionMenus.contains(userMenuVO.getMenuCode())) {
//                    userMenuVO.setMenuRight("1");
//                }
//                return userMenuVO;
//            }).collect(Collectors.toList()));
//        }

        // 用户信息缓存
        RMapCache<String, UserLoginVO> userCache = redissonClient.getMapCache(CacheEnum.USER_SESSION.getCode());
        userCache.put(vo.getAuthToken(), vo, 30, TimeUnit.MINUTES);
        log.info("用户[{}]认证信息缓存key:[{}]", vo.getUserName(), vo.getAuthToken());
        return vo;
    }
}
