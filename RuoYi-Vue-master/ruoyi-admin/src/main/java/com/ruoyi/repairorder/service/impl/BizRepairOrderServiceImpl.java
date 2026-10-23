package com.ruoyi.repairorder.service.impl;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.repairorder.mapper.BizRepairOrderMapper;
import com.ruoyi.repairorder.mapper.BizRepairRecordMapper;
import com.ruoyi.repairorder.domain.BizRepairOrder;
import com.ruoyi.repairorder.domain.BizRepairRecord;
import com.ruoyi.repairorder.service.IBizRepairOrderService;
import com.ruoyi.device.mapper.BizPartMapper;
import com.ruoyi.device.mapper.BizDeviceMapper;




/**
 * 报修工单Service业务层处理
 * 
 * @author XLT
 * @date 2026-10-23
 */
@Service
public class BizRepairOrderServiceImpl implements IBizRepairOrderService 
{
    @Autowired
    private BizRepairOrderMapper bizRepairOrderMapper;

    @Autowired
    private BizRepairRecordMapper bizRepairRecordMapper;

    @Autowired
    private BizPartMapper bizPartMapper;

    @Autowired
    private BizDeviceMapper bizDeviceMapper;

    /**
     * 查询报修工单
     * 
     * @param orderId 报修工单主键
     * @return 报修工单
     */
    @Override
    public BizRepairOrder selectBizRepairOrderByOrderId(Long orderId)
    {
        return bizRepairOrderMapper.selectBizRepairOrderByOrderId(orderId);
    }

    /**
     * 查询报修工单列表
     * 
     * @param bizRepairOrder 报修工单
     * @return 报修工单
     */
    @Override
    public List<BizRepairOrder> selectBizRepairOrderList(BizRepairOrder bizRepairOrder)
    {
        return bizRepairOrderMapper.selectBizRepairOrderList(bizRepairOrder);
    }

    /**
     * 新增报修工单
     * 
     * @param bizRepairOrder 报修工单
     * @return 结果
     */
    @Override
    public int insertBizRepairOrder(BizRepairOrder bizRepairOrder)
    {
        bizRepairOrder.setCreateTime(DateUtils.getNowDate());
        return bizRepairOrderMapper.insertBizRepairOrder(bizRepairOrder);
    }

    /**
     * 修改报修工单
     * 
     * @param bizRepairOrder 报修工单
     * @return 结果
     */
    @Override
    public int updateBizRepairOrder(BizRepairOrder bizRepairOrder)
    {
        bizRepairOrder.setUpdateTime(DateUtils.getNowDate());
        return bizRepairOrderMapper.updateBizRepairOrder(bizRepairOrder);
    }

    /**
     * 受理工单：待受理(0) -> 处理中(1)，受理人记为当前登录用户，受理时间记为当前时间
     * 只有【待受理】状态才能受理，重复受理/已完成工单直接抛业务异常
     *
     * @param orderId 报修工单主键
     * @return 结果
     */
    @Override
    @Transactional
    public int acceptOrder(Long orderId)
    {
        BizRepairOrder order = bizRepairOrderMapper.selectBizRepairOrderByOrderId(orderId);
        if (order == null)
        {
            throw new ServiceException("工单不存在或已被删除");
        }
        if (!"0".equals(order.getStatus()))
        {
            throw new ServiceException("只有【待受理】状态的工单才能受理");
        }
        BizRepairOrder update = new BizRepairOrder();
        update.setOrderId(orderId);
        update.setStatus("1");
        update.setHandlerUserId(SecurityUtils.getUserId());
        update.setHandleTime(DateUtils.getNowDate());
        update.setUpdateBy(SecurityUtils.getUsername());
        update.setUpdateTime(DateUtils.getNowDate());
        return bizRepairOrderMapper.updateBizRepairOrder(update);
    }

    /**
     * 办结工单：处理中(1) -> 已完成(2)
     * 同时落一条维修记录、扣减备件库存、把设备状态恢复为运行
     *
     * @param orderId 报修工单主键
     * @param record 维修记录（维修类型/故障原因/处理过程/备件/费用/耗时/结果）
     * @return 结果
     */
    @Override
    @Transactional
    public int finishOrder(Long orderId, BizRepairRecord record)
    {
        BizRepairOrder order = bizRepairOrderMapper.selectBizRepairOrderByOrderId(orderId);
        if (order == null)
        {
            throw new ServiceException("工单不存在或已被删除");
        }
        if (!"1".equals(order.getStatus()))
        {
            throw new ServiceException("只有【处理中】状态的工单才能办结");
        }
        if (record == null)
        {
            record = new BizRepairRecord();
        }

        // 1. 落维修记录
        record.setRecordId(null);
        record.setOrderId(orderId);
        record.setDeviceId(order.getDeviceId());
        if (record.getHandlerUserId() == null)
        {
            record.setHandlerUserId(SecurityUtils.getUserId());
        }
        if (record.getResult() == null || record.getResult().isEmpty())
        {
            record.setResult("repaired");
        }
        if (record.getRecordTime() == null)
        {
            record.setRecordTime(DateUtils.getNowDate());
        }
        record.setDelFlag("0");
        record.setCreateBy(SecurityUtils.getUsername());
        record.setCreateTime(DateUtils.getNowDate());
        bizRepairRecordMapper.insertBizRepairRecord(record);

        // 2. 扣减备件库存
        if (record.getPartId() != null && record.getPartQty() != null && record.getPartQty() > 0)
        {
            bizPartMapper.deductStock(record.getPartId(), record.getPartQty());
        }

        // 3. 设备状态恢复为运行（仅维修中/停机的设备）
        bizDeviceMapper.updateDeviceStatus(order.getDeviceId(), "running");

        // 4. 更新工单
        BizRepairOrder update = new BizRepairOrder();
        update.setOrderId(orderId);
        update.setStatus("2");
        update.setFinishTime(record.getRecordTime());
        update.setHandleResult(record.getHandleDesc() != null && !record.getHandleDesc().isEmpty()
                ? record.getHandleDesc() : "维修完成");
        update.setUpdateBy(SecurityUtils.getUsername());
        update.setUpdateTime(DateUtils.getNowDate());
        return bizRepairOrderMapper.updateBizRepairOrder(update);
    }

    /**
     * 批量删除报修工单
     * 
     * @param orderIds 需要删除的报修工单主键
     * @return 结果
     */
    @Override
    public int deleteBizRepairOrderByOrderIds(Long[] orderIds)
    {
        return bizRepairOrderMapper.deleteBizRepairOrderByOrderIds(orderIds);
    }

    /**
     * 删除报修工单信息
     * 
     * @param orderId 报修工单主键
     * @return 结果
     */
    @Override
    public int deleteBizRepairOrderByOrderId(Long orderId)
    {
        return bizRepairOrderMapper.deleteBizRepairOrderByOrderId(orderId);
    }
}
