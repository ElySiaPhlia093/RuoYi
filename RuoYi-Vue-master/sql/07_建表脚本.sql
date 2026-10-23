-- ============================================================
-- 工业设备资产与报修管理系统 · 建表脚本
-- 适用环境：MySQL 8.0 + 若依 RuoYi-Vue（Spring Boot 3 / JDK17）
-- 说明：
--   1) 本脚本可重复执行（先删后建），课堂演示可直接全选运行
--   2) 共 5 张业务表：设备、备件、报修工单、维修记录、设备巡检
--   3) 外键关系：工单→设备；维修记录→工单/设备/备件；巡检→设备/工单
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 一、设备台账表 biz_device
-- ============================================================
DROP TABLE IF EXISTS biz_device;
CREATE TABLE biz_device (
  device_id       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  device_code     VARCHAR(64)  NOT NULL                COMMENT '设备编号',
  device_name     VARCHAR(128) NOT NULL                COMMENT '设备名称',
  device_type     VARCHAR(32)  DEFAULT NULL            COMMENT '设备类型（字典 device_type）',
  model           VARCHAR(64)  DEFAULT NULL            COMMENT '设备型号',
  manufacturer    VARCHAR(128) DEFAULT NULL            COMMENT '生产厂商',
  location        VARCHAR(128) DEFAULT NULL            COMMENT '安装位置',
  status          VARCHAR(32)  DEFAULT 'running'       COMMENT '设备状态（字典 device_status）',
  purchase_date   DATE         DEFAULT NULL            COMMENT '采购日期',
  warranty_expire DATE         DEFAULT NULL            COMMENT '保修到期日',
  owner_user_id   BIGINT       DEFAULT NULL            COMMENT '负责人 → sys_user',
  remark          VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  del_flag        CHAR(1)      DEFAULT '0'             COMMENT '删除标志（0存在 2删除）',
  create_by       VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  create_time     DATETIME     DEFAULT NULL            COMMENT '创建时间',
  update_by       VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  update_time     DATETIME     DEFAULT NULL            COMMENT '更新时间',
  PRIMARY KEY (device_id),
  UNIQUE KEY uk_device_code (device_code),
  KEY idx_device_type (device_type),
  KEY idx_device_status (status),
  KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='设备台账表';

-- ============================================================
-- 二、备件库存表 biz_part
-- ============================================================
DROP TABLE IF EXISTS biz_part;
CREATE TABLE biz_part (
  part_id      BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
  part_code    VARCHAR(64)   NOT NULL                COMMENT '备件编号',
  part_name    VARCHAR(128)  NOT NULL                COMMENT '备件名称',
  spec         VARCHAR(128)  DEFAULT NULL            COMMENT '规格型号',
  unit         VARCHAR(16)   DEFAULT '个'            COMMENT '计量单位',
  stock        INT           DEFAULT 0               COMMENT '当前库存',
  safety_stock INT           DEFAULT 0               COMMENT '安全库存',
  unit_price   DECIMAL(10,2) DEFAULT 0.00            COMMENT '单价（元）',
  supplier     VARCHAR(128)  DEFAULT NULL            COMMENT '供应商',
  status       VARCHAR(16)   DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  remark       VARCHAR(500)  DEFAULT NULL            COMMENT '备注',
  del_flag     CHAR(1)       DEFAULT '0'             COMMENT '删除标志（0存在 2删除）',
  create_by    VARCHAR(64)   DEFAULT ''              COMMENT '创建者',
  create_time  DATETIME      DEFAULT NULL            COMMENT '创建时间',
  update_by    VARCHAR(64)   DEFAULT ''              COMMENT '更新者',
  update_time  DATETIME      DEFAULT NULL            COMMENT '更新时间',
  PRIMARY KEY (part_id),
  UNIQUE KEY uk_part_code (part_code),
  KEY idx_part_name (part_name),
  KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='备件库存表';

-- ============================================================
-- 三、报修工单表 biz_repair_order
-- ============================================================
DROP TABLE IF EXISTS biz_repair_order;
CREATE TABLE biz_repair_order (
  order_id        BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_no        VARCHAR(64)  NOT NULL                COMMENT '工单编号（唯一）',
  device_id       BIGINT       NOT NULL                COMMENT '报修设备 → biz_device',
  fault_type      VARCHAR(32)  DEFAULT NULL            COMMENT '故障类型（字典 fault_type）',
  fault_desc      VARCHAR(500) NOT NULL                COMMENT '故障描述',
  report_user_id  BIGINT       DEFAULT NULL            COMMENT '报修人 → sys_user',
  report_time     DATETIME     DEFAULT NULL            COMMENT '报修时间',
  status          VARCHAR(32)  DEFAULT '0'             COMMENT '工单状态（字典 order_status）',
  handler_user_id BIGINT       DEFAULT NULL            COMMENT '处理人 → sys_user',
  handle_time     DATETIME     DEFAULT NULL            COMMENT '受理时间',
  handle_result   VARCHAR(500) DEFAULT NULL            COMMENT '处理结果',
  finish_time     DATETIME     DEFAULT NULL            COMMENT '完成时间',
  remark          VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  del_flag        CHAR(1)      DEFAULT '0'             COMMENT '删除标志（0存在 2删除）',
  create_by       VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  create_time     DATETIME     DEFAULT NULL            COMMENT '创建时间',
  update_by       VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  update_time     DATETIME     DEFAULT NULL            COMMENT '更新时间',
  PRIMARY KEY (order_id),
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_device_id (device_id),
  KEY idx_order_status (status),
  KEY idx_report_time (report_time),
  KEY idx_del_flag (del_flag),
  CONSTRAINT fk_order_device FOREIGN KEY (device_id)
      REFERENCES biz_device (device_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='报修工单表';

-- ============================================================
-- 四、维修记录表 biz_repair_record（工单 1:N 维修记录）
-- ============================================================
DROP TABLE IF EXISTS biz_repair_record;
CREATE TABLE biz_repair_record (
  record_id       BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_id        BIGINT        NOT NULL                COMMENT '所属工单 → biz_repair_order',
  device_id       BIGINT        NOT NULL                COMMENT '设备 → biz_device（冗余便于统计）',
  handler_user_id BIGINT        DEFAULT NULL            COMMENT '维修人员 → sys_user',
  repair_type     VARCHAR(32)   DEFAULT NULL            COMMENT '维修类型（字典 repair_type）',
  fault_cause     VARCHAR(255)  DEFAULT NULL            COMMENT '故障原因',
  handle_desc     VARCHAR(500)  DEFAULT NULL            COMMENT '处理过程',
  part_id         BIGINT        DEFAULT NULL            COMMENT '更换备件 → biz_part',
  part_qty        INT           DEFAULT 0               COMMENT '备件数量',
  cost            DECIMAL(10,2) DEFAULT 0.00            COMMENT '维修费用（元）',
  duration_min    INT           DEFAULT NULL            COMMENT '处理耗时（分钟）',
  result          VARCHAR(32)   DEFAULT 'repaired'      COMMENT '处理结果（字典 repair_result）',
  record_time     DATETIME      DEFAULT NULL            COMMENT '维修时间',
  remark          VARCHAR(500)  DEFAULT NULL            COMMENT '备注',
  del_flag        CHAR(1)       DEFAULT '0'             COMMENT '删除标志（0存在 2删除）',
  create_by       VARCHAR(64)   DEFAULT ''              COMMENT '创建者',
  create_time     DATETIME      DEFAULT NULL            COMMENT '创建时间',
  update_by       VARCHAR(64)   DEFAULT ''              COMMENT '更新者',
  update_time     DATETIME      DEFAULT NULL            COMMENT '更新时间',
  PRIMARY KEY (record_id),
  KEY idx_rr_order (order_id),
  KEY idx_rr_device (device_id),
  KEY idx_rr_time (record_time),
  KEY idx_del_flag (del_flag),
  CONSTRAINT fk_rr_order FOREIGN KEY (order_id)
      REFERENCES biz_repair_order (order_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_rr_device FOREIGN KEY (device_id)
      REFERENCES biz_device (device_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_rr_part FOREIGN KEY (part_id)
      REFERENCES biz_part (part_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='维修记录表';

-- ============================================================
-- 五、设备巡检表 biz_inspect（巡检记录，异常可转工单）
-- ============================================================
DROP TABLE IF EXISTS biz_inspect;
CREATE TABLE biz_inspect (
  inspect_id      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  inspect_no      VARCHAR(64)  NOT NULL                COMMENT '巡检单号（唯一）',
  device_id       BIGINT       NOT NULL                COMMENT '巡检设备 → biz_device',
  inspect_user_id BIGINT       DEFAULT NULL            COMMENT '巡检人 → sys_user',
  inspect_type    VARCHAR(32)  DEFAULT 'daily'         COMMENT '巡检类型（字典 inspect_type）',
  inspect_time    DATETIME     DEFAULT NULL            COMMENT '巡检时间',
  check_items     VARCHAR(500) DEFAULT NULL            COMMENT '检查项目',
  result          VARCHAR(32)  DEFAULT 'normal'        COMMENT '巡检结果（字典 inspect_result）',
  problem_desc    VARCHAR(500) DEFAULT NULL            COMMENT '异常描述',
  order_id        BIGINT       DEFAULT NULL            COMMENT '异常转工单 → biz_repair_order',
  remark          VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  del_flag        CHAR(1)      DEFAULT '0'             COMMENT '删除标志（0存在 2删除）',
  create_by       VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  create_time     DATETIME     DEFAULT NULL            COMMENT '创建时间',
  update_by       VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  update_time     DATETIME     DEFAULT NULL            COMMENT '更新时间',
  PRIMARY KEY (inspect_id),
  UNIQUE KEY uk_inspect_no (inspect_no),
  KEY idx_inspect_device (device_id),
  KEY idx_inspect_time (inspect_time),
  KEY idx_inspect_result (result),
  KEY idx_del_flag (del_flag),
  CONSTRAINT fk_inspect_device FOREIGN KEY (device_id)
      REFERENCES biz_device (device_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_inspect_order FOREIGN KEY (order_id)
      REFERENCES biz_repair_order (order_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='设备巡检表';

-- ============================================================
-- 六、数据字典（禁止代码硬编码枚举）
-- ============================================================
DELETE FROM sys_dict_type WHERE dict_type IN
  ('device_type','device_status','fault_type','order_status',
   'repair_type','repair_result','inspect_type','inspect_result');

INSERT INTO sys_dict_type
  (dict_name, dict_type, status, create_by, create_time, remark) VALUES
  ('设备类型',   'device_type',    '0', 'admin', NOW(), '设备台账类型枚举'),
  ('设备状态',   'device_status',  '0', 'admin', NOW(), '设备运行状态枚举'),
  ('故障类型',   'fault_type',     '0', 'admin', NOW(), '报修故障分类枚举'),
  ('工单状态',   'order_status',   '0', 'admin', NOW(), '报修工单状态枚举'),
  ('维修类型',   'repair_type',    '0', 'admin', NOW(), '维修处理方式枚举'),
  ('维修结果',   'repair_result',  '0', 'admin', NOW(), '维修处理结果枚举'),
  ('巡检类型',   'inspect_type',   '0', 'admin', NOW(), '设备巡检类型枚举'),
  ('巡检结果',   'inspect_result', '0', 'admin', NOW(), '设备巡检结果枚举');

DELETE FROM sys_dict_data WHERE dict_type IN
  ('device_type','device_status','fault_type','order_status',
   'repair_type','repair_result','inspect_type','inspect_result');

-- 设备类型
INSERT INTO sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time) VALUES
  (1, '数控机床', 'cnc',        'device_type', 'primary', 'N', '0', 'admin', NOW()),
  (2, '注塑机',   'injection',  'device_type', 'success', 'N', '0', 'admin', NOW()),
  (3, '空压机',   'compressor', 'device_type', 'info',    'N', '0', 'admin', NOW()),
  (4, '传送带',   'conveyor',   'device_type', 'warning', 'N', '0', 'admin', NOW()),
  (5, 'AGV',      'agv',        'device_type', 'danger',  'N', '0', 'admin', NOW()),
  (6, '检测设备', 'detector',   'device_type', 'info',    'N', '0', 'admin', NOW());

-- 设备状态
INSERT INTO sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time) VALUES
  (1, '运行', 'running',   'device_status', 'success', 'Y', '0', 'admin', NOW()),
  (2, '待机', 'standby',   'device_status', 'info',    'N', '0', 'admin', NOW()),
  (3, '维修', 'repairing', 'device_status', 'warning', 'N', '0', 'admin', NOW()),
  (4, '停机', 'stopped',   'device_status', 'danger',  'N', '0', 'admin', NOW()),
  (5, '报废', 'scrapped',  'device_status', 'info',    'N', '0', 'admin', NOW());

-- 故障类型
INSERT INTO sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time) VALUES
  (1, '机械故障', 'mechanical', 'fault_type', 'warning', 'Y', '0', 'admin', NOW()),
  (2, '电气故障', 'electrical', 'fault_type', 'danger',  'N', '0', 'admin', NOW()),
  (3, '程序异常', 'program',    'fault_type', 'primary', 'N', '0', 'admin', NOW()),
  (4, '精度偏差', 'precision',  'fault_type', 'info',    'N', '0', 'admin', NOW()),
  (5, '其他',     'other',      'fault_type', 'info',    'N', '0', 'admin', NOW());

-- 工单状态
INSERT INTO sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time) VALUES
  (1, '待受理', '0', 'order_status', 'warning', 'Y', '0', 'admin', NOW()),
  (2, '处理中', '1', 'order_status', 'primary', 'N', '0', 'admin', NOW()),
  (3, '已完成', '2', 'order_status', 'success', 'N', '0', 'admin', NOW()),
  (4, '已取消', '3', 'order_status', 'info',    'N', '0', 'admin', NOW());

-- 维修类型
INSERT INTO sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time) VALUES
  (1, '故障维修', 'repair', 'repair_type', 'primary', 'Y', '0', 'admin', NOW()),
  (2, '更换备件', 'part',   'repair_type', 'warning', 'N', '0', 'admin', NOW()),
  (3, '预防保养', 'pm',     'repair_type', 'success', 'N', '0', 'admin', NOW()),
  (4, '调试校准', 'tune',   'repair_type', 'info',    'N', '0', 'admin', NOW());

-- 维修结果
INSERT INTO sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time) VALUES
  (1, '已修复',   'repaired', 'repair_result', 'success', 'Y', '0', 'admin', NOW()),
  (2, '部分修复', 'partial',  'repair_result', 'warning', 'N', '0', 'admin', NOW()),
  (3, '未修复',   'unfixed',  'repair_result', 'danger',  'N', '0', 'admin', NOW());

-- 巡检类型
INSERT INTO sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time) VALUES
  (1, '日常巡检', 'daily',   'inspect_type', 'primary', 'Y', '0', 'admin', NOW()),
  (2, '周巡检',   'weekly',  'inspect_type', 'info',    'N', '0', 'admin', NOW()),
  (3, '专项巡检', 'special', 'inspect_type', 'warning', 'N', '0', 'admin', NOW());

-- 巡检结果
INSERT INTO sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time) VALUES
  (1, '正常', 'normal',   'inspect_result', 'success', 'Y', '0', 'admin', NOW()),
  (2, '异常', 'abnormal', 'inspect_result', 'danger',  'N', '0', 'admin', NOW());

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 七、建表自检（运行后应看到 5 张业务表与 8 类字典）
-- ============================================================
SELECT 'biz_device'        AS 表名, COUNT(*) AS 记录数 FROM biz_device
UNION ALL SELECT 'biz_part',          COUNT(*) FROM biz_part
UNION ALL SELECT 'biz_repair_order',  COUNT(*) FROM biz_repair_order
UNION ALL SELECT 'biz_repair_record', COUNT(*) FROM biz_repair_record
UNION ALL SELECT 'biz_inspect',       COUNT(*) FROM biz_inspect
UNION ALL SELECT 'sys_dict_type(业务)', COUNT(*) FROM sys_dict_type
   WHERE dict_type IN ('device_type','device_status','fault_type','order_status',
                       'repair_type','repair_result','inspect_type','inspect_result');
