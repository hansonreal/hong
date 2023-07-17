-- 系统操作日志表
DROP TABLE IF EXISTS `t_sys_log`;
CREATE TABLE `t_sys_log`
(
    sys_log_id    bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '系统日志标识',
    sys_user_id   bigint(20)            DEFAULT NULL COMMENT '系统用户标识',
    login_name    VARCHAR(32)           DEFAULT NULL COMMENT '用户姓名',
    user_ip       VARCHAR(128) NOT NULL DEFAULT '' COMMENT '用户IP',
    method_name   VARCHAR(128) NOT NULL DEFAULT '' COMMENT '方法名',
    method_desc   VARCHAR(128) NOT NULL DEFAULT '' COMMENT '方法描述',
    req_url       VARCHAR(256) NOT NULL DEFAULT '' COMMENT '请求地址',
    opt_req_info  TEXT                  DEFAULT NULL COMMENT '操作请求参数',
    opt_res_info  TEXT                  DEFAULT NULL COMMENT '操作响应结果',
    duration_time BIGINT(20)   NOT NULL DEFAULT 0 COMMENT '方法执行持续时间，单位毫秒',
    created_at    TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    PRIMARY KEY (`sys_log_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '系统操作日志表';

-- 系统用户表
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`
(
    sys_user_id BIGINT(20)                                NOT NULL AUTO_INCREMENT COMMENT '系统用户ID',
    login_name  VARCHAR(32)                               NOT NULL COMMENT '登录用户名',
    email       VARCHAR(128)                              NULL COMMENT '电子邮箱',
    tel_phone   VARCHAR(32)                               NOT NULL COMMENT '手机号',
    sex         TINYINT(6)   DEFAULT 0                    NOT NULL COMMENT '性别 0-未知, 1-男, 2-女',
    avatar_url  VARCHAR(128)                              NULL COMMENT '头像地址',
    admin_flag  TINYINT(6)   DEFAULT 0                    NOT NULL COMMENT '是否超管（超管拥有全部权限） 0-否 1-是',
    user_state  TINYINT(6)   DEFAULT 0                    NOT NULL COMMENT '状态 0-停用 1-启用',
    created_at  TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL COMMENT '创建时间',
    updated_at  TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
    PRIMARY KEY (`sys_user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  charset = utf8mb4
    comment '系统用户表';


-- 系统用户认证表
DROP TABLE IF EXISTS `t_sys_user_auth`;
CREATE TABLE `t_sys_user_auth`
(
    sys_auth_id   BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '系统用户认证标识',
    sys_user_id   BIGINT(20)   NOT NULL COMMENT '系统用户标识',
    identity_type TINYINT(6)   NOT NULL DEFAULT '0' COMMENT '登录类型  1-登录账号 2-手机号 3-邮箱  10-微信  11-QQ 12-支付宝 13-微博',
    identifier    VARCHAR(128) NOT NULL COMMENT '认证标识 ( 用户名 | open_id )',
    credential    VARCHAR(128) NOT NULL COMMENT '密码凭证',
    salt          VARCHAR(128) NOT NULL COMMENT 'salt',
    created_at    TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    updated_at    TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
    PRIMARY KEY (`sys_auth_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户认证表';

-- 系统配置表
DROP TABLE IF EXISTS sys_config;
CREATE TABLE `t_sys_config`
(
    config_key  VARCHAR(50)  NOT NULL COMMENT '配置KEY',
    config_name VARCHAR(50)  NOT NULL COMMENT '配置名称',
    config_desc VARCHAR(200) NOT NULL COMMENT '描述信息',
    group_key   VARCHAR(50)  NOT NULL COMMENT '分组key',
    group_name  VARCHAR(50)  NOT NULL COMMENT '分组名称',
    config_val  TEXT         NOT NULL COMMENT '配置内容项',
    type        VARCHAR(20)  NOT NULL DEFAULT 'text' COMMENT '类型: text-输入框, textarea-多行文本, uploadImg-上传图片, switch-开关',
    sort_num    BIGINT(20)   NOT NULL DEFAULT 0 COMMENT '显示顺序',
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    updated_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
    PRIMARY KEY (`config_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统配置表';

-- 系统消息表
DROP TABLE IF EXISTS `t_sys_msg`;
CREATE TABLE `t_sys_msg`
(
    sys_msg_id BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '消息标识',
    msg_state  TINYINT(6)   NOT NULL DEFAULT 0 COMMENT '消息状态:0等待消费 1已消费 2已死亡，默认值0',
    msg_body   VARCHAR(512) NOT NULL COMMENT '消息内容',
    queue_name VARCHAR(128) COMMENT '队列名称',
    consumer   VARCHAR(64)  NOT NULL COMMENT '消费系统',
    consume_at TIMESTAMP(6) COMMENT '消费时间',
    die_count  INT          NOT NULL DEFAULT 10 COMMENT '死亡次数条件,由使用方决定,默认为发送10次还没被消费则标记死亡，人工介入',
    die_at     TIMESTAMP(6) COMMENT '死亡时间',
    send_count INT          NOT NULL DEFAULT 0 COMMENT '重复发送消息次数，默认为0',
    sent_at    TIMESTAMP(6) COMMENT '最近发送消息时间',
    producer   VARCHAR(64)  NOT NULL COMMENT '消息生产者',
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
    PRIMARY KEY (`sys_msg_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8mb4 COMMENT '消息表';

-- 系统字典表
DROP TABLE IF EXISTS `t_sys_dict`;
CREATE TABLE `t_sys_dict`
(
    sys_dict_id BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '字典标识',
    dict_name   VARCHAR(100) NOT NULL COMMENT '字典名称',
    dict_code   VARCHAR(100) NOT NULL COMMENT '字典编码',
    dict_desc   VARCHAR(255)          DEFAULT NULL COMMENT '字典描述',
    del_flag    TINYINT(6)   NOT NULL DEFAULT '0' COMMENT '删除状态 0-未删除, 1-已删除',
    create_by   VARCHAR(32)           DEFAULT NULL COMMENT '创建人',
    update_by   VARCHAR(32)           DEFAULT NULL COMMENT '更新人',
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    updated_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
    PRIMARY KEY (`sys_dict_id`) USING BTREE,
    UNIQUE INDEX `uk_sd_dict_code` (`dict_code`) USING BTREE
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '系统字典表';

-- 系统字典分项表
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`
(
    sys_dict_item_id BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '字典分项标识',
    sys_dict_id      BIGINT(20)            DEFAULT NULL COMMENT '字典标识',
    item_text        VARCHAR(100) NOT NULL COMMENT '字典项文本',
    item_value       VARCHAR(100) NOT NULL COMMENT '字典项值',
    item_desc        VARCHAR(255)          DEFAULT NULL COMMENT '描述',
    sort_order       TINYINT(6)            DEFAULT NULL COMMENT '排序',
    item_state       TINYINT(6)   NOT NULL DEFAULT 1 COMMENT '状态 1-启用 0-不启用',
    create_by        VARCHAR(32)           DEFAULT NULL,
    update_by        VARCHAR(32)           DEFAULT NULL,
    created_at       TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    updated_at       TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
    PRIMARY KEY (`sys_dict_item_id`) USING BTREE,
    INDEX `idx_sdi_role_dict_id` (`sys_dict_id`) USING BTREE,
    INDEX `idx_sdi_role_sort_order` (`sort_order`) USING BTREE,
    INDEX `idx_sdi_status` (`item_state`) USING BTREE,
    INDEX `idx_sdi_dict_val` (`sys_dict_id`, `item_value`) USING BTREE
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '系统字典分项表';


-- -- 第三方支付
-- 支付单表
DROP TABLE IF EXISTS `t_pay_order`;

-- 退款单表
DROP TABLE IF EXISTS `t_refund_order`;

-- 通知记录表
DROP TABLE IF EXISTS `t_notify_record`;

-- 单边账表
