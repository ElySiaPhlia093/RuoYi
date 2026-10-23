package com.ruoyi.report.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 数据看板统计Mapper接口
 * 
 * @author xlt
 * @date 2026-10-23
 */
public interface ReportMapper 
{
    /** 概览指标 */
    public Map<String, Object> selectSummary();

    /** 设备状态分布 */
    public List<Map<String, Object>> selectDeviceStatusCount();

    /** 设备类型分布 */
    public List<Map<String, Object>> selectDeviceTypeCount();

    /** 报修工单趋势（近 N 天） */
    public List<Map<String, Object>> selectOrderTrend(@Param("days") int days);

    /** 故障类型分布 */
    public List<Map<String, Object>> selectFaultTypeCount();

    /** 维修费用 TOP N 设备 */
    public List<Map<String, Object>> selectCostTop(@Param("limit") int limit);

    /** 巡检趋势（近 N 天，正常/异常） */
    public List<Map<String, Object>> selectInspectTrend(@Param("days") int days);
}
