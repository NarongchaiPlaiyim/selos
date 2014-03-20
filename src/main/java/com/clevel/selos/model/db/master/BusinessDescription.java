package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "mst_business_desc")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class BusinessDescription implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "businessgroup_id")
    private BusinessGroup businessGroup;

    @Column(name = "name", length=500)
    private String name;

    @Column(name = "tmb_Code")
    private String tmbCode;

    @Column(name = "income_factor")
    private BigDecimal incomeFactor;

    @Column(name = "isic_code")
    private String isicCode;

    @Column(name = "ar")
    private BigDecimal ar;

    @Column(name = "ap")
    private BigDecimal ap;

    @Column(name = "inv")
    private BigDecimal inv;

    @Column(name = "deviate")
    private String deviate;

    @Column(name = "esr")
    private String esr;

    @Column(name = "negative", length = 5)
    private String negative;

    @Column(name = "high_risk", length = 5)
    private String highRisk;

    @Column(name = "suspend", length = 5)
    private String suspend;

    @Column(name = "business_comment")
    private String comment;

    @Column(name = "allow_deviate", length = 100)
    private String allowDeviate;

    @Column(name = "deviated_by", length = 100)
    private String deviatedBy;

    @Column(name = "business_permission", length = 5)
    private String businessPermission;

    @Column(name = "business_permission_desc")
    private String businessPermissionDesc;

    @Column(name = "factory4", length = 500)
    private String factory4;

    @Column(name = "food_and_drug", length = 500)
    private String foodAndDrug;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "create_by")
    private User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "modify_by")
    private User modifyBy;

    @Column(name = "active")
    private int active;

    @Column(name = "cog")
    private BigDecimal cog;

    @Column(name = "description", length=500)
    private String description;

    public BusinessDescription() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTmbCode() {
        return tmbCode;
    }

    public void setTmbCode(String tmbCode) {
        this.tmbCode = tmbCode;
    }

    public BigDecimal getIncomeFactor() {
        return incomeFactor;
    }

    public void setIncomeFactor(BigDecimal incomeFactor) {
        this.incomeFactor = incomeFactor;
    }

    public String getIsicCode() {
        return isicCode;
    }

    public void setIsicCode(String isicCode) {
        this.isicCode = isicCode;
    }

    public BigDecimal getAr() {
        return ar;
    }

    public void setAr(BigDecimal ar) {
        this.ar = ar;
    }

    public BigDecimal getAp() {
        return ap;
    }

    public void setAp(BigDecimal ap) {
        this.ap = ap;
    }

    public BigDecimal getInv() {
        return inv;
    }

    public void setInv(BigDecimal inv) {
        this.inv = inv;
    }

    public BusinessGroup getBusinessGroup() {
        return businessGroup;
    }

    public void setBusinessGroup(BusinessGroup businessGroup) {
        this.businessGroup = businessGroup;
    }

    public String getDeviate() {
        return deviate;
    }

    public void setDeviate(String deviate) {
        this.deviate = deviate;
    }

    public String getEsr() {
        return esr;
    }

    public void setEsr(String esr) {
        this.esr = esr;
    }

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public String getHighRisk() {
        return highRisk;
    }

    public void setHighRisk(String highRisk) {
        this.highRisk = highRisk;
    }

    public String getSuspend() {
        return suspend;
    }

    public void setSuspend(String suspend) {
        this.suspend = suspend;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAllowDeviate() {
        return allowDeviate;
    }

    public void setAllowDeviate(String allowDeviate) {
        this.allowDeviate = allowDeviate;
    }

    public String getDeviatedBy() {
        return deviatedBy;
    }

    public void setDeviatedBy(String deviatedBy) {
        this.deviatedBy = deviatedBy;
    }

    public String getBusinessPermission() {
        return businessPermission;
    }

    public void setBusinessPermission(String businessPermission) {
        this.businessPermission = businessPermission;
    }

    public String getBusinessPermissionDesc() {
        return businessPermissionDesc;
    }

    public void setBusinessPermissionDesc(String businessPermissionDesc) {
        this.businessPermissionDesc = businessPermissionDesc;
    }

    public String getFactory4() {
        return factory4;
    }

    public void setFactory4(String factory4) {
        this.factory4 = factory4;
    }

    public String getFoodAndDrug() {
        return foodAndDrug;
    }

    public void setFoodAndDrug(String foodAndDrug) {
        this.foodAndDrug = foodAndDrug;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public BigDecimal getCog() {
        return cog;
    }

    public void setCog(BigDecimal cog) {
        this.cog = cog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("businessGroup", businessGroup).
                append("name", name).
                append("tmbCode", tmbCode).
                append("incomeFactor", incomeFactor).
                append("isicCode", isicCode).
                append("ar", ar).
                append("ap", ap).
                append("inv", inv).
                append("deviate", deviate).
                append("esr", esr).
                append("negative", negative).
                append("highRisk", highRisk).
                append("suspend", suspend).
                append("comment", comment).
                append("allowDeviate", allowDeviate).
                append("deviatedBy", deviatedBy).
                append("businessPermission", businessPermission).
                append("businessPermissionDesc", businessPermissionDesc).
                append("factory4", factory4).
                append("foodAndDrug", foodAndDrug).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("modifyBy", modifyBy).
                append("active", active).
                append("cog", cog).
                toString();
    }
}
