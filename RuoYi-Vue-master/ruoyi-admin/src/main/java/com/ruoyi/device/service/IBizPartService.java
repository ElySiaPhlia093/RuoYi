package com.ruoyi.device.service;

import java.util.List;
import com.ruoyi.device.domain.BizPart;

/**
 * 备件库存Service接口
 * 
 * @author xlt
 * @date 2026-10-23
 */
public interface IBizPartService 
{
    /** 查询备件库存 */
    public BizPart selectBizPartByPartId(Long partId);

    /** 查询备件库存列表 */
    public List<BizPart> selectBizPartList(BizPart bizPart);

    /** 新增备件库存 */
    public int insertBizPart(BizPart bizPart);

    /** 修改备件库存 */
    public int updateBizPart(BizPart bizPart);

    /** 批量删除备件库存 */
    public int deleteBizPartByPartIds(Long[] partIds);

    /** 删除备件库存信息 */
    public int deleteBizPartByPartId(Long partId);
}
