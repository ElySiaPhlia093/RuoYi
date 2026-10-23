package com.ruoyi.device.service;

import java.util.List;
import com.ruoyi.device.domain.BizInspect;

/**
 * 设备巡检Service接口
 * 
 * @author xlt
 * @date 2026-10-23
 */
public interface IBizInspectService 
{
    /** 查询设备巡检 */
    public BizInspect selectBizInspectByInspectId(Long inspectId);

    /** 查询设备巡检列表 */
    public List<BizInspect> selectBizInspectList(BizInspect bizInspect);

    /** 新增设备巡检 */
    public int insertBizInspect(BizInspect bizInspect);

    /** 修改设备巡检 */
    public int updateBizInspect(BizInspect bizInspect);

    /** 批量删除设备巡检 */
    public int deleteBizInspectByInspectIds(Long[] inspectIds);

    /** 删除设备巡检信息 */
    public int deleteBizInspectByInspectId(Long inspectId);
}
