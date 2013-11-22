package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.Disbursement;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProposeCreditDetailView implements Serializable {
    private int requestType;
    private int isRefinance;
    private ProductProgram  productProgram;
    private CreditType creditType;
    private String productCode;
    private String projectCode;
    private String borrowerName;
    private BigDecimal limit;
    private BigDecimal PCEPercent;
    private BigDecimal PCEAmount;
    private int reducePriceFlag;
    private int reduceFrontEndFee;
    private boolean reduceFlag;
    private boolean reduceFrontEndFlag;
    private BigDecimal standardPrice;
    private BigDecimal suggestPrice;
    private BigDecimal frontEndFee;
    private String remark;
    private BigDecimal holdLimitAmount;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    private BigDecimal finalPrice;
    private BigDecimal tenor;
    private Disbursement disbursement;
    private BigDecimal purpose;

    // master
//    private loanPurpose

    private List<CreditTierDetailView> creditTierDetailViewList;

    public ProposeCreditDetailView(){
        reset();
    }

    public void reset(){
        this.requestType=0;
        this.isRefinance=0;
        this.productProgram = new ProductProgram();
        this.creditType = new CreditType();
        this.disbursement = new Disbursement();
        this.productCode = "";
        this.projectCode = "";
        this.borrowerName = "";
        this.limit = BigDecimal.ZERO;
        this.PCEPercent = BigDecimal.ZERO;
        this.PCEAmount = BigDecimal.ZERO;

        this.reducePriceFlag = 0;
        this.reduceFrontEndFee = 0;
        this.standardPrice = BigDecimal.ZERO;
        this.suggestPrice = BigDecimal.ZERO;
        this.frontEndFee = BigDecimal.ZERO;
        this.remark = "";
        this.holdLimitAmount = BigDecimal.ZERO;
        this.finalPrice = BigDecimal.ZERO;
        this.tenor = BigDecimal.ZERO;
        this.purpose = BigDecimal.ZERO;
        this.creditTierDetailViewList = new ArrayList<CreditTierDetailView>();

    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getRefinance() {
        return isRefinance;
    }

    public void setRefinance(int refinance) {
        isRefinance = refinance;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getPCEPercent() {
        return PCEPercent;
    }

    public void setPCEPercent(BigDecimal PCEPercent) {
        this.PCEPercent = PCEPercent;
    }

    public BigDecimal getPCEAmount() {
        return PCEAmount;
    }

    public void setPCEAmount(BigDecimal PCEAmount) {
        this.PCEAmount = PCEAmount;
    }

    public int getReducePriceFlag() {
        return reducePriceFlag;
    }

    public void setReducePriceFlag(int reducePriceFlag) {
        this.reducePriceFlag = reducePriceFlag;
    }

    public int getReduceFrontEndFee() {
        return reduceFrontEndFee;
    }

    public void setReduceFrontEndFee(int reduceFrontEndFee) {
        this.reduceFrontEndFee = reduceFrontEndFee;
    }

    public BigDecimal getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(BigDecimal standardPrice) {
        this.standardPrice = standardPrice;
    }

    public BigDecimal getSuggestPrice() {
        return suggestPrice;
    }

    public void setSuggestPrice(BigDecimal suggestPrice) {
        this.suggestPrice = suggestPrice;
    }

    public BigDecimal getFrontEndFee() {
        return frontEndFee;
    }

    public void setFrontEndFee(BigDecimal frontEndFee) {
        this.frontEndFee = frontEndFee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getHoldLimitAmount() {
        return holdLimitAmount;
    }

    public void setHoldLimitAmount(BigDecimal holdLimitAmount) {
        this.holdLimitAmount = holdLimitAmount;
    }

    public List<CreditTierDetailView> getCreditTierDetailViewList() {
        return creditTierDetailViewList;
    }

    public void setCreditTierDetailViewList(List<CreditTierDetailView> creditTierDetailViewList) {
        this.creditTierDetailViewList = creditTierDetailViewList;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public BigDecimal getTenor() {
        return tenor;
    }

    public void setTenor(BigDecimal tenor) {
        this.tenor = tenor;
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

    public Disbursement getDisbursement() {
        return disbursement;
    }

    public void setDisbursement(Disbursement disbursement) {
        this.disbursement = disbursement;
    }

    public BigDecimal getPurpose() {
        return purpose;
    }

    public void setPurpose(BigDecimal purpose) {
        this.purpose = purpose;
    }

    public boolean isReduceFlag() {
        if(this.reducePriceFlag==1 ){
            reduceFlag =  true;
        }else if(this.reducePriceFlag==0 ){
            reduceFlag = false;
        }
        return reduceFlag;
    }

    public void setReduceFlag(boolean reduceFlag) {
        this.reduceFlag = reduceFlag;
    }

    public boolean isReduceFrontEndFlag() {
        if(this.reduceFrontEndFee==1 ){
            reduceFrontEndFlag =  true;
        }else if(this.reduceFrontEndFee==0 ){
            reduceFrontEndFlag = false;
        }
        return reduceFrontEndFlag;
    }

    public void setReduceFrontEndFlag(boolean reduceFrontEndFlag) {
        this.reduceFrontEndFlag = reduceFrontEndFlag;
    }


}
