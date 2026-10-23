package com.ruoyi.device.mapper;

import java.util.List;
import com.ruoyi.device.domain.BizInspect;

/**
 * 设备巡检Mapper接口
 * 
 * @author xlt
 * @date 2026-10-23
 */
public interface BizInspectMapper 
{
    /** 查询设备巡检 */
    public BizInspect selectBizInspectByInspectId(Long inspectId);

    /** 查询设备巡检列表 */
    public List<BizInspect> selectBizInspectList(BizInspect bizInspect);

    /** 新增设备巡检 */
    public int insertBizInspect(BizInspect bizInspect);

    /** 修改设备巡检 */
    public int updateBizInspect(BizInspect bizInspect);

    /** 删除设备巡检 */
    public int deleteBizInspectByInspectId(Long inspectId);

    /** 批量删除设备巡检 */
    public int deleteBizInspectByInspectIds(Long[] inspectIds);
}
