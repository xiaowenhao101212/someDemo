package com.xquant.example.appservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum AuditCommitType {
    /**
     * 通过
     */
    ACCEPT("2", "通过"),
    /**
     * 驳回
     */
    ABORT("3", "驳回");

    private final String value;

    private final String description;

    public static AuditCommitType ofValue(String value) {
        for (AuditCommitType item : values()) {
            if (Objects.equals(item.getValue(), value)) {
                return item;
            }
        }
        return null;
    }

}
