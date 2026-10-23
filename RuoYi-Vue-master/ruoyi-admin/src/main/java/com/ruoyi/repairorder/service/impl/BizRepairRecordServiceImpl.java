package com.ruoyi.repairorder.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.repairorder.mapper.BizRepairRecordMapper;
import com.ruoyi.repairorder.domain.BizRepairRecord;
import com.ruoyi.repairorder.service.IBizRepairRecordService;

/**
 * 维修记录Service业务层处理
 * 
 * @author xlt
 * @date 2026-10-23
 */
@Service
public class BizRepairRecordServiceImpl implements IBizRepairRecordService 
{
    @Autowired
    private BizRepairRecordMapper bizRepairRecordMapper;

    @Override
    public BizRepairRecord selectBizRepairRecordByRecordId(Long recordId)
    {
        return bizRepairRecordMapper.selectBizRepairRecordByRecordId(recordId);
    }

    @Override
    public List<BizRepairRecord> selectBizRepairRecordList(BizRepairRecord bizRepairRecord)
    {
        return bizRepairRecordMapper.selectBizRepairRecordList(bizRepairRecord);
    }

    @Override
    public int insertBizRepairRecord(BizRepairRecord bizRepairRecord)
    {
        bizRepairRecord.setCreateTime(DateUtils.getNowDate());
        return bizRepairRecordMapper.insertBizRepairRecord(bizRepairRecord);
    }

    @Override
    public int updateBizRepairRecord(BizRepairRecord bizRepairRecord)
    {
        bizRepairRecord.setUpdateTime(DateUtils.getNowDate());
        return bizRepairRecordMapper.updateBizRepairRecord(bizRepairRecord);
    }

    @Override
    public int deleteBizRepairRecordByRecordIds(Long[] recordIds)
    {
        return bizRepairRecordMapper.deleteBizRepairRecordByRecordIds(recordIds);
    }

    @Override
    public int deleteBizRepairRecordByRecordId(Long recordId)
    {
        return bizRepairRecordMapper.deleteBizRepairRecordByRecordId(recordId);
    }
}
