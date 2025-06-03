package com.xquant.example.appservice.client;

import lombok.Data;

import java.util.Map;

@Data
public class ApiResponse<T> {
    // 结果描述
    private String Msg;
    // 响应状态码
    private Integer Status_code;
    // 响应体
    private T Data;

    // 判断是否成功
    public boolean isSuccess() {
        return Status_code != null && Status_code == 1;
    }

    // 获取业务错误码
    public String getBusinessErrorCode() {
        if (Data instanceof Map) {
            Map<?, ?> dataMap = (Map<?, ?>) Data;
            if (dataMap.containsKey("rt")) {
                Map<?, ?> rtMap = (Map<?, ?>) dataMap.get("rt");
                if (rtMap.containsKey("RE")) {
                    Map<?, ?> reMap = (Map<?, ?>) rtMap.get("RE");
                    return (String) reMap.get("RC");
                }
            }
        }
        return null;
    }

    // 获取业务错误信息
    public String getBusinessErrorMessage() {
        if (Data instanceof Map) {
            Map<?, ?> dataMap = (Map<?, ?>) Data;
            if (dataMap.containsKey("rt")) {
                Map<?, ?> rtMap = (Map<?, ?>) dataMap.get("rt");
                if (rtMap.containsKey("RE")) {
                    Map<?, ?> reMap = (Map<?, ?>) rtMap.get("RE");
                    return (String) reMap.get("RM");
                }
            }
        }
        return null;
    }
}
