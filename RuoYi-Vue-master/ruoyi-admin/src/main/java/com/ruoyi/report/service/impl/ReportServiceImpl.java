package com.ruoyi.report.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.report.mapper.ReportMapper;
import com.ruoyi.report.service.IReportService;

/**
 * 数据看板统计Service业务层处理
 * 
 * @author xlt
 * @date 2026-10-23
 */
@Service
public class ReportServiceImpl implements IReportService 
{
    @Autowired
    private ReportMapper reportMapper;

    @Override
    public Map<String, Object> summary()
    {
        return reportMapper.selectSummary();
    }

    @Override
    public List<Map<String, Object>> deviceStatus()
    {
        return reportMapper.selectDeviceStatusCount();
    }

    @Override
    public List<Map<String, Object>> deviceType()
    {
        return reportMapper.selectDeviceTypeCount();
    }

    @Override
    public List<Map<String, Object>> orderTrend(int days)
    {
        return reportMapper.selectOrderTrend(days);
    }

    @Override
    public List<Map<String, Object>> faultType()
    {
        return reportMapper.selectFaultTypeCount();
    }

    @Override
    public List<Map<String, Object>> costTop(int limit)
    {
        return reportMapper.selectCostTop(limit);
    }

    @Override
    public List<Map<String, Object>> inspectTrend(int days)
    {
        return reportMapper.selectInspectTrend(days);
    }
}
