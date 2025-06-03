package com.xquant.example.appservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.jayway.jsonpath.JsonPath;
import com.xquant.example.appservice.client.WebApiClient;
import com.xquant.example.appservice.domain.dto.*;
import com.xquant.example.appservice.domain.entity.AuditTask;
import com.xquant.example.appservice.domain.entity.AuditTaskLog;
import com.xquant.example.appservice.domain.page.PageV2VO;
import com.xquant.example.appservice.domain.page.PageVO;
import com.xquant.example.appservice.domain.vo.*;
import com.xquant.example.appservice.enums.AuditCommitType;
import com.xquant.example.appservice.enums.AuditStatus;
import com.xquant.example.appservice.enums.CacheEnum;
import com.xquant.example.appservice.enums.WebApiEnum;
import com.xquant.example.appservice.exceptions.BusinessException;
import com.xquant.example.appservice.mapper.AuditTaskLogMapper;
import com.xquant.example.appservice.mapper.AuditTaskMapper;
import com.xquant.example.appservice.service.AuditTaskService;
import com.xquant.example.appservice.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 05429
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditTaskServiceImpl implements AuditTaskService {

    private final AuditTaskMapper auditTaskMapper;
    private final AuditTaskLogMapper auditTaskLogMapper;
    private final WebApiClient webApiClient;


    @Override
    public PageVO<AuditTaskVO> pageAuditTask(QueryAuditTaskDTO queryAuditTaskDTO) {
        if (Objects.nonNull(queryAuditTaskDTO.getAEnddate())) {
            // 将结束日期设置为当天的23:59:59
            queryAuditTaskDTO.setAEnddate(DateUtil.endOfDay(queryAuditTaskDTO.getAEnddate()));
        }
        try (Page<AuditTaskVO> pageVO = PageMethod.startPage(queryAuditTaskDTO.getPageNum(), queryAuditTaskDTO.getPageSize())) {
            List<AuditTask> dbAuditTasks = auditTaskMapper.list(queryAuditTaskDTO);
            // 转换为 VO 列表
            List<AuditTaskVO> auditTaskVOList = dbAuditTasks.stream()
                    .map(v -> BeanUtil.copyProperties(v, AuditTaskVO.class))
                    .collect(Collectors.toList());
            pageVO.clear();
            pageVO.addAll(auditTaskVOList);
            return PageVO.wrap(pageVO);
        }
    }

    @Override
    public AuditTaskVO get(Long taskId) {
        AuditTask auditTask = auditTaskMapper.get(taskId);
        if (Objects.isNull(auditTask)) {
            throw new BusinessException(-1, "数据不存在");
        }
        return BeanUtil.copyProperties(auditTask, AuditTaskVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditTask(AuditTaskDTO auditTaskDTO) {
        // 用户信息
        UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
        if (Objects.isNull(userLoginVO)) {
            throw new BusinessException(-1, "用户信息不存在");
        }
        // 审批任务信息
        AuditTask auditTask = auditTaskMapper.get(auditTaskDTO.getTaskId());
        if (Objects.isNull(auditTask)) {
            throw new BusinessException(-1, "数据不存在");
        }

        try {
            int lock = auditTaskMapper.lock(auditTaskDTO.getTaskId());
            if (lock == 0) {
                throw new BusinessException(-1, "数据已被其他人审批");
            }
            // 审核人
            auditTask.setApprover(userLoginVO.getUserCode());
            // 如果是通过，需要处理是否下一个节点操作，如果是驳回，直接置为7 结束流程
            if (AuditCommitType.ACCEPT.getValue().equals(auditTaskDTO.getOperationType())) {
                // 这里需要根据实际业务需求处理，目前仅模拟4
                auditTask.setStatus(AuditStatus.AUDITED.getCode());
            } else if (AuditCommitType.ABORT.getValue().equals(auditTaskDTO.getOperationType())) {
                auditTask.setStatus(AuditStatus.SIGNED.getCode());
            }
            if (StrUtil.isNotBlank(auditTaskDTO.getRemark())) {
                auditTask.setRemark(auditTaskDTO.getRemark());
            }
            // 更新审核任务
            auditTaskMapper.update(auditTask);

            // 组装日志表
            AuditTaskLog auditLog = new AuditTaskLog();
            auditLog.setTaskId(auditTaskDTO.getTaskId());
            auditLog.setAuditor(userLoginVO.getUserCode());
            auditLog.setOperationType(auditTaskDTO.getOperationType());
            auditLog.setRemark(auditTaskDTO.getRemark());
            // 审核前节点
            auditLog.setCurrentNode(auditTask.getStatus());
            // 保存审批日志
            auditTaskLogMapper.insert(auditLog);

        } catch (Exception e) {
            log.error("审批异常", e);
            throw new BusinessException(-1, "审批失败");
        } finally {
            auditTaskMapper.unlock(auditTaskDTO.getTaskId());
        }
    }

    @Override
    public List<AuditTaskLogVO> listLog(AuditTaskLogDTO auditTaskLogDTO) {
        if (Objects.nonNull(auditTaskLogDTO.getEndTime())) {
            // 将结束日期设置为当天的23:59:59
            auditTaskLogDTO.setEndTime(DateUtil.endOfDay(auditTaskLogDTO.getEndTime()));
        }
        List<AuditTaskLog> list = auditTaskLogMapper.list(auditTaskLogDTO);
        return list.stream().map(v -> BeanUtil.copyProperties(v, AuditTaskLogVO.class)).collect(Collectors.toList());
    }

    @Override
    public BizVerificationVO bizVerification(BizVerificationDTO bizVerificationDTO) {

        // 阈值 （0,50000]
        AuditTask auditTask = auditTaskMapper.get(bizVerificationDTO.getTaskId());
        if (Objects.isNull(auditTask)) {
            throw new BusinessException(-1, "数据不存在");
        }
        if (StrUtil.isBlank(auditTask.getTransactionAmount())) {
            return BizVerificationVO.builder()
                    .verificationType(bizVerificationDTO.getVerificationTypes())
                    .verificationResult("Y")
                    .msg("通过")
                    .build();
        }
        BigDecimal transactionAmount = new BigDecimal(auditTask.getTransactionAmount());
        if (transactionAmount.compareTo(new BigDecimal("50000")) > 0) {
            return BizVerificationVO.builder()
                    .verificationType(bizVerificationDTO.getVerificationTypes())
                    .verificationResult("N")
                    .msg("交易金额超过阈值")
                    .build();
        }

        return BizVerificationVO.builder()
                .verificationType(bizVerificationDTO.getVerificationTypes())
                .verificationResult("Y")
                .msg("通过")
                .build();

    }

    @Override
    public PageV2VO<AuditTaskPageVO> mobileGetTaskNodeLstByPage(AuditTaskPageDTO queryDTO) {
        // 获取用户代码
        UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
        if (Objects.isNull(userLoginVO)) {
            throw new BusinessException(-1, "用户登录过期");
        }
        // 检查缓存中是否存在数据
        String cacheKey = String.format("user:%s:%s:%s:%s:%s:%s:%s",
                userLoginVO.getAuthToken(),
                queryDTO.getAPage(),
                queryDTO.getATargetType(),
                queryDTO.getABegdate(),
                queryDTO.getAEnddate(),
                queryDTO.getADataType(),
                queryDTO.getAGroupCode());

        cacheKey = MD5.create().digestHex16(cacheKey);

        RMapCache<String, PageV2VO<AuditTaskPageVO>> pageCache = redissonClient.getMapCache(CacheEnum.AUDIT_TASK_PAGE.getCode());
        PageV2VO<AuditTaskPageVO> cachePageDate = pageCache.get(cacheKey);
        if (Objects.nonNull(cachePageDate)) {
            return cachePageDate;
        }

        Map<String, Object> webApiPram = this.buildMobileGetTaskNodeLstByPageParam(queryDTO);
        webApiPram.put("aUserCode", userLoginVO.getUserCode());


        String responseStr = webApiClient.postApi(WebApiEnum.API_MOBILEGETTASKNODELSTBYPAGE.getEndPoint(), webApiPram);

        List<AuditTaskPageVO> dataList = new ArrayList<>();

        if (StrUtil.equals("0", JsonPath.read(responseStr, "$.Data.rt.RE.RM"))) {
            return PageV2VO.of(1, 20, 0, 0, dataList, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }


        List<Map<String, String>> columns = JsonPath.read(responseStr, "$.Data.rt.COLUMN.R");
        List<Map<String, String>> groups = JsonPath.read(responseStr, "$.Data.rt.Group.R");
        List<Map<String, String>> customGroups = JsonPath.read(responseStr, "$.Data.rt.CUSTOMGROUP.R");

        // 总页数
        int pages = Integer.parseInt(JsonPath.read(responseStr, "$.Data.rt.RE.TotalPage"));
        // 查询页数，超过实际数据量
        if (pages < queryDTO.getAPage()) {
            return PageV2VO.of(queryDTO.getAPage(),
                    20,
                    Long.parseLong(JsonPath.read(responseStr, "$.Data.rt.RE.RM")),
                    Integer.parseInt(JsonPath.read(responseStr, "$.Data.rt.RE.TotalPage")),
                    new ArrayList<>(),
                    columns.stream().map(this::mapToColumnInfo).collect(Collectors.toList()),
                    groups.stream().map(this::mapToColumnInfo).collect(Collectors.toList()),
                    customGroups.stream().map(this::mapToColumnInfo).collect(Collectors.toList()));
        }

        List<Map<String, String>> rowMaps = JsonPath.read(responseStr, "$.Data.rt.ROW");

        dataList = rowMaps.stream().map(m -> {
            AuditTaskPageVO rowInfo = new AuditTaskPageVO();
            rowInfo.setTid(m.get("TID"));
            rowInfo.setNid(m.get("NID"));
            rowInfo.setGr(m.get("GR"));
            rowInfo.setId(m.get("ID"));
            rowInfo.setCcroleFlag(StrUtil.equals(m.get("ISCCROLE"), "True") ? "1" : "0");
            rowInfo.setTy(m.get("TY"));
            rowInfo.setCashDirection(m.get("CASHDIRECTION"));
            rowInfo.setOr(m.get("OR"));
            rowInfo.setPartyName(m.get("PARTYNAME"));
            List<PageV2VO.ColumnInfo> columnInfos = new ArrayList<>();
            JSONArray columnJsonArr = JSONArray.parseArray(JSONObject.toJSONString(m.get("R")));
            columnJsonArr.forEach(obj -> {
                JSONObject json = (JSONObject) obj;
                PageV2VO.ColumnInfo columnInfo = new PageV2VO.ColumnInfo();
                columnInfo.setCode(json.getString("NAME"));
                columnInfo.setName(json.getString("#text"));
                columnInfos.add(columnInfo);
            });
            rowInfo.setColumnInfos(columnInfos);
            return rowInfo;
        }).collect(Collectors.toList());

        return PageV2VO.of(queryDTO.getAPage(),
                20,
                Long.parseLong(JsonPath.read(responseStr, "$.Data.rt.RE.RM")),
                Integer.parseInt(JsonPath.read(responseStr, "$.Data.rt.RE.TotalPage")),
                dataList,
                columns.stream().map(this::mapToColumnInfo).collect(Collectors.toList()),
                groups.stream().map(this::mapToColumnInfo).collect(Collectors.toList()),
                customGroups.stream().map(this::mapToColumnInfo).collect(Collectors.toList()));
    }

    @Override
    public AuditTaskNodeVO mobileSingleTaskNodeTree(AuditTaskNodeDTO queryDTO) {
        // 获取用户代码
        UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
        if (Objects.isNull(userLoginVO)) {
            throw new BusinessException(-1, "用户登录过期");
        }
        Map<String, Object> webApiParam = new HashMap<>();
        webApiParam.put("aUserCode", userLoginVO.getUserCode());
        webApiParam.put("aTaskId", queryDTO.getTid());
        String responseStr = webApiClient.postApi(WebApiEnum.API_MOBILESINGLETASKNODETREE.getEndPoint(), webApiParam);

        // 提取根T节点
        Map<String, Object> rootT = JsonPath.read(responseStr, "$.Data.rt.T");
        return this.convertNode(rootT);
    }

    @Override
    public List<AuditTaskLogV2VO> mobileSingleTraceLog(AuditTaskLogV2DTO queryDTO) {
        // 获取用户代码
        UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
        if (Objects.isNull(userLoginVO)) {
            throw new BusinessException(-1, "用户登录过期");
        }
        Map<String, Object> webApiParam = new HashMap<>();
        webApiParam.put("aUserCode", userLoginVO.getUserCode());
        webApiParam.put("aTaskId", queryDTO.getTid());
        String responseStr = webApiClient.postApi(WebApiEnum.API_MOBILESINGLETRACELOG.getEndPoint(), webApiParam);

        List<Map<String, String>> responseList = JsonPath.read(responseStr, "$.Data.rt.TL");
        return responseList.stream().map(m -> {
            AuditTaskLogV2VO vo = new AuditTaskLogV2VO();
            vo.setTi(m.get("TI"));
            vo.setLy(m.get("LY"));
            vo.setNa(m.get("NA"));
            vo.setOn(m.get("ON"));
            vo.setRn(m.get("RN"));
            vo.setSn(m.get("SN"));
            vo.setAn(m.get("AN"));
            vo.setOt(m.get("OT"));
            vo.setOe(m.get("OE"));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<BizValidVO> mobileSingleLimitresult(BizValidDTO queryDTO) {
        // 获取用户代码
        UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
        if (Objects.isNull(userLoginVO)) {
            throw new BusinessException(-1, "用户登录过期");
        }
        Map<String, Object> webApiParam = new HashMap<>();
        webApiParam.put("aUserCode", userLoginVO.getUserCode());
        webApiParam.put("aTaskId", queryDTO.getATaskId());
        webApiParam.put("aIsIncludeConfirmed", queryDTO.getAIsIncludeConfirmed());
        webApiParam.put("aIsIncludeOrdered", queryDTO.getAIsIncludeOrdered());
        String responseStr = webApiClient.postApi(WebApiEnum.API_MOBILESINGLELIMITRESULT.getEndPoint(), webApiParam);
        List<Map<String, String>> responseList = JsonPath.read(responseStr, "$.Data.rt.L");
        return responseList.stream().map(m -> {
            BizValidVO vo = new BizValidVO();
            vo.setNa(m.get("NA"));
            vo.setTy(m.get("TY"));
            vo.setBf(m.get("BF"));
            vo.setAf(m.get("AF"));
            vo.setDf(m.get("DF"));
            vo.setCa(m.get("CA"));
            vo.setLe(m.get("LE"));
            vo.setSid(m.get("SID"));
            vo.setMe(m.get("ME"));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void mobileApproveTaskByNodeIdBatch(ApprovedBatchDTO approvedBatchDTO) {
        // 获取用户代码
        UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
        if (Objects.isNull(userLoginVO)) {
            throw new BusinessException(-1, "用户登录过期");
        }
        Map<String, Object> webApiParam = new HashMap<>();
        webApiParam.put("aUserCode", userLoginVO.getUserCode());
        webApiParam.put("aNodeIds", approvedBatchDTO.getANodeIds());
        webApiParam.put("targetType", approvedBatchDTO.getTargetType());
        webApiParam.put("aAction", approvedBatchDTO.getAAction());
        webApiParam.put("actionNote", approvedBatchDTO.getActionNote());
        webApiParam.put("aCCRoles", approvedBatchDTO.getACCRoles());
        webApiParam.put("aDataTypeCurrent", approvedBatchDTO.getADataTypeCurrent());

        webApiClient.postApi(WebApiEnum.API_MOBILEAPPROVETASKBYNODEID_BATCH.getEndPoint(), webApiParam);
    }

    @Override
    public List<TaskNodeGroupVO> mobileGetTaskNodeGroup(TaskNodeGroupDTO queryDTO) {
        // 获取用户代码
        UserLoginVO userLoginVO = CookieUtil.getUserInfoFromCookie();
        if (Objects.isNull(userLoginVO)) {
            throw new BusinessException(-1, "用户登录过期");
        }
        Map<String, Object> webApiPram = new HashMap<>();
        webApiPram.put("aUserCode", userLoginVO.getUserCode());
        Optional.ofNullable(queryDTO.getATargetType()).ifPresent(v -> webApiPram.put("aTargetType", v));
        Optional.ofNullable(queryDTO.getADataType()).ifPresent(v -> webApiPram.put("aDataType", v));
        webApiPram.put("aBegdate", queryDTO.getABegdate());
        webApiPram.put("aEnddate", queryDTO.getAEnddate());
        // 开始日期默认是当前日期往前14天;结束日期默认是当期日期往后1天
        if (StrUtil.hasBlank(queryDTO.getABegdate(), queryDTO.getAEnddate())) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (StrUtil.isBlank(queryDTO.getABegdate())) {
                webApiPram.put("aBegdate", today.minusDays(14).format(formatter));
            }
            if (StrUtil.isBlank(queryDTO.getAEnddate())) {
                webApiPram.put("aEnddate", today.plusDays(1).format(formatter));
            }
        }
        String responseStr = webApiClient.postApi(WebApiEnum.API_MOBILEGETTASKNODEGROUP.getEndPoint(), webApiPram);

        List<Map<String, String>> responseList = JsonPath.read(responseStr, "$.Data.rt.G");

        return responseList.stream().map(m -> {
            TaskNodeGroupVO vo = new TaskNodeGroupVO();
            vo.setC(m.get("C"));
            vo.setP(m.get("P"));
            vo.setCt(m.get("CT"));
            vo.setM(m.get("M"));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 递归转换单个节点
     */
    private AuditTaskNodeVO convertNode(Map<String, Object> nodeMap) {

        AuditTaskNodeVO node = new AuditTaskNodeVO();
        node.setId((String) nodeMap.get("ID"));
        node.setTx((String) nodeMap.get("TX"));
        node.setVa((String) nodeMap.get("VA"));
        node.setIn((String) nodeMap.get("IN"));
        node.setTy((String) nodeMap.get("TY"));
        node.setLe((String) nodeMap.get("LE"));
        node.setLc((String) nodeMap.get("IC"));

        // 处理子节点
        Object children = nodeMap.get("T");
        if (children != null) {
            if (children instanceof List) {
                // 多个子节点的情况
                List<Map<String, Object>> childList = (List<Map<String, Object>>) children;
                List<AuditTaskNodeVO> childNodes = new ArrayList<>();
                for (Map<String, Object> childMap : childList) {
                    childNodes.add(convertNode(childMap));
                }
                node.setChildNodes(childNodes);
            } else if (children instanceof Map) {
                // 单个子节点的情况
                Map<String, Object> childMap = (Map<String, Object>) children;
                List<AuditTaskNodeVO> childNodes = new ArrayList<>();
                childNodes.add(convertNode(childMap));
                node.setChildNodes(childNodes);
            }
        }

        return node;
    }

    /**
     * 组装审批数据分页查询条件
     *
     * @param queryDTO
     * @return
     */
    private Map<String, Object> buildMobileGetTaskNodeLstByPageParam(AuditTaskPageDTO queryDTO) {
        Map<String, Object> webApiPram = new HashMap<>();
        webApiPram.put("aPage", queryDTO.getAPage());
        Optional.ofNullable(queryDTO.getATargetType()).ifPresent(v -> webApiPram.put("aTargetType", v));
        Optional.ofNullable(queryDTO.getADataType()).ifPresent(v -> webApiPram.put("aDataType", v));
        Optional.ofNullable(queryDTO.getAGroupCode()).ifPresent(v -> webApiPram.put("aGroupCode", v));
        webApiPram.put("aBegdate", queryDTO.getABegdate());
        webApiPram.put("aEnddate", queryDTO.getAEnddate());
        // 开始日期默认是当前日期往前14天;结束日期默认是当期日期往后1天
        if (StrUtil.hasBlank(queryDTO.getABegdate(), queryDTO.getAEnddate())) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (StrUtil.isBlank(queryDTO.getABegdate())) {
                webApiPram.put("aBegdate", today.minusDays(14).format(formatter));
            }
            if (StrUtil.isBlank(queryDTO.getAEnddate())) {
                webApiPram.put("aEnddate", today.plusDays(1).format(formatter));
            }
        }
        return webApiPram;
    }

    /**
     * 将单个Map转换为ColumnInfo对象
     */
    private PageV2VO.ColumnInfo mapToColumnInfo(Map<String, String> columnMap) {
        PageV2VO.ColumnInfo columnInfo = new PageV2VO.ColumnInfo();
        columnInfo.setCode(columnMap.get("NAME"));
        columnInfo.setName(columnMap.get("#text"));
        return columnInfo;
    }

}
