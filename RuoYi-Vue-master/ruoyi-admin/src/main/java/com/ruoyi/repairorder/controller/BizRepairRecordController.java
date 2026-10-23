package com.ruoyi.repairorder.controller;

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
import com.ruoyi.repairorder.domain.BizRepairRecord;
import com.ruoyi.repairorder.service.IBizRepairRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 维修记录Controller
 * 
 * @author xlt
 * @date 2026-10-23
 */
@RestController
@RequestMapping("/repairorder/record")
public class BizRepairRecordController extends BaseController
{
    @Autowired
    private IBizRepairRecordService bizRepairRecordService;

    /** 查询维修记录列表 */
    @PreAuthorize("@ss.hasPermi('repairorder:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizRepairRecord bizRepairRecord)
    {
        startPage();
        List<BizRepairRecord> list = bizRepairRecordService.selectBizRepairRecordList(bizRepairRecord);
        return getDataTable(list);
    }

    /** 导出维修记录列表 */
    @PreAuthorize("@ss.hasPermi('repairorder:record:export')")
    @Log(title = "维修记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizRepairRecord bizRepairRecord)
    {
        List<BizRepairRecord> list = bizRepairRecordService.selectBizRepairRecordList(bizRepairRecord);
        ExcelUtil<BizRepairRecord> util = new ExcelUtil<BizRepairRecord>(BizRepairRecord.class);
        util.exportExcel(response, list, "维修记录数据");
    }

    /** 获取维修记录详细信息 */
    @PreAuthorize("@ss.hasPermi('repairorder:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(bizRepairRecordService.selectBizRepairRecordByRecordId(recordId));
    }

    /** 新增维修记录 */
    @PreAuthorize("@ss.hasPermi('repairorder:record:add')")
    @Log(title = "维修记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizRepairRecord bizRepairRecord)
    {
        return toAjax(bizRepairRecordService.insertBizRepairRecord(bizRepairRecord));
    }

    /** 修改维修记录 */
    @PreAuthorize("@ss.hasPermi('repairorder:record:edit')")
    @Log(title = "维修记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizRepairRecord bizRepairRecord)
    {
        return toAjax(bizRepairRecordService.updateBizRepairRecord(bizRepairRecord));
    }

    /** 删除维修记录 */
    @PreAuthorize("@ss.hasPermi('repairorder:record:remove')")
    @Log(title = "维修记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(bizRepairRecordService.deleteBizRepairRecordByRecordIds(recordIds));
    }
}
