CREATE TABLE t_audit_task_log (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志主键ID',
    task_id BIGINT NOT NULL COMMENT '关联的审核任务ID',
    auditor VARCHAR(100) NOT NULL COMMENT '审核人',
    audit_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
    current_node VARCHAR(50) NOT NULL COMMENT '当前节点编码',
    current_node_name VARCHAR(100) NOT NULL COMMENT '当前节点名称',
    previous_node VARCHAR(50) COMMENT '前一个节点编码',
    next_node VARCHAR(50) COMMENT '下一个节点编码',
    operation_type TINYINT NOT NULL COMMENT '操作类型(1.通过 2.退回 3.拒绝)',
    remark VARCHAR(500) COMMENT '审核意见',
    INDEX idx_task_id (task_id),
    INDEX idx_audit_time (audit_time),
    INDEX idx_current_node (current_node)
) COMMENT '审核任务流转日志表';