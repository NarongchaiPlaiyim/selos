package com.clevel.selos.integration.brms.model.request.data2;

import com.clevel.selos.model.db.master.Country;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AttributeTypeLevelApplication implements Serializable{

    private Date appInDate;
    private BigDecimal monthlyIncome;
    private Date expectedSubmitDate;                //4
    private String customerEntity;                  //5
    private boolean existingSMECustomer;            //6
    private boolean sameSetOfBorrower;              //7
    private boolean refinanceInFlag;                //8
    private boolean refinanceOutFlag;               //9
    private String lendingReferType;                //#N/A
    private boolean BAFlag;                         //#N/A
    private boolean topUpBAFlag;                    //#N/A
    private boolean SBCGFlag;                       //#N/A
    private boolean aadFlag;
    private BigDecimal numberOfYearFromLatestFinancialStatement;
    private BigDecimal netWorth;
    private BigDecimal finalDBR;
    private BigDecimal borrowerGroupSale;
    private BigDecimal totalGroupSale;
    private BigDecimal existingGroupExposure;
    private boolean lgCapability;
    private boolean everLgClaim;
    private boolean abandonProject;
    private boolean projectDelay;
    private boolean sufficientSourceOfFund;
    private String primeCustomer;
    private BigDecimal numOfTotalFacility;
    private BigDecimal numOfContingentFacility;
    private BigDecimal numberOfExistiongOD;
    private BigDecimal numberOfRequestedOD;
    private BigDecimal numberOfCoreAsset;
    private BigDecimal numOfNonCoreAsset;
    private BigDecimal totalFixAssetValue;
    private BigDecimal ExistingODLimit;
    private BigDecimal totalCollOfExposure;
    private BigDecimal totalWCRequirement;
    private BigDecimal netWCRequirement125x;
    private BigDecimal netWCRequirement15x;
    private BigDecimal netWCRequirement35;
    private BigDecimal existingWCRCreditLimitWithTMB;
    private BigDecimal existingCoreWCLoanCreditLimitWithTMB;
    private String businessLocation;
    private int yearInBusiness;
    private Country countryOfRegistration;
    private BigDecimal tradeChequeReturn;
    private BigDecimal maxWCCreditLimit;
    private BigDecimal totalRequestedWCCreditLimit;
    private BigDecimal maxCoreWCLoanLimit;
    private BigDecimal totalRequestedCoreWCLoanCreditLimit;
    private BigDecimal totalRequestedODCreditLimit;

    public AttributeTypeLevelApplication() {
    }

    public AttributeTypeLevelApplication(Date appInDate, BigDecimal monthlyIncome, Date expectedSubmitDate, String customerEntity, boolean existingSMECustomer, boolean sameSetOfBorrower, boolean refinanceInFlag, boolean refinanceOutFlag, String lendingReferType, boolean BAFlag, boolean topUpBAFlag, boolean SBCGFlag, boolean aadFlag, BigDecimal numberOfYearFromLatestFinancialStatement, BigDecimal netWorth, BigDecimal finalDBR, BigDecimal borrowerGroupSale, BigDecimal totalGroupSale, BigDecimal existingGroupExposure, boolean lgCapability, boolean everLgClaim, boolean abandonProject, boolean projectDelay, boolean sufficientSourceOfFund, String primeCustomer, BigDecimal numOfTotalFacility, BigDecimal numOfContingentFacility, BigDecimal numberOfExistiongOD, BigDecimal numberOfRequestedOD, BigDecimal numberOfCoreAsset, BigDecimal numOfNonCoreAsset, BigDecimal totalFixAssetValue, BigDecimal existingODLimit, BigDecimal totalCollOfExposure, BigDecimal totalWCRequirement, BigDecimal netWCRequirement125x, BigDecimal netWCRequirement15x, BigDecimal netWCRequirement35, BigDecimal existingWCRCreditLimitWithTMB, BigDecimal existingCoreWCLoanCreditLimitWithTMB, String businessLocation, int yearInBusiness, Country countryOfRegistration, BigDecimal tradeChequeReturn, BigDecimal maxWCCreditLimit, BigDecimal totalRequestedWCCreditLimit, BigDecimal maxCoreWCLoanLimit, BigDecimal totalRequestedCoreWCLoanCreditLimit, BigDecimal totalRequestedODCreditLimit) {
        this.appInDate = appInDate;
        this.monthlyIncome = monthlyIncome;
        this.expectedSubmitDate = expectedSubmitDate;
        this.customerEntity = customerEntity;
        this.existingSMECustomer = existingSMECustomer;
        this.sameSetOfBorrower = sameSetOfBorrower;
        this.refinanceInFlag = refinanceInFlag;
        this.refinanceOutFlag = refinanceOutFlag;
        this.lendingReferType = lendingReferType;
        this.BAFlag = BAFlag;
        this.topUpBAFlag = topUpBAFlag;
        this.SBCGFlag = SBCGFlag;
        this.aadFlag = aadFlag;
        this.numberOfYearFromLatestFinancialStatement = numberOfYearFromLatestFinancialStatement;
        this.netWorth = netWorth;
        this.finalDBR = finalDBR;
        this.borrowerGroupSale = borrowerGroupSale;
        this.totalGroupSale = totalGroupSale;
        this.existingGroupExposure = existingGroupExposure;
        this.lgCapability = lgCapability;
        this.everLgClaim = everLgClaim;
        this.abandonProject = abandonProject;
        this.projectDelay = projectDelay;
        this.sufficientSourceOfFund = sufficientSourceOfFund;
        this.primeCustomer = primeCustomer;
        this.numOfTotalFacility = numOfTotalFacility;
        this.numOfContingentFacility = numOfContingentFacility;
        this.numberOfExistiongOD = numberOfExistiongOD;
        this.numberOfRequestedOD = numberOfRequestedOD;
        this.numberOfCoreAsset = numberOfCoreAsset;
        this.numOfNonCoreAsset = numOfNonCoreAsset;
        this.totalFixAssetValue = totalFixAssetValue;
        ExistingODLimit = existingODLimit;
        this.totalCollOfExposure = totalCollOfExposure;
        this.totalWCRequirement = totalWCRequirement;
        this.netWCRequirement125x = netWCRequirement125x;
        this.netWCRequirement15x = netWCRequirement15x;
        this.netWCRequirement35 = netWCRequirement35;
        this.existingWCRCreditLimitWithTMB = existingWCRCreditLimitWithTMB;
        this.existingCoreWCLoanCreditLimitWithTMB = existingCoreWCLoanCreditLimitWithTMB;
        this.businessLocation = businessLocation;
        this.yearInBusiness = yearInBusiness;
        this.countryOfRegistration = countryOfRegistration;
        this.tradeChequeReturn = tradeChequeReturn;
        this.maxWCCreditLimit = maxWCCreditLimit;
        this.totalRequestedWCCreditLimit = totalRequestedWCCreditLimit;
        this.maxCoreWCLoanLimit = maxCoreWCLoanLimit;
        this.totalRequestedCoreWCLoanCreditLimit = totalRequestedCoreWCLoanCreditLimit;
        this.totalRequestedODCreditLimit = totalRequestedODCreditLimit;
    }

    public Date getAppInDate() {
        return appInDate;
    }

    public void setAppInDate(Date appInDate) {
        this.appInDate = appInDate;
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

    public boolean isTopUpBAFlag() {
        return topUpBAFlag;
    }

    public void setTopUpBAFlag(boolean topUpBAFlag) {
        this.topUpBAFlag = topUpBAFlag;
    }

    public boolean isSBCGFlag() {
        return SBCGFlag;
    }

    public void setSBCGFlag(boolean SBCGFlag) {
        this.SBCGFlag = SBCGFlag;
    }

    public boolean isAadFlag() {
        return aadFlag;
    }

    public void setAadFlag(boolean aadFlag) {
        this.aadFlag = aadFlag;
    }

    public BigDecimal getNumberOfYearFromLatestFinancialStatement() {
        return numberOfYearFromLatestFinancialStatement;
    }

    public void setNumberOfYearFromLatestFinancialStatement(BigDecimal numberOfYearFromLatestFinancialStatement) {
        this.numberOfYearFromLatestFinancialStatement = numberOfYearFromLatestFinancialStatement;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

    public BigDecimal getFinalDBR() {
        return finalDBR;
    }

    public void setFinalDBR(BigDecimal finalDBR) {
        this.finalDBR = finalDBR;
    }

    public BigDecimal getBorrowerGroupSale() {
        return borrowerGroupSale;
    }

    public void setBorrowerGroupSale(BigDecimal borrowerGroupSale) {
        this.borrowerGroupSale = borrowerGroupSale;
    }

    public BigDecimal getTotalGroupSale() {
        return totalGroupSale;
    }

    public void setTotalGroupSale(BigDecimal totalGroupSale) {
        this.totalGroupSale = totalGroupSale;
    }

    public BigDecimal getExistingGroupExposure() {
        return existingGroupExposure;
    }

    public void setExistingGroupExposure(BigDecimal existingGroupExposure) {
        this.existingGroupExposure = existingGroupExposure;
    }

    public boolean isLgCapability() {
        return lgCapability;
    }

    public void setLgCapability(boolean lgCapability) {
        this.lgCapability = lgCapability;
    }

    public boolean isEverLgClaim() {
        return everLgClaim;
    }

    public void setEverLgClaim(boolean everLgClaim) {
        this.everLgClaim = everLgClaim;
    }

    public boolean isAbandonProject() {
        return abandonProject;
    }

    public void setAbandonProject(boolean abandonProject) {
        this.abandonProject = abandonProject;
    }

    public boolean isProjectDelay() {
        return projectDelay;
    }

    public void setProjectDelay(boolean projectDelay) {
        this.projectDelay = projectDelay;
    }

    public boolean isSufficientSourceOfFund() {
        return sufficientSourceOfFund;
    }

    public void setSufficientSourceOfFund(boolean sufficientSourceOfFund) {
        this.sufficientSourceOfFund = sufficientSourceOfFund;
    }

    public String getPrimeCustomer() {
        return primeCustomer;
    }

    public void setPrimeCustomer(String primeCustomer) {
        this.primeCustomer = primeCustomer;
    }

    public BigDecimal getNumOfTotalFacility() {
        return numOfTotalFacility;
    }

    public void setNumOfTotalFacility(BigDecimal numOfTotalFacility) {
        this.numOfTotalFacility = numOfTotalFacility;
    }

    public BigDecimal getNumOfContingentFacility() {
        return numOfContingentFacility;
    }

    public void setNumOfContingentFacility(BigDecimal numOfContingentFacility) {
        this.numOfContingentFacility = numOfContingentFacility;
    }

    public BigDecimal getNumberOfExistiongOD() {
        return numberOfExistiongOD;
    }

    public void setNumberOfExistiongOD(BigDecimal numberOfExistiongOD) {
        this.numberOfExistiongOD = numberOfExistiongOD;
    }

    public BigDecimal getNumberOfRequestedOD() {
        return numberOfRequestedOD;
    }

    public void setNumberOfRequestedOD(BigDecimal numberOfRequestedOD) {
        this.numberOfRequestedOD = numberOfRequestedOD;
    }

    public BigDecimal getNumberOfCoreAsset() {
        return numberOfCoreAsset;
    }

    public void setNumberOfCoreAsset(BigDecimal numberOfCoreAsset) {
        this.numberOfCoreAsset = numberOfCoreAsset;
    }

    public BigDecimal getNumOfNonCoreAsset() {
        return numOfNonCoreAsset;
    }

    public void setNumOfNonCoreAsset(BigDecimal numOfNonCoreAsset) {
        this.numOfNonCoreAsset = numOfNonCoreAsset;
    }

    public BigDecimal getTotalFixAssetValue() {
        return totalFixAssetValue;
    }

    public void setTotalFixAssetValue(BigDecimal totalFixAssetValue) {
        this.totalFixAssetValue = totalFixAssetValue;
    }

    public BigDecimal getExistingODLimit() {
        return ExistingODLimit;
    }

    public void setExistingODLimit(BigDecimal existingODLimit) {
        ExistingODLimit = existingODLimit;
    }

    public BigDecimal getTotalCollOfExposure() {
        return totalCollOfExposure;
    }

    public void setTotalCollOfExposure(BigDecimal totalCollOfExposure) {
        this.totalCollOfExposure = totalCollOfExposure;
    }

    public BigDecimal getTotalWCRequirement() {
        return totalWCRequirement;
    }

    public void setTotalWCRequirement(BigDecimal totalWCRequirement) {
        this.totalWCRequirement = totalWCRequirement;
    }

    public BigDecimal getNetWCRequirement125x() {
        return netWCRequirement125x;
    }

    public void setNetWCRequirement125x(BigDecimal netWCRequirement125x) {
        this.netWCRequirement125x = netWCRequirement125x;
    }

    public BigDecimal getNetWCRequirement15x() {
        return netWCRequirement15x;
    }

    public void setNetWCRequirement15x(BigDecimal netWCRequirement15x) {
        this.netWCRequirement15x = netWCRequirement15x;
    }

    public BigDecimal getNetWCRequirement35() {
        return netWCRequirement35;
    }

    public void setNetWCRequirement35(BigDecimal netWCRequirement35) {
        this.netWCRequirement35 = netWCRequirement35;
    }

    public BigDecimal getExistingWCRCreditLimitWithTMB() {
        return existingWCRCreditLimitWithTMB;
    }

    public void setExistingWCRCreditLimitWithTMB(BigDecimal existingWCRCreditLimitWithTMB) {
        this.existingWCRCreditLimitWithTMB = existingWCRCreditLimitWithTMB;
    }

    public BigDecimal getExistingCoreWCLoanCreditLimitWithTMB() {
        return existingCoreWCLoanCreditLimitWithTMB;
    }

    public void setExistingCoreWCLoanCreditLimitWithTMB(BigDecimal existingCoreWCLoanCreditLimitWithTMB) {
        this.existingCoreWCLoanCreditLimitWithTMB = existingCoreWCLoanCreditLimitWithTMB;
    }

    public String getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(String businessLocation) {
        this.businessLocation = businessLocation;
    }

    public int getYearInBusiness() {
        return yearInBusiness;
    }

    public void setYearInBusiness(int yearInBusiness) {
        this.yearInBusiness = yearInBusiness;
    }

    public Country getCountryOfRegistration() {
        return countryOfRegistration;
    }

    public void setCountryOfRegistration(Country countryOfRegistration) {
        this.countryOfRegistration = countryOfRegistration;
    }

    public BigDecimal getTradeChequeReturn() {
        return tradeChequeReturn;
    }

    public void setTradeChequeReturn(BigDecimal tradeChequeReturn) {
        this.tradeChequeReturn = tradeChequeReturn;
    }

    public BigDecimal getMaxWCCreditLimit() {
        return maxWCCreditLimit;
    }

    public void setMaxWCCreditLimit(BigDecimal maxWCCreditLimit) {
        this.maxWCCreditLimit = maxWCCreditLimit;
    }

    public BigDecimal getTotalRequestedWCCreditLimit() {
        return totalRequestedWCCreditLimit;
    }

    public void setTotalRequestedWCCreditLimit(BigDecimal totalRequestedWCCreditLimit) {
        this.totalRequestedWCCreditLimit = totalRequestedWCCreditLimit;
    }

    public BigDecimal getMaxCoreWCLoanLimit() {
        return maxCoreWCLoanLimit;
    }

    public void setMaxCoreWCLoanLimit(BigDecimal maxCoreWCLoanLimit) {
        this.maxCoreWCLoanLimit = maxCoreWCLoanLimit;
    }

    public BigDecimal getTotalRequestedCoreWCLoanCreditLimit() {
        return totalRequestedCoreWCLoanCreditLimit;
    }

    public void setTotalRequestedCoreWCLoanCreditLimit(BigDecimal totalRequestedCoreWCLoanCreditLimit) {
        this.totalRequestedCoreWCLoanCreditLimit = totalRequestedCoreWCLoanCreditLimit;
    }

    public BigDecimal getTotalRequestedODCreditLimit() {
        return totalRequestedODCreditLimit;
    }

    public void setTotalRequestedODCreditLimit(BigDecimal totalRequestedODCreditLimit) {
        this.totalRequestedODCreditLimit = totalRequestedODCreditLimit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("monthlyIncome", monthlyIncome)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("customerEntity", customerEntity)
                .append("existingSMECustomer", existingSMECustomer)
                .append("sameSetOfBorrower", sameSetOfBorrower)
                .append("refinanceInFlag", refinanceInFlag)
                .append("refinanceOutFlag", refinanceOutFlag)
                .append("lendingReferType", lendingReferType)
                .append("BAFlag", BAFlag)
                .append("topUpBAFlag", topUpBAFlag)
                .append("SBCGFlag", SBCGFlag)
                .append("aadFlag", aadFlag)
                .append("numberOfYearFromLatestFinancialStatement", numberOfYearFromLatestFinancialStatement)
                .append("netWorth", netWorth)
                .append("finalDBR", finalDBR)
                .append("borrowerGroupSale", borrowerGroupSale)
                .append("totalGroupSale", totalGroupSale)
                .append("existingGroupExposure", existingGroupExposure)
                .append("lgCapability", lgCapability)
                .append("everLgClaim", everLgClaim)
                .append("abandonProject", abandonProject)
                .append("projectDelay", projectDelay)
                .append("sufficientSourceOfFund", sufficientSourceOfFund)
                .append("primeCustomer", primeCustomer)
                .append("numOfTotalFacility", numOfTotalFacility)
                .append("numOfContingentFacility", numOfContingentFacility)
                .append("numberOfExistiongOD", numberOfExistiongOD)
                .append("numberOfRequestedOD", numberOfRequestedOD)
                .append("numberOfCoreAsset", numberOfCoreAsset)
                .append("numOfNonCoreAsset", numOfNonCoreAsset)
                .append("totalFixAssetValue", totalFixAssetValue)
                .append("ExistingODLimit", ExistingODLimit)
                .append("totalCollOfExposure", totalCollOfExposure)
                .append("totalWCRequirement", totalWCRequirement)
                .append("netWCRequirement125x", netWCRequirement125x)
                .append("netWCRequirement15x", netWCRequirement15x)
                .append("netWCRequirement35", netWCRequirement35)
                .append("existingWCRCreditLimitWithTMB", existingWCRCreditLimitWithTMB)
                .append("existingCoreWCLoanCreditLimitWithTMB", existingCoreWCLoanCreditLimitWithTMB)
                .append("businessLocation", businessLocation)
                .append("yearInBusiness", yearInBusiness)
                .append("countryOfRegistration", countryOfRegistration)
                .append("tradeChequeReturn", tradeChequeReturn)
                .append("maxWCCreditLimit", maxWCCreditLimit)
                .append("totalRequestedWCCreditLimit", totalRequestedWCCreditLimit)
                .append("maxCoreWCLoanLimit", maxCoreWCLoanLimit)
                .append("totalRequestedCoreWCLoanCreditLimit", totalRequestedCoreWCLoanCreditLimit)
                .append("totalRequestedODCreditLimit", totalRequestedODCreditLimit)
                .toString();
    }
}
