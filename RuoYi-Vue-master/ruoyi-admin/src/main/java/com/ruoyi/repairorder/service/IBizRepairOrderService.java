package com.ruoyi.repairorder.service;

import java.util.List;
import com.ruoyi.repairorder.domain.BizRepairOrder;
import com.ruoyi.repairorder.domain.BizRepairRecord;

/**
 * 报修工单Service接口
 * 
 * @author XLT
 * @date 2026-10-23
 */
public interface IBizRepairOrderService 
{
    /**
     * 查询报修工单
     * 
     * @param orderId 报修工单主键
     * @return 报修工单
     */
    public BizRepairOrder selectBizRepairOrderByOrderId(Long orderId);

    /**
     * 查询报修工单列表
     * 
     * @param bizRepairOrder 报修工单
     * @return 报修工单集合
     */
    public List<BizRepairOrder> selectBizRepairOrderList(BizRepairOrder bizRepairOrder);

    /**
     * 新增报修工单
     * 
     * @param bizRepairOrder 报修工单
     * @return 结果
     */
    public int insertBizRepairOrder(BizRepairOrder bizRepairOrder);

    /**
     * 修改报修工单
     * 
     * @param bizRepairOrder 报修工单
     * @return 结果
     */
    public int updateBizRepairOrder(BizRepairOrder bizRepairOrder);

    /**
     * 受理工单：待受理(0) -> 处理中(1)，记录受理人与受理时间
     *
     * @param orderId 报修工单主键
     * @return 结果
     */
    public int acceptOrder(Long orderId);

    /**
     * 办结工单：处理中(1) -> 已完成(2)，落维修记录并扣减备件库存
     *
     * @param orderId 报修工单主键
     * @param record 维修记录
     * @return 结果
     */
    public int finishOrder(Long orderId, BizRepairRecord record);

    /**
     * 批量删除报修工单
     * 
     * @param orderIds 需要删除的报修工单主键集合
     * @return 结果
     */
    public int deleteBizRepairOrderByOrderIds(Long[] orderIds);

    /**
     * 删除报修工单信息
     * 
     * @param orderId 报修工单主键
     * @return 结果
     */
    public int deleteBizRepairOrderByOrderId(Long orderId);
}
