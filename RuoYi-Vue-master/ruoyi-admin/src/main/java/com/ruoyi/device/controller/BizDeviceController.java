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
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.device.domain.BizDevice;
import com.ruoyi.device.service.IBizDeviceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备台账Controller
 * 
 * @author xlt
 * @date 2026-10-16
 */
@RestController
@RequestMapping("/device/device")
public class BizDeviceController extends BaseController
{
    @Autowired
    private IBizDeviceService bizDeviceService;

    /**
     * 查询设备台账列表
     */
    @PreAuthorize("@ss.hasPermi('device:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizDevice bizDevice)
    {
        startPage();
        List<BizDevice> list = bizDeviceService.selectBizDeviceList(bizDevice);
        return getDataTable(list);
    }

    /**
     * 导出设备台账列表
     */
    @PreAuthorize("@ss.hasPermi('device:device:export')")
    @Log(title = "设备台账", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizDevice bizDevice)
    {
        List<BizDevice> list = bizDeviceService.selectBizDeviceList(bizDevice);
        ExcelUtil<BizDevice> util = new ExcelUtil<BizDevice>(BizDevice.class);
        util.exportExcel(response, list, "设备台账数据");
    }

    /**
     * 获取设备台账详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:device:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return success(bizDeviceService.selectBizDeviceByDeviceId(deviceId));
    }

    /**
     * 新增设备台账
     */
    @PreAuthorize("@ss.hasPermi('device:device:add')")
    @Log(title = "设备台账", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizDevice bizDevice)
    {
        return toAjax(bizDeviceService.insertBizDevice(bizDevice));
    }

    /**
     * 修改设备台账
     */
    @PreAuthorize("@ss.hasPermi('device:device:edit')")
    @Log(title = "设备台账", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizDevice bizDevice)
    {
        return toAjax(bizDeviceService.updateBizDevice(bizDevice));
    }

    /**
     * 删除设备台账
     */
    @PreAuthorize("@ss.hasPermi('device:device:remove')")
    @Log(title = "设备台账", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(bizDeviceService.deleteBizDeviceByDeviceIds(deviceIds));
    }

    /**
     * 导入设备台账数据
     */
    @PreAuthorize("@ss.hasPermi('device:device:import')")
    @Log(title = "设备台账", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<BizDevice> util = new ExcelUtil<BizDevice>(BizDevice.class);
        List<BizDevice> deviceList = util.importExcel(file.getInputStream());
        String message = bizDeviceService.importBizDevice(deviceList, updateSupport, getUsername());
        return AjaxResult.success(message);
    }

    /**
     * 下载设备台账导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BizDevice> util = new ExcelUtil<BizDevice>(BizDevice.class);
        util.importTemplateExcel(response, "设备台账数据");
    }
}
