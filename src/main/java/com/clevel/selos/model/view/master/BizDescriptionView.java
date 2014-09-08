package com.clevel.selos.model.view.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class BizDescriptionView implements Serializable{
    private int id;
    private int businessGroupId;
    private String name;
    private String tmbCode;
    private BigDecimal incomeFactor;
    private String isicCode;
    private BigDecimal ar;
    private BigDecimal ap;
    private BigDecimal inv;
    private String deviate;
    private String esr;
    private String negative;
    private String highRisk;
    private String suspend;
    private String comment;
    private String allowDeviate;
    private String deviatedBy;
    private String businessPermission;
    private String businessPermissionDesc;
    private String factory4;
    private String foodAndDrug;
    private int active;
    private BigDecimal cog;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusinessGroupId() {
        return businessGroupId;
    }

    public void setBusinessGroupId(int businessGroupId) {
        this.businessGroupId = businessGroupId;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("businessGroupId", businessGroupId)
                .append("name", name)
                .append("tmbCode", tmbCode)
                .append("incomeFactor", incomeFactor)
                .append("isicCode", isicCode)
                .append("ar", ar)
                .append("ap", ap)
                .append("inv", inv)
                .append("deviate", deviate)
                .append("esr", esr)
                .append("negative", negative)
                .append("highRisk", highRisk)
                .append("suspend", suspend)
                .append("comment", comment)
                .append("allowDeviate", allowDeviate)
                .append("deviatedBy", deviatedBy)
                .append("businessPermission", businessPermission)
                .append("businessPermissionDesc", businessPermissionDesc)
                .append("factory4", factory4)
                .append("foodAndDrug", foodAndDrug)
                .append("active", active)
                .append("cog", cog)
                .append("description", description)
                .toString();
    }
}
