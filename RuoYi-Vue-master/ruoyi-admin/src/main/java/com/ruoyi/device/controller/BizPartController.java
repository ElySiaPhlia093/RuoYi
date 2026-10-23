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
import com.ruoyi.device.domain.BizPart;
import com.ruoyi.device.service.IBizPartService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 备件库存Controller
 * 
 * @author xlt
 * @date 2026-10-23
 */
@RestController
@RequestMapping("/device/part")
public class BizPartController extends BaseController
{
    @Autowired
    private IBizPartService bizPartService;

    /** 查询备件库存列表 */
    @PreAuthorize("@ss.hasPermi('device:part:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPart bizPart)
    {
        startPage();
        List<BizPart> list = bizPartService.selectBizPartList(bizPart);
        return getDataTable(list);
    }

    /** 导出备件库存列表 */
    @PreAuthorize("@ss.hasPermi('device:part:export')")
    @Log(title = "备件库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPart bizPart)
    {
        List<BizPart> list = bizPartService.selectBizPartList(bizPart);
        ExcelUtil<BizPart> util = new ExcelUtil<BizPart>(BizPart.class);
        util.exportExcel(response, list, "备件库存数据");
    }

    /** 获取备件库存详细信息 */
    @PreAuthorize("@ss.hasPermi('device:part:query')")
    @GetMapping(value = "/{partId}")
    public AjaxResult getInfo(@PathVariable("partId") Long partId)
    {
        return success(bizPartService.selectBizPartByPartId(partId));
    }

    /** 新增备件库存 */
    @PreAuthorize("@ss.hasPermi('device:part:add')")
    @Log(title = "备件库存", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizPart bizPart)
    {
        return toAjax(bizPartService.insertBizPart(bizPart));
    }

    /** 修改备件库存 */
    @PreAuthorize("@ss.hasPermi('device:part:edit')")
    @Log(title = "备件库存", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizPart bizPart)
    {
        return toAjax(bizPartService.updateBizPart(bizPart));
    }

    /** 删除备件库存 */
    @PreAuthorize("@ss.hasPermi('device:part:remove')")
    @Log(title = "备件库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/{partIds}")
    public AjaxResult remove(@PathVariable Long[] partIds)
    {
        return toAjax(bizPartService.deleteBizPartByPartIds(partIds));
    }
}
