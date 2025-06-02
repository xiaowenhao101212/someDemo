package com.xquant.example.appservice.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据审批状态
 *
 * @author 05429
 */
@Getter
@RequiredArgsConstructor
public enum AuditStatus {

    /**
     * 待审批
     */
    PENDING("0", "待审批"),

    /**
     * 待会签
     */
    WAITING_COUNTERSIGN("1", "待会签"),

    /**
     * 提前审批
     */
    EARLY_APPROVAL("2", "提前审批"),

    /**
     * 补审批
     */
    SUPPLEMENTARY_APPROVAL("3", "补审批"),

    /**
     * 已审核
     */
    AUDITED("4", "已审核"),

    /**
     * 已会签
     */
    COUNTERSIGNED("5", "已会签"),

    /**
     * 等待签核
     */
    WAITING_SIGN("6", "等待签核"),

    /**
     * 已签核
     */
    SIGNED("7", "已签核"),

    /**
     * 我的提交
     */
    MY_SUBMISSION("9", "我的提交");

    private final String code;

    private final String description;

    /**
     * 根据状态码获取枚举实例
     */
    public static AuditStatus getByCode(String code) {
        for (AuditStatus status : values()) {
            if (StrUtil.equals(status.code, code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的审批状态码: " + code);
    }

    /**
     * 根据状态码获取描述
     */
    public static String getDescriptionByCode(String code) {
        return getByCode(code).getDescription();
    }
}
