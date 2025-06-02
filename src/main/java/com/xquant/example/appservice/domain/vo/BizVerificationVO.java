package com.xquant.example.appservice.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BizVerificationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 验证类型
    @ApiModelProperty ("验证类型")
    private String  verificationType;

    // 验证结果
    @ApiModelProperty ("验证结果")
    private String verificationResult;

    @ApiModelProperty("验证结果信息")
    private String msg;

}
