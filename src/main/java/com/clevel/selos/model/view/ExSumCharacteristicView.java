package com.clevel.selos.model.view;

import org.terracotta.modules.ehcache.transaction.SerializedReadCommittedClusteredSoftLock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExSumCharacteristicView implements Serializable {

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

    public ExSumCharacteristicView() {
        reset();
    }

    public void reset() {
        this.customer = "";
        this.currentDBR = BigDecimal.ZERO;
        this.finalDBR = BigDecimal.ZERO;
        this.income = BigDecimal.ZERO;
        this.recommendedWCNeed = BigDecimal.ZERO;
        this.actualWC = BigDecimal.ZERO;
        this.startBusinessDate = new Date();
        this.yearInBusiness = "";
        this.salePerYearBDM = BigDecimal.ZERO;
        this.salePerYearUW = BigDecimal.ZERO;
        this.groupSaleBDM = BigDecimal.ZERO;
        this.groupSaleUW = BigDecimal.ZERO;
        this.groupExposureBDM = BigDecimal.ZERO;
        this.groupExposureUW = BigDecimal.ZERO;
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
}
