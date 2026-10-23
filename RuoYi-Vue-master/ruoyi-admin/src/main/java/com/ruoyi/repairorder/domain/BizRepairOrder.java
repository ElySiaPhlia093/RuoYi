package com.ruoyi.repairorder.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报修工单对象 biz_repair_order
 * 
 * @author XLT
 * @date 2026-10-23
 */
public class BizRepairOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long orderId;

    /** 工单编号 */
    @Excel(name = "工单编号")
    private String orderNo;

    /** 报修设备  */
    @Excel(name = "报修设备 ")
    private Long deviceId;

    /** 故障类型 */
    @Excel(name = "故障类型")
    private String faultType;

    /** 故障描述 */
    @Excel(name = "故障描述")
    private String faultDesc;

    /** 报修人  */
    @Excel(name = "报修人 ")
    private Long reportUserId;

    /** 报修时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报修时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportTime;

    /** 工单状态 */
    @Excel(name = "工单状态")
    private String status;

    /** 处理人  */
    @Excel(name = "处理人 ")
    private Long handlerUserId;

    /** 受理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "受理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handleTime;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String handleResult;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date finishTime;

    /** 删除标志 */
    private String delFlag;

    /**设备编号 join带出  不是本表字段**/
    private String deviceCode;

    /**设备名称 join带出  不是本表字段**/
    private String deviceName;

    /**安装位置 join带出  不是本表字段**/
    private String location;

    /**报修人姓名 join sys_user 带出  不是本表字段**/
    private String reportUserName;

    /**处理人姓名 join sys_user 带出  不是本表字段**/
    private String handlerName;


    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setFaultType(String faultType) 
    {
        this.faultType = faultType;
    }

    public String getFaultType() 
    {
        return faultType;
    }

    public void setFaultDesc(String faultDesc) 
    {
        this.faultDesc = faultDesc;
    }

    public String getFaultDesc() 
    {
        return faultDesc;
    }

    public void setReportUserId(Long reportUserId) 
    {
        this.reportUserId = reportUserId;
    }

    public Long getReportUserId() 
    {
        return reportUserId;
    }

    public void setReportTime(Date reportTime) 
    {
        this.reportTime = reportTime;
    }

    public Date getReportTime() 
    {
        return reportTime;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setHandlerUserId(Long handlerUserId) 
    {
        this.handlerUserId = handlerUserId;
    }

    public Long getHandlerUserId() 
    {
        return handlerUserId;
    }

    public void setHandleTime(Date handleTime) 
    {
        this.handleTime = handleTime;
    }

    public Date getHandleTime() 
    {
        return handleTime;
    }

    public void setHandleResult(String handleResult) 
    {
        this.handleResult = handleResult;
    }

    public String getHandleResult() 
    {
        return handleResult;
    }

    public void setFinishTime(Date finishTime) 
    {
        this.finishTime = finishTime;
    }

    public Date getFinishTime() 
    {
        return finishTime;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public void setDeviceCode(String deviceCode)
    {
        this.deviceCode=deviceCode;
    }

    public String getDeviceCode()
    {
        return deviceCode;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName=deviceName;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setLocation(String location)
    {
        this.location=location;
    }

    public String getLocation()
    {
        return location;
    }

    public void setReportUserName(String reportUserName)
    {
        this.reportUserName=reportUserName;
    }

    public String getReportUserName()
    {
        return reportUserName;
    }

    public void setHandlerName(String handlerName)
    {
        this.handlerName=handlerName;
    }

    public String getHandlerName()
    {
        return handlerName;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("deviceId", getDeviceId())
            .append("faultType", getFaultType())
            .append("faultDesc", getFaultDesc())
            .append("reportUserId", getReportUserId())
            .append("reportTime", getReportTime())
            .append("status", getStatus())
            .append("handlerUserId", getHandlerUserId())
            .append("handleTime", getHandleTime())
            .append("handleResult", getHandleResult())
            .append("finishTime", getFinishTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
