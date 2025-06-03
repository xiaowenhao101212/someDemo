package com.xquant.example.appservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum WebApiEnum {
    /**
     * 登录接口
     */
    API_MOBILELOGON("/MobileLogon", "登录接口", "post"),
    /**
     * 分页查询审批数据列表信息
     */
    API_MOBILEGETTASKNODELSTBYPAGE("/MobileGetTaskNodeLstByPage", "分页查询审批数据列表信息", "post"),
    /**
     * 查询单笔审批单审批流程图
     */
    API_MOBILESINGLETASKNODETREE("/MobileSingleTaskNodeTree", "查询单笔审批单审批流程图", "post"),
    /**
     * 查询审批日志
     */
    API_MOBILESINGLETRACELOG("/MobileSingleTraceLog", "查询审批日志", "post"),
    /**
     * 查询限额检查结果
     */
    API_MOBILESINGLELIMITRESULT("/MobileSingleLimitresult", "查询限额检查结果", "post"),
    /**
     * 批量审批操作
     */
    API_MOBILEAPPROVETASKBYNODEID_BATCH("/MobileApproveTaskByNodeId_Batch", "批量审批操作", "post"),
    /**
     * 查询审批数据分组信息
     */
    API_MOBILEGETTASKNODEGROUP("/MobileGetTaskNodeGroup", "查询审批数据分组信息", "post");

    private final String endPoint;

    private final String description;

    private final String method;

}
