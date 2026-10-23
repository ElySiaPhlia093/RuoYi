package com.ruoyi.device.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备巡检对象 biz_inspect
 * 
 * @author xlt
 * @date 2026-10-23
 */
public class BizInspect extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long inspectId;

    /** 巡检单号 */
    @Excel(name = "巡检单号")
    private String inspectNo;

    /** 巡检设备 → biz_device */
    private Long deviceId;

    /** 巡检人 → sys_user */
    private Long inspectUserId;

    /** 巡检类型（字典 inspect_type） */
    @Excel(name = "巡检类型", readConverterExp = "daily=日常巡检,weekly=周巡检,special=专项巡检")
    private String inspectType;

    /** 巡检时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "巡检时间", width = 20, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inspectTime;

    /** 检查项目 */
    @Excel(name = "检查项目")
    private String checkItems;

    /** 巡检结果（字典 inspect_result） */
    @Excel(name = "巡检结果", readConverterExp = "normal=正常,abnormal=异常")
    private String result;

    /** 异常描述 */
    @Excel(name = "异常描述")
    private String problemDesc;

    /** 异常转工单 → biz_repair_order */
    private Long orderId;

    /** 备注 */
    private String remark;

    /** 删除标志 */
    private String delFlag;

    /** 设备编号（关联查询） */
    @Excel(name = "设备编号")
    private String deviceCode;

    /** 设备名称（关联查询） */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 安装位置（关联查询） */
    @Excel(name = "安装位置")
    private String location;

    /** 巡检人姓名（关联查询） */
    @Excel(name = "巡检人")
    private String inspectUserName;

    /** 关联工单编号（关联查询） */
    @Excel(name = "关联工单")
    private String orderNo;

    public void setInspectId(Long inspectId) { this.inspectId = inspectId; }
    public Long getInspectId() { return inspectId; }

    public void setInspectNo(String inspectNo) { this.inspectNo = inspectNo; }
    public String getInspectNo() { return inspectNo; }

    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getDeviceId() { return deviceId; }

    public void setInspectUserId(Long inspectUserId) { this.inspectUserId = inspectUserId; }
    public Long getInspectUserId() { return inspectUserId; }

    public void setInspectType(String inspectType) { this.inspectType = inspectType; }
    public String getInspectType() { return inspectType; }

    public void setInspectTime(Date inspectTime) { this.inspectTime = inspectTime; }
    public Date getInspectTime() { return inspectTime; }

    public void setCheckItems(String checkItems) { this.checkItems = checkItems; }
    public String getCheckItems() { return checkItems; }

    public void setResult(String result) { this.result = result; }
    public String getResult() { return result; }

    public void setProblemDesc(String problemDesc) { this.problemDesc = problemDesc; }
    public String getProblemDesc() { return problemDesc; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getOrderId() { return orderId; }

    public void setRemark(String remark) { this.remark = remark; }
    public String getRemark() { return remark; }

    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
    public String getDelFlag() { return delFlag; }

    public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }
    public String getDeviceCode() { return deviceCode; }

    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getDeviceName() { return deviceName; }

    public void setLocation(String location) { this.location = location; }
    public String getLocation() { return location; }

    public void setInspectUserName(String inspectUserName) { this.inspectUserName = inspectUserName; }
    public String getInspectUserName() { return inspectUserName; }

    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getOrderNo() { return orderNo; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("inspectId", getInspectId())
            .append("inspectNo", getInspectNo())
            .append("deviceId", getDeviceId())
            .append("inspectUserId", getInspectUserId())
            .append("inspectType", getInspectType())
            .append("inspectTime", getInspectTime())
            .append("checkItems", getCheckItems())
            .append("result", getResult())
            .append("problemDesc", getProblemDesc())
            .append("orderId", getOrderId())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
