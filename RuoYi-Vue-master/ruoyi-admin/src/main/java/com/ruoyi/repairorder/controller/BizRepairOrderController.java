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
import com.ruoyi.repairorder.domain.BizRepairOrder;
import com.ruoyi.repairorder.domain.BizRepairRecord;
import com.ruoyi.repairorder.service.IBizRepairOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报修工单Controller
 * 
 * @author XLT
 * @date 2026-10-23
 */
@RestController
@RequestMapping("/repairorder/repairorder")
public class BizRepairOrderController extends BaseController
{
    @Autowired
    private IBizRepairOrderService bizRepairOrderService;

    /**
     * 查询报修工单列表
     */
    @PreAuthorize("@ss.hasPermi('repairorder:repairorder:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizRepairOrder bizRepairOrder)
    {
        startPage();
        List<BizRepairOrder> list = bizRepairOrderService.selectBizRepairOrderList(bizRepairOrder);
        return getDataTable(list);
    }

    /**
     * 导出报修工单列表
     */
    @PreAuthorize("@ss.hasPermi('repairorder:repairorder:export')")
    @Log(title = "报修工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizRepairOrder bizRepairOrder)
    {
        List<BizRepairOrder> list = bizRepairOrderService.selectBizRepairOrderList(bizRepairOrder);
        ExcelUtil<BizRepairOrder> util = new ExcelUtil<BizRepairOrder>(BizRepairOrder.class);
        util.exportExcel(response, list, "报修工单数据");
    }

    /**
     * 获取报修工单详细信息
     */
    @PreAuthorize("@ss.hasPermi('repairorder:repairorder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(bizRepairOrderService.selectBizRepairOrderByOrderId(orderId));
    }

    /**
     * 新增报修工单
     */
    @PreAuthorize("@ss.hasPermi('repairorder:repairorder:add')")
    @Log(title = "报修工单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizRepairOrder bizRepairOrder)
    {
        return toAjax(bizRepairOrderService.insertBizRepairOrder(bizRepairOrder));
    }

    /**
     * 修改报修工单
     */
    @PreAuthorize("@ss.hasPermi('repairorder:repairorder:edit')")
    @Log(title = "报修工单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizRepairOrder bizRepairOrder)
    {
        return toAjax(bizRepairOrderService.updateBizRepairOrder(bizRepairOrder));
    }

    /**
     * 受理工单（待受理 -> 处理中，记录受理人/受理时间）
     */
    @PreAuthorize("@ss.hasPermi('repairorder:repairorder:edit')")
    @Log(title = "报修工单", businessType = BusinessType.UPDATE)
    @PutMapping("/accept/{orderId}")
    public AjaxResult accept(@PathVariable("orderId") Long orderId)
    {
        return toAjax(bizRepairOrderService.acceptOrder(orderId));
    }

    /**
     * 办结工单（处理中 -> 已完成，落维修记录并扣减备件库存）
     */
    @PreAuthorize("@ss.hasPermi('repairorder:repairorder:edit')")
    @Log(title = "报修工单", businessType = BusinessType.UPDATE)
    @PutMapping("/finish/{orderId}")
    public AjaxResult finish(@PathVariable("orderId") Long orderId, @RequestBody(required = false) BizRepairRecord record)
    {
        return toAjax(bizRepairOrderService.finishOrder(orderId, record));
    }

    /**
     * 删除报修工单
     */
    @PreAuthorize("@ss.hasPermi('repairorder:repairorder:remove')")
    @Log(title = "报修工单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(bizRepairOrderService.deleteBizRepairOrderByOrderIds(orderIds));
    }
}
