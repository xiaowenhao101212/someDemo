package com.xquant.example.appservice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BizVerificationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "验证类型未传")
    @ApiModelProperty(value = "验证类型", required = true)
    private String verificationTypes;

    @NotNull(message = "任务id未传")
    @ApiModelProperty("任务id")
    private Long taskId;

}
