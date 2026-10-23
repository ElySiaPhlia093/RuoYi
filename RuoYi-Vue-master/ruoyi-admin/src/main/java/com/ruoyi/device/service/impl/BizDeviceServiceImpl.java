package com.ruoyi.device.service.impl;

import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.BizDeviceMapper;
import com.ruoyi.device.domain.BizDevice;
import com.ruoyi.device.service.IBizDeviceService;

/**
 * 设备台账Service业务层处理
 * 
 * @author xlt
 * @date 2026-10-16
 */
@Service
public class BizDeviceServiceImpl implements IBizDeviceService 
{
    @Autowired
    private BizDeviceMapper bizDeviceMapper;

    /**
     * 查询设备台账
     * 
     * @param deviceId 设备台账主键
     * @return 设备台账
     */
    @Override
    public BizDevice selectBizDeviceByDeviceId(Long deviceId)
    {
        return bizDeviceMapper.selectBizDeviceByDeviceId(deviceId);
    }

    /**
     * 查询设备台账列表
     * 
     * @param bizDevice 设备台账
     * @return 设备台账
     */
    @Override
    public List<BizDevice> selectBizDeviceList(BizDevice bizDevice)
    {
        return bizDeviceMapper.selectBizDeviceList(bizDevice);
    }

    /**
     * 新增设备台账
     * 
     * @param bizDevice 设备台账
     * @return 结果
     */
    @Override
    public int insertBizDevice(BizDevice bizDevice)
    {
        bizDevice.setCreateTime(DateUtils.getNowDate());
        return bizDeviceMapper.insertBizDevice(bizDevice);
    }

    /**
     * 修改设备台账
     * 
     * @param bizDevice 设备台账
     * @return 结果
     */
    @Override
    public int updateBizDevice(BizDevice bizDevice)
    {
        bizDevice.setUpdateTime(DateUtils.getNowDate());
        return bizDeviceMapper.updateBizDevice(bizDevice);
    }

    /**
     * 批量删除设备台账
     * 
     * @param deviceIds 需要删除的设备台账主键
     * @return 结果
     */
    @Override
    public int deleteBizDeviceByDeviceIds(Long[] deviceIds)
    {
        try
        {
            return bizDeviceMapper.deleteBizDeviceByDeviceIds(deviceIds);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new ServiceException("该设备已被报修工单或巡检记录引用，不能删除（可先删除关联记录）");
        }
    }

    /**
     * 删除设备台账信息
     * 
     * @param deviceId 设备台账主键
     * @return 结果
     */
    @Override
    public int deleteBizDeviceByDeviceId(Long deviceId)
    {
        try
        {
            return bizDeviceMapper.deleteBizDeviceByDeviceId(deviceId);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new ServiceException("该设备已被报修工单或巡检记录引用，不能删除（可先删除关联记录）");
        }
    }

    /**
     * 导入设备台账数据
     * 
     * @param list 待导入数据
     * @param isUpdateSupport 是否更新已存在的数据
     * @param operName 操作人
     * @return 导入结果
     */
    @Override
    public String importBizDevice(List<BizDevice> list, boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new ServiceException("导入设备数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (BizDevice device : list)
        {
            try
            {
                if (StringUtils.isEmpty(device.getDeviceCode()))
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、第 " + (successNum + failureNum) + " 行：设备编号不能为空");
                    continue;
                }
                BizDevice exist = bizDeviceMapper.selectBizDeviceByDeviceCode(device.getDeviceCode());
                if (exist == null)
                {
                    device.setCreateBy(operName);
                    this.insertBizDevice(device);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、设备 " + device.getDeviceCode() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    device.setDeviceId(exist.getDeviceId());
                    device.setUpdateBy(operName);
                    this.updateBizDevice(device);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、设备 " + device.getDeviceCode() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备 " + device.getDeviceCode() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、设备 " + device.getDeviceCode() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
