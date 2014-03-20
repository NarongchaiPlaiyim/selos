package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class ExistingCollateralRelatedDecisionReport extends ReportModel{


    private int count;
    private String potentialCollateral;
    private String collateralType;
    private String owner;
    private String relation;
    private Date appraisalDate;
    private String collateralNumber;
    private String collateralLocation;
    private String remark;
    private String cusName;
    private String accountNumber;
    private String accountSuffix;
    private String productProgram;
    private String creditFacility;
    private BigDecimal limit;
    private String mortgageType;
    private BigDecimal appraisalValue;
    private BigDecimal mortgageValue;

    public ExistingCollateralRelatedDecisionReport() {
        potentialCollateral = getDefaultString();
        collateralType = getDefaultString();
        owner = getDefaultString();
        relation = getDefaultString();
        appraisalDate = new Date();
        collateralNumber = getDefaultString();
        collateralLocation = getDefaultString();
        remark = getDefaultString();
        cusName = getDefaultString();
        accountNumber = getDefaultString();
        accountSuffix = getDefaultString();
        productProgram = getDefaultString();
        creditFacility = getDefaultString();
        limit = BigDecimal.ZERO;
        mortgageType = getDefaultString();
        appraisalValue = getDefaultBigDecimal();
        mortgageValue = getDefaultBigDecimal();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public String getCollateralLocation() {
        return collateralLocation;
    }

    public void setCollateralLocation(String collateralLocation) {
        this.collateralLocation = collateralLocation;
    }

    public String getCollateralNumber() {
        return collateralNumber;
    }

    public void setCollateralNumber(String collateralNumber) {
        this.collateralNumber = collateralNumber;
    }

    public String getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
    }

    public String getCreditFacility() {
        return creditFacility;
    }

    public void setCreditFacility(String creditFacility) {
        this.creditFacility = creditFacility;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
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

    public BigDecimal getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(BigDecimal mortgageValue) {
        this.mortgageValue = mortgageValue;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPotentialCollateral() {
        return potentialCollateral;
    }

    public void setPotentialCollateral(String potentialCollateral) {
        this.potentialCollateral = potentialCollateral;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("potentialCollateral", potentialCollateral)
                .append("collateralType", collateralType)
                .append("owner", owner)
                .append("relation", relation)
                .append("appraisalDate", appraisalDate)
                .append("collateralNumber", collateralNumber)
                .append("collateralLocation", collateralLocation)
                .append("remark", remark)
                .append("cusName", cusName)
                .append("accountNumber", accountNumber)
                .append("accountSuffix", accountSuffix)
                .append("creditFacility", creditFacility)
                .append("limit", limit)
                .append("mortgageType", mortgageType)
                .append("appraisalValue", appraisalValue)
                .append("mortgageValue", mortgageValue)
                .append("productProgram", productProgram)
                .toString();
    }
}
