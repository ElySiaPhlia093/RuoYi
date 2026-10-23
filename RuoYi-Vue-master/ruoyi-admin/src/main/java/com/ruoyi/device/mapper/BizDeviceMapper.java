package com.ruoyi.device.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.device.domain.BizDevice;

/**
 * 设备台账Mapper接口
 * 
 * @author xlt
 * @date 2026-10-16
 */
public interface BizDeviceMapper 
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
     * 删除设备台账
     * 
     * @param deviceId 设备台账主键
     * @return 结果
     */
    public int deleteBizDeviceByDeviceId(Long deviceId);

    /**
     * 批量删除设备台账
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizDeviceByDeviceIds(Long[] deviceIds);

    /**
     * 更新设备运行状态
     *
     * @param deviceId 设备主键
     * @param status 状态（running/repairing/stopped/...）
     * @return 结果
     */
    public int updateDeviceStatus(@Param("deviceId") Long deviceId, @Param("status") String status);

    /**
     * 按设备编号查询设备（导入去重用）
     *
     * @param deviceCode 设备编号
     * @return 设备
     */
    public BizDevice selectBizDeviceByDeviceCode(String deviceCode);
}
