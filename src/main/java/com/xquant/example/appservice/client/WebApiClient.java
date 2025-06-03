package com.xquant.example.appservice.client;

import cn.hutool.core.util.StrUtil;
import com.jayway.jsonpath.JsonPath;
import com.xquant.example.appservice.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * @author 05429
 */
@Slf4j
public class WebApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public WebApiClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /**
     * 调用 WebAPI POST 接口
     *
     * @param endpoint    接口端点，如 "/API/service/MobileLogon"
     * @param requestBody 请求体 Map
     * @return ResponseEntity<String> 响应实体
     */
    public String postApi(String endpoint, Map<String, Object> requestBody) {
        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 可以在这里添加通用请求头，如认证token等
            // headers.set("Authorization", "Bearer xxxx");

            // 创建请求实体
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 构建完整URL
            String fullUrl = baseUrl + endpoint;
            log.info("Calling API: {}, Request: {}", fullUrl, requestBody);
            // 发送POST请求
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, requestEntity, String.class);
            log.info("API Response: {}", response.getBody());

            if (Objects.isNull(response.getBody())
                    || Objects.isNull(JsonPath.read(response.getBody(), "$.Data"))
                    || Objects.isNull(JsonPath.read(response.getBody(), "$.Data.rt"))
                    || Objects.isNull(JsonPath.read(response.getBody(), "$.Data.rt.RE"))) {
                throw new BusinessException(-1, String.format("未接受到接口[%s]任何响应信息", fullUrl));
            }
            int webResponseStatus = JsonPath.read(response.getBody(), "$.Status_code");
            if (webResponseStatus != 1) {
                throw new BusinessException(-1, String.format("服务[%s]响应码[%s]异常，msg:[%s]", fullUrl, webResponseStatus, JsonPath.read(response.getBody(), "$.Msg")));
            }
            String status = JsonPath.read(response.getBody(), "$.Data.rt.RE.RC");
            if (!StrUtil.equals(status, "0")) {
                throw new BusinessException(-1, String.format("接口[%s]响应码[%s]异常，msg:[%s]", fullUrl, status, JsonPath.read(response.getBody(), "$.Data.rt.RE.RM")));
            }

            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Client error when calling API: {}, Status: {}, Response: {}",
                    endpoint, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("API调用客户端错误: " + e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            log.error("Server error when calling API: {}, Status: {}, Response: {}",
                    endpoint, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("API服务端错误: " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            log.error("Error calling API: {}", endpoint, e);
            throw new RuntimeException("API调用失败: " + e.getMessage(), e);
        }
    }

    /**
     * 调用 WebAPI POST 接口（带自定义请求头）
     *
     * @param endpoint      接口端点
     * @param requestBody   请求体
     * @param customHeaders 自定义请求头
     * @return ResponseEntity<String> 响应实体
     */
    public ResponseEntity<String> postApi(String endpoint,
                                          Map<String, Object> requestBody,
                                          HttpHeaders customHeaders) {
        // 合并基础头和自定义头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (customHeaders != null) {
            headers.putAll(customHeaders);
        }

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        String fullUrl = baseUrl + endpoint;

        return restTemplate.postForEntity(fullUrl, requestEntity, String.class);
    }
}
