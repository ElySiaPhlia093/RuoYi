package com.ruoyi.report.controller;

import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.report.service.IReportService;

/**
 * 数据看板统计Controller
 * 
 * @author xlt
 * @date 2026-10-23
 */
@RestController
@RequestMapping("/report/dashboard")
public class ReportController extends BaseController
{
    @Autowired
    private IReportService reportService;

    /** 概览指标 */
    @PreAuthorize("@ss.hasPermi('report:dashboard:list')")
    @GetMapping("/summary")
    public AjaxResult summary()
    {
        return success(reportService.summary());
    }

    /** 设备状态分布 */
    @PreAuthorize("@ss.hasPermi('report:dashboard:list')")
    @GetMapping("/deviceStatus")
    public AjaxResult deviceStatus()
    {
        return success(reportService.deviceStatus());
    }

    /** 设备类型分布 */
    @PreAuthorize("@ss.hasPermi('report:dashboard:list')")
    @GetMapping("/deviceType")
    public AjaxResult deviceType()
    {
        return success(reportService.deviceType());
    }

    /** 报修工单趋势（默认近 30 天） */
    @PreAuthorize("@ss.hasPermi('report:dashboard:list')")
    @GetMapping("/orderTrend")
    public AjaxResult orderTrend(@RequestParam(value = "days", defaultValue = "30") int days)
    {
        return success(reportService.orderTrend(days));
    }

    /** 故障类型分布 */
    @PreAuthorize("@ss.hasPermi('report:dashboard:list')")
    @GetMapping("/faultType")
    public AjaxResult faultType()
    {
        return success(reportService.faultType());
    }

    /** 维修费用 TOP N（默认 5） */
    @PreAuthorize("@ss.hasPermi('report:dashboard:list')")
    @GetMapping("/costTop")
    public AjaxResult costTop(@RequestParam(value = "limit", defaultValue = "5") int limit)
    {
        return success(reportService.costTop(limit));
    }

    /** 巡检趋势（默认近 30 天） */
    @PreAuthorize("@ss.hasPermi('report:dashboard:list')")
    @GetMapping("/inspectTrend")
    public AjaxResult inspectTrend(@RequestParam(value = "days", defaultValue = "30") int days)
    {
        List<Map<String, Object>> list = reportService.inspectTrend(days);
        return success(list);
    }
}
