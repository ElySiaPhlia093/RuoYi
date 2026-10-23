package com.ruoyi.repairorder.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 维修记录对象 biz_repair_record
 * 
 * @author xlt
 * @date 2026-10-23
 */
public class BizRepairRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long recordId;

    /** 所属工单 → biz_repair_order */
    private Long orderId;

    /** 设备 → biz_device */
    private Long deviceId;

    /** 维修人员 → sys_user */
    private Long handlerUserId;

    /** 维修类型（字典 repair_type） */
    @Excel(name = "维修类型", readConverterExp = "repair=故障维修,part=更换备件,pm=预防保养,tune=调试校准")
    private String repairType;

    /** 故障原因 */
    @Excel(name = "故障原因")
    private String faultCause;

    /** 处理过程 */
    @Excel(name = "处理过程")
    private String handleDesc;

    /** 更换备件 → biz_part */
    private Long partId;

    /** 备件数量 */
    @Excel(name = "备件数量")
    private Integer partQty;

    /** 维修费用（元） */
    @Excel(name = "维修费用")
    private BigDecimal cost;

    /** 处理耗时（分钟） */
    @Excel(name = "处理耗时(分钟)")
    private Integer durationMin;

    /** 处理结果（字典 repair_result） */
    @Excel(name = "处理结果", readConverterExp = "repaired=已修复,partial=部分修复,unfixed=未修复")
    private String result;

    /** 维修时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "维修时间", width = 20, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    /** 备注 */
    private String remark;

    /** 删除标志 */
    private String delFlag;

    /** 工单编号（关联查询） */
    @Excel(name = "工单编号")
    private String orderNo;

    /** 设备编号（关联查询） */
    @Excel(name = "设备编号")
    private String deviceCode;

    /** 设备名称（关联查询） */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 维修人员姓名（关联查询） */
    @Excel(name = "维修人员")
    private String handlerName;

    /** 备件名称（关联查询） */
    @Excel(name = "备件名称")
    private String partName;

    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public Long getRecordId() { return recordId; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getOrderId() { return orderId; }

    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getDeviceId() { return deviceId; }

    public void setHandlerUserId(Long handlerUserId) { this.handlerUserId = handlerUserId; }
    public Long getHandlerUserId() { return handlerUserId; }

    public void setRepairType(String repairType) { this.repairType = repairType; }
    public String getRepairType() { return repairType; }

    public void setFaultCause(String faultCause) { this.faultCause = faultCause; }
    public String getFaultCause() { return faultCause; }

    public void setHandleDesc(String handleDesc) { this.handleDesc = handleDesc; }
    public String getHandleDesc() { return handleDesc; }

    public void setPartId(Long partId) { this.partId = partId; }
    public Long getPartId() { return partId; }

    public void setPartQty(Integer partQty) { this.partQty = partQty; }
    public Integer getPartQty() { return partQty; }

    public void setCost(BigDecimal cost) { this.cost = cost; }
    public BigDecimal getCost() { return cost; }

    public void setDurationMin(Integer durationMin) { this.durationMin = durationMin; }
    public Integer getDurationMin() { return durationMin; }

    public void setResult(String result) { this.result = result; }
    public String getResult() { return result; }

    public void setRecordTime(Date recordTime) { this.recordTime = recordTime; }
    public Date getRecordTime() { return recordTime; }

    public void setRemark(String remark) { this.remark = remark; }
    public String getRemark() { return remark; }

    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
    public String getDelFlag() { return delFlag; }

    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getOrderNo() { return orderNo; }

    public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }
    public String getDeviceCode() { return deviceCode; }

    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getDeviceName() { return deviceName; }

    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }
    public String getHandlerName() { return handlerName; }

    public void setPartName(String partName) { this.partName = partName; }
    public String getPartName() { return partName; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("orderId", getOrderId())
            .append("deviceId", getDeviceId())
            .append("handlerUserId", getHandlerUserId())
            .append("repairType", getRepairType())
            .append("faultCause", getFaultCause())
            .append("handleDesc", getHandleDesc())
            .append("partId", getPartId())
            .append("partQty", getPartQty())
            .append("cost", getCost())
            .append("durationMin", getDurationMin())
            .append("result", getResult())
            .append("recordTime", getRecordTime())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
