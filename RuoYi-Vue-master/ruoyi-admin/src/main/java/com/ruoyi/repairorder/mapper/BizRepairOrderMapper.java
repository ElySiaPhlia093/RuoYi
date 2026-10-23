package com.ruoyi.repairorder.mapper;

import java.util.List;
import com.ruoyi.repairorder.domain.BizRepairOrder;

/**
 * 报修工单Mapper接口
 * 
 * @author XLT
 * @date 2026-10-23
 */
public interface BizRepairOrderMapper 
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
     * 删除报修工单
     * 
     * @param orderId 报修工单主键
     * @return 结果
     */
    public int deleteBizRepairOrderByOrderId(Long orderId);

    /**
     * 批量删除报修工单
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizRepairOrderByOrderIds(Long[] orderIds);
}
