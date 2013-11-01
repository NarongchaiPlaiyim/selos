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
    private boolean tcg;
    private boolean refinance;
    private Bank refinanceBank;
    private BorrowingType borrowingType;
    private String checkerId;
    private String remark;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    private int modifyFlag;

    public PrescreenView() {

    }

    public void reset() {
        this.productGroup = new ProductGroup();
        this.expectedSubmitDate = null;
        this.businessLocation = new Province();
        this.registerDate = null;
        this.tcg = false;
        this.refinance = false;
        this.refinanceBank = new Bank();
        this.referredExperience = new ReferredExperience();
        this.borrowingType = new BorrowingType();
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

    public boolean isTcg() {
        return tcg;
    }

    public void setTcg(boolean tcg) {
        this.tcg = tcg;
    }

    public boolean isRefinance() {
        return refinance;
    }

    public void setRefinance(boolean refinance) {
        this.refinance = refinance;
    }

    public Bank getRefinanceBank() {
        return refinanceBank;
    }

    public void setRefinanceBank(Bank refinanceBank) {
        this.refinanceBank = refinanceBank;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("productGroup", productGroup)
                .append("searchBy", searchBy)
                .append("searchId", searchId)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("businessLocation", businessLocation)
                .append("registerDate", registerDate)
                .append("referDate", referDate)
                .append("referredExperience", referredExperience)
                .append("tcg", tcg)
                .append("refinance", refinance)
                .append("refinanceBank", refinanceBank)
                .append("borrowingType", borrowingType)
                .append("checkerId", checkerId)
                .append("remark", remark)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
