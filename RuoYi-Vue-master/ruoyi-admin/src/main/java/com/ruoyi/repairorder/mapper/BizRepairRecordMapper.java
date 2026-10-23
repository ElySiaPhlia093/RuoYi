package com.ruoyi.repairorder.mapper;

import java.util.List;
import com.ruoyi.repairorder.domain.BizRepairRecord;

/**
 * 维修记录Mapper接口
 * 
 * @author xlt
 * @date 2026-10-23
 */
public interface BizRepairRecordMapper 
{
    /** 查询维修记录 */
    public BizRepairRecord selectBizRepairRecordByRecordId(Long recordId);

    /** 查询维修记录列表 */
    public List<BizRepairRecord> selectBizRepairRecordList(BizRepairRecord bizRepairRecord);

    /** 新增维修记录 */
    public int insertBizRepairRecord(BizRepairRecord bizRepairRecord);

    /** 修改维修记录 */
    public int updateBizRepairRecord(BizRepairRecord bizRepairRecord);

    /** 删除维修记录 */
    public int deleteBizRepairRecordByRecordId(Long recordId);

    /** 批量删除维修记录 */
    public int deleteBizRepairRecordByRecordIds(Long[] recordIds);
}
