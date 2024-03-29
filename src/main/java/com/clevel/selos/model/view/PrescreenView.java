package com.clevel.selos.model.view;


import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PrescreenView implements Serializable {
    private long id;
    protected ProductGroup productGroup;
    private int searchBy;
    private String searchId;
    private Date expectedSubmitDate;
    private BigDecimal groupExposure;
    private BigDecimal groupIncome;
    private Province businessLocation;
    private Date registerDate;
    private Date referDate;
    private ReferredExperience referredExperience;
    private int tcg;
    private int refinanceIn;
    private Bank refinanceInBank;
    private int refinanceOut;
    private Bank refinanceOutBank;
    private BorrowingType borrowingType;
    private String checkerId;
    private String remark;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    private int modifyFlag;

    private Country countryOfRegister;

    public PrescreenView() {

    }

    public void reset() {
        this.productGroup = new ProductGroup();
        this.expectedSubmitDate = null;
        this.businessLocation = new Province();
        this.registerDate = null;
        this.tcg = 0;
        this.refinanceIn = 0;
        this.refinanceInBank = new Bank();
        this.refinanceOut = 0;
        this.refinanceOutBank = new Bank();
        this.referredExperience = new ReferredExperience();
        this.borrowingType = new BorrowingType();
        this.countryOfRegister = new Country();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public int getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(int searchBy) {
        this.searchBy = searchBy;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public BigDecimal getGroupExposure() {
        return groupExposure;
    }

    public void setGroupExposure(BigDecimal groupExposure) {
        this.groupExposure = groupExposure;
    }

    public BigDecimal getGroupIncome() {
        return groupIncome;
    }

    public void setGroupIncome(BigDecimal groupIncome) {
        this.groupIncome = groupIncome;
    }

    public Province getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(Province businessLocation) {
        this.businessLocation = businessLocation;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getReferDate() {
        return referDate;
    }

    public void setReferDate(Date referDate) {
        this.referDate = referDate;
    }

    public ReferredExperience getReferredExperience() {
        return referredExperience;
    }

    public void setReferredExperience(ReferredExperience referredExperience) {
        this.referredExperience = referredExperience;
    }

    public int getTcg() {
        return tcg;
    }

    public void setTcg(int tcg) {
        this.tcg = tcg;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public BorrowingType getBorrowingType() {
        return borrowingType;
    }

    public void setBorrowingType(BorrowingType borrowingType) {
        this.borrowingType = borrowingType;
    }

    public int getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(int modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    public int getRefinanceIn() {
        return refinanceIn;
    }

    public void setRefinanceIn(int refinanceIn) {
        this.refinanceIn = refinanceIn;
    }

    public Bank getRefinanceInBank() {
        return refinanceInBank;
    }

    public void setRefinanceInBank(Bank refinanceInBank) {
        this.refinanceInBank = refinanceInBank;
    }

    public int getRefinanceOut() {
        return refinanceOut;
    }

    public void setRefinanceOut(int refinanceOut) {
        this.refinanceOut = refinanceOut;
    }

    public Bank getRefinanceOutBank() {
        return refinanceOutBank;
    }

    public void setRefinanceOutBank(Bank refinanceOutBank) {
        this.refinanceOutBank = refinanceOutBank;
    }

    public Country getCountryOfRegister() {
        return countryOfRegister;
    }

    public void setCountryOfRegister(Country countryOfRegister) {
        this.countryOfRegister = countryOfRegister;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("productGroup", productGroup)
                .append("searchBy", searchBy)
                .append("searchId", searchId)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("groupExposure", groupExposure)
                .append("groupIncome", groupIncome)
                .append("businessLocation", businessLocation)
                .append("registerDate", registerDate)
                .append("referDate", referDate)
                .append("referredExperience", referredExperience)
                .append("tcg", tcg)
                .append("refinanceIn", refinanceIn)
                .append("refinanceInBank", refinanceInBank)
                .append("refinanceOut", refinanceOut)
                .append("refinanceOutBank", refinanceOutBank)
                .append("borrowingType", borrowingType)
                .append("checkerId", checkerId)
                .append("remark", remark)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("modifyFlag", modifyFlag)
                .toString();
    }
}
