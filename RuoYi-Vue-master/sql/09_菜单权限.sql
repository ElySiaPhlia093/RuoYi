-- ============================================================
-- 工业设备资产与报修管理系统 · 菜单与权限脚本（可重复执行）
-- 内容：
--   1) 数据看板（一级菜单）
--   2) 设备管理：设备台账 / 设备巡检 / 备件库存
--   3) 报修工单（一级菜单）
--   4) 维修记录（一级菜单）
--   5) 演示角色「维修员」+ 演示账号 repair01
-- 依赖：07_建表脚本.sql 已执行；数据库 ry-vue
-- ============================================================

SET NAMES utf8mb4;

-- ------------------------------------------------------------
-- 一、清理旧菜单（按 perms 前缀删按钮，按名称删目录/页面）
-- ------------------------------------------------------------
DELETE FROM sys_role_menu WHERE menu_id IN (
  SELECT menu_id FROM sys_menu
  WHERE perms LIKE 'device:%' OR perms LIKE 'repairorder:%' OR perms LIKE 'report:%'
);

DELETE FROM sys_menu
WHERE perms LIKE 'device:%' OR perms LIKE 'repairorder:%' OR perms LIKE 'report:%';

DELETE FROM sys_menu
WHERE menu_name IN ('设备管理','设备台账','设备巡检','备件库存','报修工单','维修记录','数据看板');

-- 清理孤儿授权记录
DELETE FROM sys_role_menu WHERE menu_id NOT IN (SELECT menu_id FROM sys_menu);

-- ------------------------------------------------------------
-- 二、一级菜单
-- ------------------------------------------------------------
INSERT INTO sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache,
   menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
  (2025, '数据看板', 0, 1, 'report', 'report/index', '', 1, 0,
   'C', '0', '0', 'report:dashboard:list', 'chart', 'admin', NOW(), '', NULL, '设备运行与工单统计看板'),
  (2024, '设备管理', 0, 4, 'device', NULL, '', 1, 0,
   'M', '0', '0', '', 'component', 'admin', NOW(), '', NULL, '设备台账/巡检/备件'),
  (2040, '报修工单', 0, 5, 'repairorder', 'repairorder/repairorder/index', '', 1, 0,
   'C', '0', '0', 'repairorder:repairorder:list', 'chart', 'admin', NOW(), '', NULL, '报修工单'),
  (2041, '维修记录', 0, 6, 'repairrecord', 'repairorder/record/index', '', 1, 0,
   'C', '0', '0', 'repairorder:record:list', 'tool', 'admin', NOW(), '', NULL, '工单维修记录');

-- 若依官网往后挪
UPDATE sys_menu SET order_num = 9 WHERE menu_id = 4;

-- ------------------------------------------------------------
-- 三、设备管理下的页面菜单
-- ------------------------------------------------------------
INSERT INTO sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache,
   menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
  (2031, '设备台账', 2024, 1, 'ledger', 'device/device/index', '', 1, 0,
   'C', '0', '0', 'device:device:list', 'list', 'admin', NOW(), '', NULL, '设备台账'),
  (2032, '设备巡检', 2024, 2, 'inspect', 'device/inspect/index', '', 1, 0,
   'C', '0', '0', 'device:inspect:list', 'eye-open', 'admin', NOW(), '', NULL, '设备巡检记录'),
  (2033, '备件库存', 2024, 3, 'part', 'device/part/index', '', 1, 0,
   'C', '0', '0', 'device:part:list', 'shopping', 'admin', NOW(), '', NULL, '备件库存');

-- ------------------------------------------------------------
-- 四、按钮权限
-- ------------------------------------------------------------
INSERT INTO sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache,
   menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
  -- 设备台账
  (2051, '设备查询', 2031, 1, '', NULL, '', 1, 0, 'F', '0', '0', 'device:device:query',  '#', 'admin', NOW(), '', NULL, ''),
  (2052, '设备新增', 2031, 2, '', NULL, '', 1, 0, 'F', '0', '0', 'device:device:add',    '#', 'admin', NOW(), '', NULL, ''),
  (2053, '设备修改', 2031, 3, '', NULL, '', 1, 0, 'F', '0', '0', 'device:device:edit',   '#', 'admin', NOW(), '', NULL, ''),
  (2054, '设备删除', 2031, 4, '', NULL, '', 1, 0, 'F', '0', '0', 'device:device:remove', '#', 'admin', NOW(), '', NULL, ''),
  (2055, '设备导出', 2031, 5, '', NULL, '', 1, 0, 'F', '0', '0', 'device:device:export', '#', 'admin', NOW(), '', NULL, ''),
  (2056, '设备导入', 2031, 6, '', NULL, '', 1, 0, 'F', '0', '0', 'device:device:import', '#', 'admin', NOW(), '', NULL, ''),
  -- 设备巡检
  (2061, '巡检查询', 2032, 1, '', NULL, '', 1, 0, 'F', '0', '0', 'device:inspect:query',  '#', 'admin', NOW(), '', NULL, ''),
  (2062, '巡检新增', 2032, 2, '', NULL, '', 1, 0, 'F', '0', '0', 'device:inspect:add',    '#', 'admin', NOW(), '', NULL, ''),
  (2063, '巡检修改', 2032, 3, '', NULL, '', 1, 0, 'F', '0', '0', 'device:inspect:edit',   '#', 'admin', NOW(), '', NULL, ''),
  (2064, '巡检删除', 2032, 4, '', NULL, '', 1, 0, 'F', '0', '0', 'device:inspect:remove', '#', 'admin', NOW(), '', NULL, ''),
  (2065, '巡检导出', 2032, 5, '', NULL, '', 1, 0, 'F', '0', '0', 'device:inspect:export', '#', 'admin', NOW(), '', NULL, ''),
  -- 备件库存
  (2071, '备件查询', 2033, 1, '', NULL, '', 1, 0, 'F', '0', '0', 'device:part:query',  '#', 'admin', NOW(), '', NULL, ''),
  (2072, '备件新增', 2033, 2, '', NULL, '', 1, 0, 'F', '0', '0', 'device:part:add',    '#', 'admin', NOW(), '', NULL, ''),
  (2073, '备件修改', 2033, 3, '', NULL, '', 1, 0, 'F', '0', '0', 'device:part:edit',   '#', 'admin', NOW(), '', NULL, ''),
  (2074, '备件删除', 2033, 4, '', NULL, '', 1, 0, 'F', '0', '0', 'device:part:remove', '#', 'admin', NOW(), '', NULL, ''),
  (2075, '备件导出', 2033, 5, '', NULL, '', 1, 0, 'F', '0', '0', 'device:part:export', '#', 'admin', NOW(), '', NULL, ''),
  -- 报修工单
  (2081, '工单查询', 2040, 1, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:repairorder:query',  '#', 'admin', NOW(), '', NULL, ''),
  (2082, '工单新增', 2040, 2, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:repairorder:add',    '#', 'admin', NOW(), '', NULL, ''),
  (2083, '工单修改', 2040, 3, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:repairorder:edit',   '#', 'admin', NOW(), '', NULL, ''),
  (2084, '工单删除', 2040, 4, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:repairorder:remove', '#', 'admin', NOW(), '', NULL, ''),
  (2085, '工单导出', 2040, 5, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:repairorder:export', '#', 'admin', NOW(), '', NULL, ''),
  -- 维修记录
  (2091, '记录查询', 2041, 1, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:record:query',  '#', 'admin', NOW(), '', NULL, ''),
  (2092, '记录新增', 2041, 2, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:record:add',    '#', 'admin', NOW(), '', NULL, ''),
  (2093, '记录修改', 2041, 3, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:record:edit',   '#', 'admin', NOW(), '', NULL, ''),
  (2094, '记录删除', 2041, 4, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:record:remove', '#', 'admin', NOW(), '', NULL, ''),
  (2095, '记录导出', 2041, 5, '', NULL, '', 1, 0, 'F', '0', '0', 'repairorder:record:export', '#', 'admin', NOW(), '', NULL, '');

-- ------------------------------------------------------------
-- 五、演示角色「维修员」：只能看看板、处理工单、登记维修记录
-- ------------------------------------------------------------
DELETE FROM sys_role WHERE role_key = 'repairer';

INSERT INTO sys_role
  (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly,
   status, del_flag, create_by, create_time, remark)
VALUES
  ('维修员', 'repairer', 3, '1', 1, 1, '0', '0', 'admin', NOW(), '演示角色：数据看板 + 工单处理 + 维修记录');

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r, sys_menu m
WHERE r.role_key = 'repairer'
  AND m.menu_id IN (2025, 2024, 2031, 2051, 2040, 2081, 2083, 2085, 2041, 2091, 2092, 2093, 2095);

-- ------------------------------------------------------------
-- 六、演示账号 repair01 / admin123（密码复用 admin 的 BCrypt 值）
-- ------------------------------------------------------------
DELETE FROM sys_user_role WHERE user_id IN (SELECT user_id FROM sys_user WHERE user_name = 'repair01');
DELETE FROM sys_user WHERE user_name = 'repair01';

INSERT INTO sys_user
  (dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password,
   status, del_flag, login_ip, login_date, pwd_update_date, create_by, create_time,
   update_by, update_time, remark)
SELECT 103, 'repair01', '维修员张工', '00', '', '', '0', '', password,
       '0', '0', '', NULL, NULL, 'admin', NOW(), '', NULL, '演示账号：密码同 admin（admin123）'
FROM sys_user WHERE user_name = 'admin' LIMIT 1;

INSERT INTO sys_user_role (user_id, role_id)
SELECT u.user_id, r.role_id
FROM sys_user u, sys_role r
WHERE u.user_name = 'repair01' AND r.role_key = 'repairer';

-- ------------------------------------------------------------
-- 七、自检
-- ------------------------------------------------------------
SELECT m.menu_id, m.menu_name, m.parent_id, m.menu_type, m.path, m.perms
FROM sys_menu m
WHERE m.menu_id >= 2024
ORDER BY m.parent_id, m.order_num;

SELECT r.role_name, r.role_key, COUNT(rm.menu_id) AS 授权菜单数
FROM sys_role r LEFT JOIN sys_role_menu rm ON rm.role_id = r.role_id
WHERE r.role_key = 'repairer'
GROUP BY r.role_id, r.role_name, r.role_key;
