package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExSumBusinessInfoView implements Serializable {

    private BigDecimal netFixAsset;
    private BigDecimal noOfEmployee;
    private String bizProvince;
    private String bizType;
    private String bizGroup;
    private String bizCode;
    private String bizDesc;
    private String qualitativeClass;
    private BigDecimal bizSize;
    private BigDecimal BDM;
    private BigDecimal UW;
    private BigDecimal AR;
    private BigDecimal AP;
    private BigDecimal INV;

    public ExSumBusinessInfoView() {
        reset();
    }

    public void reset() {
        this.netFixAsset = BigDecimal.ZERO;
        this.noOfEmployee = BigDecimal.ZERO;
        this.bizProvince = "";
        this.bizType = "";
        this.bizGroup = "";
        this.bizCode = "";
        this.bizDesc = "";
        this.qualitativeClass = "";
        this.bizSize = BigDecimal.ZERO;
        this.BDM = BigDecimal.ZERO;
        this.UW = BigDecimal.ZERO;
        this.AR = BigDecimal.ZERO;
        this.AP = BigDecimal.ZERO;
        this.INV = BigDecimal.ZERO;
    }

    public BigDecimal getNetFixAsset() {
        return netFixAsset;
    }

    public void setNetFixAsset(BigDecimal netFixAsset) {
        this.netFixAsset = netFixAsset;
    }

    public BigDecimal getNoOfEmployee() {
        return noOfEmployee;
    }

    public void setNoOfEmployee(BigDecimal noOfEmployee) {
        this.noOfEmployee = noOfEmployee;
    }

    public String getBizProvince() {
        return bizProvince;
    }

    public void setBizProvince(String bizProvince) {
        this.bizProvince = bizProvince;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizGroup() {
        return bizGroup;
    }

    public void setBizGroup(String bizGroup) {
        this.bizGroup = bizGroup;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getBizDesc() {
        return bizDesc;
    }

    public void setBizDesc(String bizDesc) {
        this.bizDesc = bizDesc;
    }

    public String getQualitativeClass() {
        return qualitativeClass;
    }

    public void setQualitativeClass(String qualitativeClass) {
        this.qualitativeClass = qualitativeClass;
    }

    public BigDecimal getBizSize() {
        return bizSize;
    }

    public void setBizSize(BigDecimal bizSize) {
        this.bizSize = bizSize;
    }

    public BigDecimal getBDM() {
        return BDM;
    }

    public void setBDM(BigDecimal BDM) {
        this.BDM = BDM;
    }

    public BigDecimal getUW() {
        return UW;
    }

    public void setUW(BigDecimal UW) {
        this.UW = UW;
    }

    public BigDecimal getAR() {
        return AR;
    }

    public void setAR(BigDecimal AR) {
        this.AR = AR;
    }

    public BigDecimal getAP() {
        return AP;
    }

    public void setAP(BigDecimal AP) {
        this.AP = AP;
    }

    public BigDecimal getINV() {
        return INV;
    }

    public void setINV(BigDecimal INV) {
        this.INV = INV;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("netFixAsset", netFixAsset).
                append("noOfEmployee", noOfEmployee).
                append("bizProvince", bizProvince).
                append("bizType", bizType).
                append("bizGroup", bizGroup).
                append("bizCode", bizCode).
                append("bizDesc", bizDesc).
                append("qualitativeClass", qualitativeClass).
                append("bizSize", bizSize).
                append("BDM", BDM).
                append("UW", UW).
                append("AR", AR).
                append("AP", AP).
                append("INV", INV).
                toString();
    }
}
