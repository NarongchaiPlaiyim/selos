package com.clevel.selos.model.view;

import java.math.BigDecimal;
import java.util.Date;

public class ExSumCharacteristicView {

    private String customer;
    private Double currentDBR;
    private Double finalDBR;
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
        this.currentDBR = 0.00;
        this.finalDBR = 0.00;
        this.income = new BigDecimal(0);
        this.recommendedWCNeed = new BigDecimal(0);
        this.actualWC = new BigDecimal(0);
        this.salePerYearBDM = new BigDecimal(0);
        this.salePerYearUW = new BigDecimal(0);
        this.groupSaleBDM = new BigDecimal(0);
        this.groupSaleUW = new BigDecimal(0);
        this.groupExposureBDM = new BigDecimal(0);
        this.groupExposureUW = new BigDecimal(0);
        this.startBusinessDate = new Date();
        this.yearInBusiness = "";

    }


    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Double getCurrentDBR() {
        return currentDBR;
    }

    public void setCurrentDBR(Double currentDBR) {
        this.currentDBR = currentDBR;
    }

    public Double getFinalDBR() {
        return finalDBR;
    }

    public void setFinalDBR(Double finalDBR) {
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
