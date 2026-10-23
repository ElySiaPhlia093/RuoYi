package com.ruoyi.device.service;

import java.util.List;
import com.ruoyi.device.domain.BizDevice;

/**
 * 设备台账Service接口
 * 
 * @author xlt
 * @date 2026-10-16
 */
public interface IBizDeviceService 
{
    /**
     * 查询设备台账
     * 
     * @param deviceId 设备台账主键
     * @return 设备台账
     */
    public BizDevice selectBizDeviceByDeviceId(Long deviceId);

    /**
     * 查询设备台账列表
     * 
     * @param bizDevice 设备台账
     * @return 设备台账集合
     */
    public List<BizDevice> selectBizDeviceList(BizDevice bizDevice);

    /**
     * 新增设备台账
     * 
     * @param bizDevice 设备台账
     * @return 结果
     */
    public int insertBizDevice(BizDevice bizDevice);

    /**
     * 修改设备台账
     * 
     * @param bizDevice 设备台账
     * @return 结果
     */
    public int updateBizDevice(BizDevice bizDevice);

    /**
     * 批量删除设备台账
     * 
     * @param deviceIds 需要删除的设备台账主键集合
     * @return 结果
     */
    public int deleteBizDeviceByDeviceIds(Long[] deviceIds);

    /**
     * 删除设备台账信息
     * 
     * @param deviceId 设备台账主键
     * @return 结果
     */
    public int deleteBizDeviceByDeviceId(Long deviceId);

    /**
     * 导入设备台账数据
     *
     * @param list 待导入数据
     * @param isUpdateSupport 是否更新已存在数据
     * @param operName 操作人
     * @return 导入结果
     */
    public String importBizDevice(List<BizDevice> list, boolean isUpdateSupport, String operName);
}
