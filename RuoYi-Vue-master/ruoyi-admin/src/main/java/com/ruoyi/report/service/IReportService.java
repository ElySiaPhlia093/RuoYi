package com.ruoyi.report.service;

import java.util.List;
import java.util.Map;

/**
 * 数据看板统计Service接口
 * 
 * @author xlt
 * @date 2026-10-23
 */
public interface IReportService 
{
    /** 概览指标 */
    public Map<String, Object> summary();

    /** 设备状态分布 */
    public List<Map<String, Object>> deviceStatus();

    /** 设备类型分布 */
    public List<Map<String, Object>> deviceType();

    /** 报修工单趋势 */
    public List<Map<String, Object>> orderTrend(int days);

    /** 故障类型分布 */
    public List<Map<String, Object>> faultType();

    /** 维修费用 TOP */
    public List<Map<String, Object>> costTop(int limit);

    /** 巡检趋势 */
    public List<Map<String, Object>> inspectTrend(int days);
}
