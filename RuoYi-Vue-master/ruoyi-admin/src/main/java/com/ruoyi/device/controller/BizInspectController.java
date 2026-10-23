package com.ruoyi.device.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.device.domain.BizInspect;
import com.ruoyi.device.service.IBizInspectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备巡检Controller
 * 
 * @author xlt
 * @date 2026-10-23
 */
@RestController
@RequestMapping("/device/inspect")
public class BizInspectController extends BaseController
{
    @Autowired
    private IBizInspectService bizInspectService;

    /** 查询设备巡检列表 */
    @PreAuthorize("@ss.hasPermi('device:inspect:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizInspect bizInspect)
    {
        startPage();
        List<BizInspect> list = bizInspectService.selectBizInspectList(bizInspect);
        return getDataTable(list);
    }

    /** 导出设备巡检列表 */
    @PreAuthorize("@ss.hasPermi('device:inspect:export')")
    @Log(title = "设备巡检", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizInspect bizInspect)
    {
        List<BizInspect> list = bizInspectService.selectBizInspectList(bizInspect);
        ExcelUtil<BizInspect> util = new ExcelUtil<BizInspect>(BizInspect.class);
        util.exportExcel(response, list, "设备巡检数据");
    }

    /** 获取设备巡检详细信息 */
    @PreAuthorize("@ss.hasPermi('device:inspect:query')")
    @GetMapping(value = "/{inspectId}")
    public AjaxResult getInfo(@PathVariable("inspectId") Long inspectId)
    {
        return success(bizInspectService.selectBizInspectByInspectId(inspectId));
    }

    /** 新增设备巡检 */
    @PreAuthorize("@ss.hasPermi('device:inspect:add')")
    @Log(title = "设备巡检", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizInspect bizInspect)
    {
        return toAjax(bizInspectService.insertBizInspect(bizInspect));
    }

    /** 修改设备巡检 */
    @PreAuthorize("@ss.hasPermi('device:inspect:edit')")
    @Log(title = "设备巡检", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizInspect bizInspect)
    {
        return toAjax(bizInspectService.updateBizInspect(bizInspect));
    }

    /** 删除设备巡检 */
    @PreAuthorize("@ss.hasPermi('device:inspect:remove')")
    @Log(title = "设备巡检", businessType = BusinessType.DELETE)
    @DeleteMapping("/{inspectIds}")
    public AjaxResult remove(@PathVariable Long[] inspectIds)
    {
        return toAjax(bizInspectService.deleteBizInspectByInspectIds(inspectIds));
    }
}
