package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.db.master.SubDistrict;
import com.clevel.selos.model.db.working.WorkCase;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BizInfoSummaryView {
    private long id;
    private String bizLocationName;
    private String isRental;
    private String ownerName;
    private String expiryDate;
    private String addressNo;
    private String addressMoo;
    private String addressBuilding;
    private String addressStreet;
    private Province province;
    private District district;
    private SubDistrict subDistrict;
    private String postCode;
    private String country;
    private String addressEng;
    private String phoneNo;
    private String extension;
    private String registrationDate;
    private String establishDate;
    private String bizInterviewInfo;
    private BigDecimal circulationAmount;
    private BigDecimal circulationPercentage;
    private BigDecimal productionCostsAmount;
    private BigDecimal productionCostsPercentage;
    private BigDecimal profitMarginAmount;
    private BigDecimal profitMarginPercentage;
    private BigDecimal operatingExpenseAmount;
    private BigDecimal operatingExpensePercentage;
    private BigDecimal earningsBeforeTaxAmount;
    private BigDecimal earningsBeforeTaxPercentage;
    private BigDecimal reduceInterestAmount;
    private BigDecimal reduceInterestPercentage;
    private BigDecimal reduceTaxAmount;
    private BigDecimal reduceTaxPercentage;
    private BigDecimal netMarginAmount;
    private BigDecimal netMarginPercentage;
    private String netFixAsset;
    private String noOfEmployee;
    private BigDecimal  sumIncomeAmount;
    private BigDecimal sumIncomePercent;
    private BigDecimal weightIncomeFactor;
    private BigDecimal  sumWeightInterviewedIncomeFactorPercent;
    private BigDecimal  sumWeightAR;
    private BigDecimal sumWeightAP;
    private WorkCase workCase;
    private Date createDate;
    private Date modifyDate;
    private List<BizInfoDetailView> bizInfoDetailList;

    public BizInfoSummaryView() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBizLocationName() {
        return bizLocationName;
    }

    public void setBizLocationName(String bizLocationName) {
        this.bizLocationName = bizLocationName;
    }

    public String getRental() {
        return isRental;
    }

    public void setRental(String rental) {
        isRental = rental;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getAddressMoo() {
        return addressMoo;
    }

    public void setAddressMoo(String addressMoo) {
        this.addressMoo = addressMoo;
    }

    public String getAddressBuilding() {
        return addressBuilding;
    }

    public void setAddressBuilding(String addressBuilding) {
        this.addressBuilding = addressBuilding;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public SubDistrict getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(SubDistrict subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressEng() {
        return addressEng;
    }

    public void setAddressEng(String addressEng) {
        this.addressEng = addressEng;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(String establishDate) {
        this.establishDate = establishDate;
    }

    public String getBizInterviewInfo() {
        return bizInterviewInfo;
    }

    public void setBizInterviewInfo(String bizInterviewInfo) {
        this.bizInterviewInfo = bizInterviewInfo;
    }

    public BigDecimal getCirculationAmount() {
        return circulationAmount;
    }

    public void setCirculationAmount(BigDecimal circulationAmount) {
        this.circulationAmount = circulationAmount;
    }

    public BigDecimal getCirculationPercentage() {
        return circulationPercentage;
    }

    public void setCirculationPercentage(BigDecimal circulationPercentage) {
        this.circulationPercentage = circulationPercentage;
    }

    public BigDecimal getProductionCostsAmount() {
        return productionCostsAmount;
    }

    public void setProductionCostsAmount(BigDecimal productionCostsAmount) {
        this.productionCostsAmount = productionCostsAmount;
    }

    public BigDecimal getProductionCostsPercentage() {
        return productionCostsPercentage;
    }

    public void setProductionCostsPercentage(BigDecimal productionCostsPercentage) {
        this.productionCostsPercentage = productionCostsPercentage;
    }

    public BigDecimal getProfitMarginAmount() {
        return profitMarginAmount;
    }

    public void setProfitMarginAmount(BigDecimal profitMarginAmount) {
        this.profitMarginAmount = profitMarginAmount;
    }

    public BigDecimal getProfitMarginPercentage() {
        return profitMarginPercentage;
    }

    public void setProfitMarginPercentage(BigDecimal profitMarginPercentage) {
        this.profitMarginPercentage = profitMarginPercentage;
    }

    public BigDecimal getOperatingExpenseAmount() {
        return operatingExpenseAmount;
    }

    public void setOperatingExpenseAmount(BigDecimal operatingExpenseAmount) {
        this.operatingExpenseAmount = operatingExpenseAmount;
    }

    public BigDecimal getOperatingExpensePercentage() {
        return operatingExpensePercentage;
    }

    public void setOperatingExpensePercentage(BigDecimal operatingExpensePercentage) {
        this.operatingExpensePercentage = operatingExpensePercentage;
    }

    public BigDecimal getEarningsBeforeTaxAmount() {
        return earningsBeforeTaxAmount;
    }

    public void setEarningsBeforeTaxAmount(BigDecimal earningsBeforeTaxAmount) {
        this.earningsBeforeTaxAmount = earningsBeforeTaxAmount;
    }

    public BigDecimal getEarningsBeforeTaxPercentage() {
        return earningsBeforeTaxPercentage;
    }

    public void setEarningsBeforeTaxPercentage(BigDecimal earningsBeforeTaxPercentage) {
        this.earningsBeforeTaxPercentage = earningsBeforeTaxPercentage;
    }

    public BigDecimal getReduceInterestAmount() {
        return reduceInterestAmount;
    }

    public void setReduceInterestAmount(BigDecimal reduceInterestAmount) {
        this.reduceInterestAmount = reduceInterestAmount;
    }

    public BigDecimal getReduceInterestPercentage() {
        return reduceInterestPercentage;
    }

    public void setReduceInterestPercentage(BigDecimal reduceInterestPercentage) {
        this.reduceInterestPercentage = reduceInterestPercentage;
    }

    public BigDecimal getReduceTaxAmount() {
        return reduceTaxAmount;
    }

    public void setReduceTaxAmount(BigDecimal reduceTaxAmount) {
        this.reduceTaxAmount = reduceTaxAmount;
    }

    public BigDecimal getReduceTaxPercentage() {
        return reduceTaxPercentage;
    }

    public void setReduceTaxPercentage(BigDecimal reduceTaxPercentage) {
        this.reduceTaxPercentage = reduceTaxPercentage;
    }

    public BigDecimal getNetMarginAmount() {
        return netMarginAmount;
    }

    public void setNetMarginAmount(BigDecimal netMarginAmount) {
        this.netMarginAmount = netMarginAmount;
    }

    public BigDecimal getNetMarginPercentage() {
        return netMarginPercentage;
    }

    public void setNetMarginPercentage(BigDecimal netMarginPercentage) {
        this.netMarginPercentage = netMarginPercentage;
    }

    public String getNetFixAsset() {
        return netFixAsset;
    }

    public void setNetFixAsset(String netFixAsset) {
        this.netFixAsset = netFixAsset;
    }

    public String getNoOfEmployee() {
        return noOfEmployee;
    }

    public void setNoOfEmployee(String noOfEmployee) {
        this.noOfEmployee = noOfEmployee;
    }

    public BigDecimal getSumIncomeAmount() {
        return sumIncomeAmount;
    }

    public void setSumIncomeAmount(BigDecimal sumIncomeAmount) {
        this.sumIncomeAmount = sumIncomeAmount;
    }

    public BigDecimal getSumIncomePercent() {
        return sumIncomePercent;
    }

    public void setSumIncomePercent(BigDecimal sumIncomePercent) {
        this.sumIncomePercent = sumIncomePercent;
    }

    public BigDecimal getWeightIncomeFactor() {
        return weightIncomeFactor;
    }

    public void setWeightIncomeFactor(BigDecimal weightIncomeFactor) {
        this.weightIncomeFactor = weightIncomeFactor;
    }

    public BigDecimal getSumWeightInterviewedIncomeFactorPercent() {
        return sumWeightInterviewedIncomeFactorPercent;
    }

    public void setSumWeightInterviewedIncomeFactorPercent(BigDecimal sumWeightInterviewedIncomeFactorPercent) {
        this.sumWeightInterviewedIncomeFactorPercent = sumWeightInterviewedIncomeFactorPercent;
    }

    public BigDecimal getSumWeightAR() {
        return sumWeightAR;
    }

    public void setSumWeightAR(BigDecimal sumWeightAR) {
        this.sumWeightAR = sumWeightAR;
    }

    public BigDecimal getSumWeightAP() {
        return sumWeightAP;
    }

    public void setSumWeightAP(BigDecimal sumWeightAP) {
        this.sumWeightAP = sumWeightAP;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
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

    public List<BizInfoDetailView> getBizInfoDetailList() {
        return bizInfoDetailList;
    }

    public void setBizInfoDetailList(List<BizInfoDetailView> bizInfoDetailList) {
        this.bizInfoDetailList = bizInfoDetailList;
    }
}
