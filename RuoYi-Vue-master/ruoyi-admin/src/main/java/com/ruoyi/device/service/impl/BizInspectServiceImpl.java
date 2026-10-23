package com.ruoyi.device.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.BizInspectMapper;
import com.ruoyi.device.domain.BizInspect;
import com.ruoyi.device.service.IBizInspectService;

/**
 * 设备巡检Service业务层处理
 * 
 * @author xlt
 * @date 2026-10-23
 */
@Service
public class BizInspectServiceImpl implements IBizInspectService 
{
    @Autowired
    private BizInspectMapper bizInspectMapper;

    @Override
    public BizInspect selectBizInspectByInspectId(Long inspectId)
    {
        return bizInspectMapper.selectBizInspectByInspectId(inspectId);
    }

    @Override
    public List<BizInspect> selectBizInspectList(BizInspect bizInspect)
    {
        return bizInspectMapper.selectBizInspectList(bizInspect);
    }

    @Override
    public int insertBizInspect(BizInspect bizInspect)
    {
        bizInspect.setCreateTime(DateUtils.getNowDate());
        return bizInspectMapper.insertBizInspect(bizInspect);
    }

    @Override
    public int updateBizInspect(BizInspect bizInspect)
    {
        bizInspect.setUpdateTime(DateUtils.getNowDate());
        return bizInspectMapper.updateBizInspect(bizInspect);
    }

    @Override
    public int deleteBizInspectByInspectIds(Long[] inspectIds)
    {
        return bizInspectMapper.deleteBizInspectByInspectIds(inspectIds);
    }

    @Override
    public int deleteBizInspectByInspectId(Long inspectId)
    {
        return bizInspectMapper.deleteBizInspectByInspectId(inspectId);
    }
}
