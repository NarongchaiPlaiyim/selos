package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class BorrowerCharacteristicExSumReport extends ReportModel{

    //ExSumCharacteristicView
    private String customer;
    private BigDecimal currentDBR;
    private BigDecimal finalDBR;
    private BigDecimal income;
    private BigDecimal recommendedWCNeed;
    private BigDecimal actualWC;
    private Date startBusinessDate;
    private String yearInBusiness;
    private BigDecimal salePerYearBDM;
    private BigDecimal salePerYearUW;
    private BigDecimal groupSaleBDM;
    private BigDecimal groupSaleUW;
    private BigDecimal groupExposureBDM;
    private BigDecimal groupExposureUW;

    //ExSumBusinessInfoView
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

    private String businessOperationActivity;
    private String businessPermission;
    private Date expiryDate;


    public BorrowerCharacteristicExSumReport() {
        this.customer = "";
        this.yearInBusiness = "";

        this.bizProvince = "";
        this.bizType = "";
        this.bizGroup = "";
        this.bizCode = "";
        this.bizDesc = "";
        this.qualitativeClass = "";

        this.businessOperationActivity = "";
        this.businessPermission = "";
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public BigDecimal getCurrentDBR() {
        return currentDBR;
    }

    public void setCurrentDBR(BigDecimal currentDBR) {
        this.currentDBR = currentDBR;
    }

    public BigDecimal getFinalDBR() {
        return finalDBR;
    }

    public void setFinalDBR(BigDecimal finalDBR) {
        this.finalDBR = finalDBR;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getRecommendedWCNeed() {
        return recommendedWCNeed;
    }

    public void setRecommendedWCNeed(BigDecimal recommendedWCNeed) {
        this.recommendedWCNeed = recommendedWCNeed;
    }

    public BigDecimal getActualWC() {
        return actualWC;
    }

    public void setActualWC(BigDecimal actualWC) {
        this.actualWC = actualWC;
    }

    public Date getStartBusinessDate() {
        return startBusinessDate;
    }

    public void setStartBusinessDate(Date startBusinessDate) {
        this.startBusinessDate = startBusinessDate;
    }

    public String getYearInBusiness() {
        return yearInBusiness;
    }

    public void setYearInBusiness(String yearInBusiness) {
        this.yearInBusiness = yearInBusiness;
    }

    public BigDecimal getSalePerYearBDM() {
        return salePerYearBDM;
    }

    public void setSalePerYearBDM(BigDecimal salePerYearBDM) {
        this.salePerYearBDM = salePerYearBDM;
    }

    public BigDecimal getSalePerYearUW() {
        return salePerYearUW;
    }

    public void setSalePerYearUW(BigDecimal salePerYearUW) {
        this.salePerYearUW = salePerYearUW;
    }

    public BigDecimal getGroupSaleBDM() {
        return groupSaleBDM;
    }

    public void setGroupSaleBDM(BigDecimal groupSaleBDM) {
        this.groupSaleBDM = groupSaleBDM;
    }

    public BigDecimal getGroupSaleUW() {
        return groupSaleUW;
    }

    public void setGroupSaleUW(BigDecimal groupSaleUW) {
        this.groupSaleUW = groupSaleUW;
    }

    public BigDecimal getGroupExposureBDM() {
        return groupExposureBDM;
    }

    public void setGroupExposureBDM(BigDecimal groupExposureBDM) {
        this.groupExposureBDM = groupExposureBDM;
    }

    public BigDecimal getGroupExposureUW() {
        return groupExposureUW;
    }

    public void setGroupExposureUW(BigDecimal groupExposureUW) {
        this.groupExposureUW = groupExposureUW;
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

    public String getBusinessOperationActivity() {
        return businessOperationActivity;
    }

    public void setBusinessOperationActivity(String businessOperationActivity) {
        this.businessOperationActivity = businessOperationActivity;
    }

    public String getBusinessPermission() {
        return businessPermission;
    }

    public void setBusinessPermission(String businessPermission) {
        this.businessPermission = businessPermission;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("customer", customer).
                append("currentDBR", currentDBR).
                append("finalDBR", finalDBR).
                append("income", income).
                append("recommendedWCNeed", recommendedWCNeed).
                append("actualWC", actualWC).
                append("startBusinessDate", startBusinessDate).
                append("yearInBusiness", yearInBusiness).
                append("salePerYearBDM", salePerYearBDM).
                append("salePerYearUW", salePerYearUW).
                append("groupSaleBDM", groupSaleBDM).
                append("groupSaleUW", groupSaleUW).
                append("groupExposureBDM", groupExposureBDM).
                append("groupExposureUW", groupExposureUW).
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
                append("businessOperationActivity", businessOperationActivity).
                append("businessPermission", businessPermission).
                append("expiryDate", expiryDate).
                toString();
    }
}
