package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ApplicationTypeLevel implements Serializable {

    private String applicationNumber;               //1
    private Date processDate;                       //2
    private BigDecimal monthlyIncome;               //3
    private Date expectedSubmitDate;                //4
    private String customerEntity;                  //5
    private boolean existingSMECustomer;            //6
    private boolean sameSetOfBorrower;              //7
    private boolean refinanceInFlag;                //8
    private boolean refinanceOutFlag;               //9

    private String lendingReferType;                //#N/a
    private boolean BAFlag;                         //#N/a

    private BigDecimal finalGroupExposure;
    private String collateralPotertialForPricing;

    //AccountType #Bank Statement
    private List<AccountTypeLevel> accountType;

    //AttributeType
    AttributeTypeLevelApplication attribute;

    //BorrowerType
    private List<BorrowerTypeLevel> borrowerType;

    //BusinessType
    private List<BusinessTypeLevel> businessType;

    //ProductType
    private List<ProductTypeLevel> productType;

    public ApplicationTypeLevel() {
    }

    public ApplicationTypeLevel(String applicationNumber, Date processDate, BigDecimal monthlyIncome, Date expectedSubmitDate, String customerEntity, boolean existingSMECustomer, boolean sameSetOfBorrower, boolean refinanceInFlag, boolean refinanceOutFlag, String lendingReferType, boolean BAFlag, BigDecimal finalGroupExposure, String collateralPotertialForPricing, List<AccountTypeLevel> accountType, AttributeTypeLevelApplication attribute, List<BorrowerTypeLevel> borrowerType, List<BusinessTypeLevel> businessType, List<ProductTypeLevel> productType) {
        this.applicationNumber = applicationNumber;
        this.processDate = processDate;
        this.monthlyIncome = monthlyIncome;
        this.expectedSubmitDate = expectedSubmitDate;
        this.customerEntity = customerEntity;
        this.existingSMECustomer = existingSMECustomer;
        this.sameSetOfBorrower = sameSetOfBorrower;
        this.refinanceInFlag = refinanceInFlag;
        this.refinanceOutFlag = refinanceOutFlag;
        this.lendingReferType = lendingReferType;
        this.BAFlag = BAFlag;
        this.finalGroupExposure = finalGroupExposure;
        this.collateralPotertialForPricing = collateralPotertialForPricing;
        this.accountType = accountType;
        this.attribute = attribute;
        this.borrowerType = borrowerType;
        this.businessType = businessType;
        this.productType = productType;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public String getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(String customerEntity) {
        this.customerEntity = customerEntity;
    }

    public boolean isExistingSMECustomer() {
        return existingSMECustomer;
    }

    public void setExistingSMECustomer(boolean existingSMECustomer) {
        this.existingSMECustomer = existingSMECustomer;
    }

    public boolean isSameSetOfBorrower() {
        return sameSetOfBorrower;
    }

    public void setSameSetOfBorrower(boolean sameSetOfBorrower) {
        this.sameSetOfBorrower = sameSetOfBorrower;
    }

    public boolean isRefinanceInFlag() {
        return refinanceInFlag;
    }

    public void setRefinanceInFlag(boolean refinanceInFlag) {
        this.refinanceInFlag = refinanceInFlag;
    }

    public boolean isRefinanceOutFlag() {
        return refinanceOutFlag;
    }

    public void setRefinanceOutFlag(boolean refinanceOutFlag) {
        this.refinanceOutFlag = refinanceOutFlag;
    }

    public String getLendingReferType() {
        return lendingReferType;
    }

    public void setLendingReferType(String lendingReferType) {
        this.lendingReferType = lendingReferType;
    }

    public boolean isBAFlag() {
        return BAFlag;
    }

    public void setBAFlag(boolean BAFlag) {
        this.BAFlag = BAFlag;
    }

    public BigDecimal getFinalGroupExposure() {
        return finalGroupExposure;
    }

    public void setFinalGroupExposure(BigDecimal finalGroupExposure) {
        this.finalGroupExposure = finalGroupExposure;
    }

    public String getCollateralPotertialForPricing() {
        return collateralPotertialForPricing;
    }

    public void setCollateralPotertialForPricing(String collateralPotertialForPricing) {
        this.collateralPotertialForPricing = collateralPotertialForPricing;
    }

    public List<AccountTypeLevel> getAccountType() {
        return accountType;
    }

    public void setAccountType(List<AccountTypeLevel> accountType) {
        this.accountType = accountType;
    }

    public AttributeTypeLevelApplication getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeTypeLevelApplication attribute) {
        this.attribute = attribute;
    }

    public List<BorrowerTypeLevel> getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(List<BorrowerTypeLevel> borrowerType) {
        this.borrowerType = borrowerType;
    }

    public List<BusinessTypeLevel> getBusinessType() {
        return businessType;
    }

    public void setBusinessType(List<BusinessTypeLevel> businessType) {
        this.businessType = businessType;
    }

    public List<ProductTypeLevel> getProductType() {
        return productType;
    }

    public void setProductType(List<ProductTypeLevel> productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("applicationNumber", applicationNumber)
                .append("processDate", processDate)
                .append("monthlyIncome", monthlyIncome)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("customerEntity", customerEntity)
                .append("existingSMECustomer", existingSMECustomer)
                .append("sameSetOfBorrower", sameSetOfBorrower)
                .append("refinanceInFlag", refinanceInFlag)
                .append("refinanceOutFlag", refinanceOutFlag)
                .append("lendingReferType", lendingReferType)
                .append("BAFlag", BAFlag)
                .append("finalGroupExposure", finalGroupExposure)
                .append("collateralPotertialForPricing", collateralPotertialForPricing)
                .append("accountType", accountType)
                .append("attribute", attribute)
                .append("borrowerType", borrowerType)
                .append("businessType", businessType)
                .append("productType", productType)
                .toString();
    }
}
