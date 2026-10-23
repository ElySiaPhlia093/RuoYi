package com.ruoyi.device.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.device.domain.BizPart;

/**
 * 备件库存Mapper接口
 * 
 * @author xlt
 * @date 2026-10-23
 */
public interface BizPartMapper 
{
    /** 查询备件库存 */
    public BizPart selectBizPartByPartId(Long partId);

    /** 查询备件库存列表 */
    public List<BizPart> selectBizPartList(BizPart bizPart);

    /** 新增备件库存 */
    public int insertBizPart(BizPart bizPart);

    /** 修改备件库存 */
    public int updateBizPart(BizPart bizPart);

    /** 删除备件库存 */
    public int deleteBizPartByPartId(Long partId);

    /** 批量删除备件库存 */
    public int deleteBizPartByPartIds(Long[] partIds);

    /** 扣减库存（不低于 0） */
    public int deductStock(@Param("partId") Long partId, @Param("qty") Integer qty);
}
