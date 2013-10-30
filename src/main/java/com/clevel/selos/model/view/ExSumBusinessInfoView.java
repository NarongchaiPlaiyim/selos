package com.clevel.selos.model.view;

import java.math.BigDecimal;

public class ExSumBusinessInfoView {

    private BigDecimal netFixAsset;
    private int     noOfEmployee;
    private String  bizProvince;
    private String  bizType;
    private String  bizGroup;
    private String  bizCode;
    private String  bizDesc;
    private String  qualitativeClass;
    private BigDecimal bizSize;
    private Double BDM;
    private Double UW;
    private Double AR;
    private Double AP;
    private Double INV;

    public ExSumBusinessInfoView() {
        reset();
    }

    public void reset() {
        this.netFixAsset = new BigDecimal(0);
        this.noOfEmployee = 0;
        this.bizProvince  = "";
        this.bizType      = "";
        this.bizGroup     = "";
        this.bizCode      = "";
        this.bizDesc      = "";
        this.qualitativeClass = "";
        this.bizSize = new BigDecimal(0);
        this.BDM = 0.00;
        this.UW = 0.00;
        this.AR = 0.00;
        this.AP = 0.00;
        this.INV  = 0.00;

    }

    public BigDecimal getNetFixAsset() {
        return netFixAsset;
    }

    public void setNetFixAsset(BigDecimal netFixAsset) {
        this.netFixAsset = netFixAsset;
    }

    public int getNoOfEmployee() {
        return noOfEmployee;
    }

    public void setNoOfEmployee(int noOfEmployee) {
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

    public Double getBDM() {
        return BDM;
    }

    public void setBDM(Double BDM) {
        this.BDM = BDM;
    }

    public Double getUW() {
        return UW;
    }

    public void setUW(Double UW) {
        this.UW = UW;
    }

    public Double getAR() {
        return AR;
    }

    public void setAR(Double AR) {
        this.AR = AR;
    }

    public Double getAP() {
        return AP;
    }

    public void setAP(Double AP) {
        this.AP = AP;
    }

    public Double getINV() {
        return INV;
    }

    public void setINV(Double INV) {
        this.INV = INV;
    }
}
