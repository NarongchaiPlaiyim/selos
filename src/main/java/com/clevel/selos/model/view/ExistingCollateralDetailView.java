package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ExistingCollateralDetailView implements Serializable {
    private long id;
    private PotentialCollateralView potentialCollateralView;
    private CollateralTypeView collateralTypeView;
    private String owner;
    private RelationView relationView;
    private Date appraisalDate;
    private String collateralNumber;
    private String collateralLocation;
    private String remark;
    private String cusName;
    private String accountNumber;
    private String accountSuffix;
    private String productProgram;
    private String creditFacility;//todo: Change creditFacility to view object?
    private BigDecimal limit;
    private String mortgageType;//todo: Change mortgageType to view object?
    private BigDecimal appraisalValue;
    private BigDecimal mortgageValue;

//    private List<CreditTypeDetailView> creditTypeDetailViewList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PotentialCollateralView getPotentialCollateralView() {
        return potentialCollateralView;
    }

    public void setPotentialCollateralView(PotentialCollateralView potentialCollateralView) {
        this.potentialCollateralView = potentialCollateralView;
    }

    public CollateralTypeView getCollateralTypeView() {
        return collateralTypeView;
    }

    public void setCollateralTypeView(CollateralTypeView collateralTypeView) {
        this.collateralTypeView = collateralTypeView;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public RelationView getRelationView() {
        return relationView;
    }

    public void setRelationView(RelationView relationView) {
        this.relationView = relationView;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public String getCollateralNumber() {
        return collateralNumber;
    }

    public void setCollateralNumber(String collateralNumber) {
        this.collateralNumber = collateralNumber;
    }

    public String getCollateralLocation() {
        return collateralLocation;
    }

    public void setCollateralLocation(String collateralLocation) {
        this.collateralLocation = collateralLocation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountSuffix() {
        return accountSuffix;
    }

    public void setAccountSuffix(String accountSuffix) {
        this.accountSuffix = accountSuffix;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getCreditFacility() {
        return creditFacility;
    }

    public void setCreditFacility(String creditFacility) {
        this.creditFacility = creditFacility;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public String getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public BigDecimal getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(BigDecimal mortgageValue) {
        this.mortgageValue = mortgageValue;
    }
}
