package com.ruoyi.device.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备台账对象 biz_device
 * 
 * @author xlt
 * @date 2026-10-16
 */
public class BizDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long deviceId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceCode;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备类型（字典 device_type） */
    @Excel(name = "设备类型", readConverterExp = "cnc=数控机床,injection=注塑机,compressor=空压机,conveyor=传送带,agv=AGV,detector=检测设备")
    private String deviceType;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private String model;

    /** 生产厂商 */
    @Excel(name = "生产厂商")
    private String manufacturer;

    /** 安装位置 */
    @Excel(name = "安装位置")
    private String location;

    /** 设备状态（字典 device_status） */
    @Excel(name = "设备状态", readConverterExp = "running=运行,scrapped=报废,repairing=维修,stopped=停机,standby=待机")
    private String status;

    /** 采购日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采购日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseDate;

    /** 保修到期日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "保修到期日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date warrantyExpire;

    /** 负责人 → sys_user */
    @Excel(name = "负责人 → sys_user")
    private Long ownerUserId;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setDeviceCode(String deviceCode) 
    {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode() 
    {
        return deviceCode;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setDeviceType(String deviceType) 
    {
        this.deviceType = deviceType;
    }

    public String getDeviceType() 
    {
        return deviceType;
    }

    public void setModel(String model) 
    {
        this.model = model;
    }

    public String getModel() 
    {
        return model;
    }

    public void setManufacturer(String manufacturer) 
    {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer() 
    {
        return manufacturer;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setPurchaseDate(Date purchaseDate) 
    {
        this.purchaseDate = purchaseDate;
    }

    public Date getPurchaseDate() 
    {
        return purchaseDate;
    }

    public void setWarrantyExpire(Date warrantyExpire) 
    {
        this.warrantyExpire = warrantyExpire;
    }

    public Date getWarrantyExpire() 
    {
        return warrantyExpire;
    }

    public void setOwnerUserId(Long ownerUserId) 
    {
        this.ownerUserId = ownerUserId;
    }

    public Long getOwnerUserId() 
    {
        return ownerUserId;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceCode", getDeviceCode())
            .append("deviceName", getDeviceName())
            .append("deviceType", getDeviceType())
            .append("model", getModel())
            .append("manufacturer", getManufacturer())
            .append("location", getLocation())
            .append("status", getStatus())
            .append("purchaseDate", getPurchaseDate())
            .append("warrantyExpire", getWarrantyExpire())
            .append("ownerUserId", getOwnerUserId())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
