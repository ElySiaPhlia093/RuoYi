package com.ruoyi.device.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 备件库存对象 biz_part
 * 
 * @author xlt
 * @date 2026-10-23
 */
public class BizPart extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long partId;

    /** 备件编号 */
    @Excel(name = "备件编号")
    private String partCode;

    /** 备件名称 */
    @Excel(name = "备件名称")
    private String partName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String spec;

    /** 计量单位 */
    @Excel(name = "单位")
    private String unit;

    /** 当前库存 */
    @Excel(name = "当前库存")
    private Integer stock;

    /** 安全库存 */
    @Excel(name = "安全库存")
    private Integer safetyStock;

    /** 单价（元） */
    @Excel(name = "单价")
    private BigDecimal unitPrice;

    /** 供应商 */
    @Excel(name = "供应商")
    private String supplier;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 备注 */
    private String remark;

    /** 删除标志 */
    private String delFlag;

    public void setPartId(Long partId) { this.partId = partId; }
    public Long getPartId() { return partId; }

    public void setPartCode(String partCode) { this.partCode = partCode; }
    public String getPartCode() { return partCode; }

    public void setPartName(String partName) { this.partName = partName; }
    public String getPartName() { return partName; }

    public void setSpec(String spec) { this.spec = spec; }
    public String getSpec() { return spec; }

    public void setUnit(String unit) { this.unit = unit; }
    public String getUnit() { return unit; }

    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getStock() { return stock; }

    public void setSafetyStock(Integer safetyStock) { this.safetyStock = safetyStock; }
    public Integer getSafetyStock() { return safetyStock; }

    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getUnitPrice() { return unitPrice; }

    public void setSupplier(String supplier) { this.supplier = supplier; }
    public String getSupplier() { return supplier; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    public void setRemark(String remark) { this.remark = remark; }
    public String getRemark() { return remark; }

    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
    public String getDelFlag() { return delFlag; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("partId", getPartId())
            .append("partCode", getPartCode())
            .append("partName", getPartName())
            .append("spec", getSpec())
            .append("unit", getUnit())
            .append("stock", getStock())
            .append("safetyStock", getSafetyStock())
            .append("unitPrice", getUnitPrice())
            .append("supplier", getSupplier())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
