package com.ruoyi.device.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.BizPartMapper;
import com.ruoyi.device.domain.BizPart;
import com.ruoyi.device.service.IBizPartService;

/**
 * 备件库存Service业务层处理
 * 
 * @author xlt
 * @date 2026-10-23
 */
@Service
public class BizPartServiceImpl implements IBizPartService 
{
    @Autowired
    private BizPartMapper bizPartMapper;

    @Override
    public BizPart selectBizPartByPartId(Long partId)
    {
        return bizPartMapper.selectBizPartByPartId(partId);
    }

    @Override
    public List<BizPart> selectBizPartList(BizPart bizPart)
    {
        return bizPartMapper.selectBizPartList(bizPart);
    }

    @Override
    public int insertBizPart(BizPart bizPart)
    {
        bizPart.setCreateTime(DateUtils.getNowDate());
        return bizPartMapper.insertBizPart(bizPart);
    }

    @Override
    public int updateBizPart(BizPart bizPart)
    {
        bizPart.setUpdateTime(DateUtils.getNowDate());
        return bizPartMapper.updateBizPart(bizPart);
    }

    @Override
    public int deleteBizPartByPartIds(Long[] partIds)
    {
        return bizPartMapper.deleteBizPartByPartIds(partIds);
    }

    @Override
    public int deleteBizPartByPartId(Long partId)
    {
        return bizPartMapper.deleteBizPartByPartId(partId);
    }
}
