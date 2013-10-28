package com.clevel.selos.integration.brms.model.request.data;

import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.Province;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ApplicationLevel implements Serializable {
    private String applicationNumber;
    private Date processDate;
    private Date appInDate;
    private Date expectedSubmitDate;
    private CustomerEntity customerEntity;
    private boolean existingSMECustomer;
    private boolean sameSetOfBorrower;
    private boolean refinanceInFlag;
    private boolean refinanceOutFlag;
    private BigDecimal borrowerGroupSale;
    private BigDecimal totalGroupSale;
    private BigDecimal totalFacility;
    private BigDecimal contingentFacility;
    private Province bizLocation;
    private int yearInBusiness;
    private Country countryOfRegistration;

    List<BorrowerLevel> customerLevelList;
    List<BankAccountLevel> bankAccountLevelList;
    List<BusinessLevel> businessLevelList;

    private BigDecimal numOfTotalFacility;
    private BigDecimal numOfContingentFacility;
    private String businessLocation;

    private boolean sbcgFlag;
    private boolean aadFlag;
    private BigDecimal numberOfYearFromLatestFinancialStatement;
    private BigDecimal netWorth;
    private BigDecimal finalDBR;
    private BigDecimal monthlyIncome;
    private BigDecimal existingGroupExposure;
    private BigDecimal finalGroupExposure;
    private String lgCapability;
    private String everLgClaim;
    private String abandonProject;
    private String projectDelay;
    private String sufficientSourceOfFund;
    private String primeCustomer;
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
    private BigDecimal tradeChequeReturn;
    private String collateralPotertialForPricing;
    private BigDecimal maxCreditLimitByCollateral;
    private BigDecimal totalProposedCreditLimit;
    private String guaranteeType;
    private BigDecimal maxWCCreditLimit;
    private BigDecimal totalRequestedWCCreditLimit;
    private BigDecimal maxCoreWCLoanLimit;
    private BigDecimal totalRequestedCoreWCLoanCreditLimit;
    private BigDecimal totalRequestedODCreditLimit;
    private String lendingReferType;
    private String BAFlag;
    private String topUpBAFlag;
    private String SBCGFlag;

    public ApplicationLevel() {
    }

    public ApplicationLevel(String applicationNumber, Date processDate, Date appInDate, Date expectedSubmitDate, CustomerEntity customerEntity, boolean existingSMECustomer, boolean sameSetOfBorrower, boolean refinanceInFlag, boolean refinanceOutFlag, BigDecimal borrowerGroupSale, BigDecimal totalGroupSale, BigDecimal totalFacility, BigDecimal contingentFacility, Province bizLocation, int yearInBusiness, Country countryOfRegistration, ProductGroup productGroup) {
        this.applicationNumber = applicationNumber;
        this.processDate = processDate;
        this.appInDate = appInDate;
        this.expectedSubmitDate = expectedSubmitDate;
        this.customerEntity = customerEntity;
        this.existingSMECustomer = existingSMECustomer;
        this.sameSetOfBorrower = sameSetOfBorrower;
        this.refinanceInFlag = refinanceInFlag;
        this.refinanceOutFlag = refinanceOutFlag;
        this.borrowerGroupSale = borrowerGroupSale;
        this.totalGroupSale = totalGroupSale;
        this.totalFacility = totalFacility;
        this.contingentFacility = contingentFacility;
        this.bizLocation = bizLocation;
        this.yearInBusiness = yearInBusiness;
        this.countryOfRegistration = countryOfRegistration;
    }

    public String getLendingReferType() {
        return lendingReferType;
    }

    public void setLendingReferType(String lendingReferType) {
        this.lendingReferType = lendingReferType;
    }

    public String getBAFlag() {
        return BAFlag;
    }

    public void setBAFlag(String BAFlag) {
        this.BAFlag = BAFlag;
    }

    public String getTopUpBAFlag() {
        return topUpBAFlag;
    }

    public void setTopUpBAFlag(String topUpBAFlag) {
        this.topUpBAFlag = topUpBAFlag;
    }

    public String getSBCGFlag() {
        return SBCGFlag;
    }

    public void setSBCGFlag(String SBCGFlag) {
        this.SBCGFlag = SBCGFlag;
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

    public BigDecimal getMaxWCCreditLimit() {
        return maxWCCreditLimit;
    }

    public void setMaxWCCreditLimit(BigDecimal maxWCCreditLimit) {
        this.maxWCCreditLimit = maxWCCreditLimit;
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

    public Date getAppInDate() {
        return appInDate;
    }

    public void setAppInDate(Date appInDate) {
        this.appInDate = appInDate;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
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

    public BigDecimal getTotalFacility() {
        return totalFacility;
    }

    public void setTotalFacility(BigDecimal totalFacility) {
        this.totalFacility = totalFacility;
    }

    public BigDecimal getContingentFacility() {
        return contingentFacility;
    }

    public void setContingentFacility(BigDecimal contingentFacility) {
        this.contingentFacility = contingentFacility;
    }

    public Province getBizLocation() {
        return bizLocation;
    }

    public void setBizLocation(Province bizLocation) {
        this.bizLocation = bizLocation;
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

    public String getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(String businessLocation) {
        this.businessLocation = businessLocation;
    }

    public List<BorrowerLevel> getCustomerLevelList() {
        return customerLevelList;
    }

    public void setCustomerLevelList(List<BorrowerLevel> customerLevelList) {
        this.customerLevelList = customerLevelList;
    }

    public List<BankAccountLevel> getBankAccountLevelList() {
        return bankAccountLevelList;
    }

    public void setBankAccountLevelList(List<BankAccountLevel> bankAccountLevelList) {
        this.bankAccountLevelList = bankAccountLevelList;
    }

    public List<BusinessLevel> getBusinessLevelList() {
        return businessLevelList;
    }

    public void setBusinessLevelList(List<BusinessLevel> businessLevelList) {
        this.businessLevelList = businessLevelList;
    }

    public boolean isSbcgFlag() {
        return sbcgFlag;
    }

    public void setSbcgFlag(boolean sbcgFlag) {
        this.sbcgFlag = sbcgFlag;
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

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getExistingGroupExposure() {
        return existingGroupExposure;
    }

    public void setExistingGroupExposure(BigDecimal existingGroupExposure) {
        this.existingGroupExposure = existingGroupExposure;
    }

    public BigDecimal getFinalGroupExposure() {
        return finalGroupExposure;
    }

    public void setFinalGroupExposure(BigDecimal finalGroupExposure) {
        this.finalGroupExposure = finalGroupExposure;
    }

    public String getLgCapability() {
        return lgCapability;
    }

    public void setLgCapability(String lgCapability) {
        this.lgCapability = lgCapability;
    }

    public String getEverLgClaim() {
        return everLgClaim;
    }

    public void setEverLgClaim(String everLgClaim) {
        this.everLgClaim = everLgClaim;
    }

    public String getAbandonProject() {
        return abandonProject;
    }

    public void setAbandonProject(String abandonProject) {
        this.abandonProject = abandonProject;
    }

    public String getProjectDelay() {
        return projectDelay;
    }

    public void setProjectDelay(String projectDelay) {
        this.projectDelay = projectDelay;
    }

    public String getSufficientSourceOfFund() {
        return sufficientSourceOfFund;
    }

    public void setSufficientSourceOfFund(String sufficientSourceOfFund) {
        this.sufficientSourceOfFund = sufficientSourceOfFund;
    }

    public String getPrimeCustomer() {
        return primeCustomer;
    }

    public void setPrimeCustomer(String primeCustomer) {
        this.primeCustomer = primeCustomer;
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

    public BigDecimal getTradeChequeReturn() {
        return tradeChequeReturn;
    }

    public void setTradeChequeReturn(BigDecimal tradeChequeReturn) {
        this.tradeChequeReturn = tradeChequeReturn;
    }

    public String getCollateralPotertialForPricing() {
        return collateralPotertialForPricing;
    }

    public void setCollateralPotertialForPricing(String collateralPotertialForPricing) {
        this.collateralPotertialForPricing = collateralPotertialForPricing;
    }

    public BigDecimal getMaxCreditLimitByCollateral() {
        return maxCreditLimitByCollateral;
    }

    public void setMaxCreditLimitByCollateral(BigDecimal maxCreditLimitByCollateral) {
        this.maxCreditLimitByCollateral = maxCreditLimitByCollateral;
    }

    public BigDecimal getTotalProposedCreditLimit() {
        return totalProposedCreditLimit;
    }

    public void setTotalProposedCreditLimit(BigDecimal totalProposedCreditLimit) {
        this.totalProposedCreditLimit = totalProposedCreditLimit;
    }

    public String getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("applicationNumber", applicationNumber)
                .append("processDate", processDate)
                .append("appInDate", appInDate)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("customerEntity", customerEntity)
                .append("existingSMECustomer", existingSMECustomer)
                .append("sameSetOfBorrower", sameSetOfBorrower)
                .append("refinanceInFlag", refinanceInFlag)
                .append("refinanceOutFlag", refinanceOutFlag)
                .append("borrowerGroupSale", borrowerGroupSale)
                .append("totalGroupSale", totalGroupSale)
                .append("totalFacility", totalFacility)
                .append("contingentFacility", contingentFacility)
                .append("bizLocation", bizLocation)
                .append("yearInBusiness", yearInBusiness)
                .append("countryOfRegistration", countryOfRegistration)
                .append("customerLevelList", customerLevelList)
                .append("bankAccountLevelList", bankAccountLevelList)
                .append("businessLevelList", businessLevelList)
                .append("numOfTotalFacility", numOfTotalFacility)
                .append("numOfContingentFacility", numOfContingentFacility)
                .append("businessLocation", businessLocation)
                .append("sbcgFlag", sbcgFlag)
                .append("aadFlag", aadFlag)
                .append("numberOfYearFromLatestFinancialStatement", numberOfYearFromLatestFinancialStatement)
                .append("netWorth", netWorth)
                .append("finalDBR", finalDBR)
                .append("monthlyIncome", monthlyIncome)
                .append("existingGroupExposure", existingGroupExposure)
                .append("finalGroupExposure", finalGroupExposure)
                .append("lgCapability", lgCapability)
                .append("everLgClaim", everLgClaim)
                .append("abandonProject", abandonProject)
                .append("projectDelay", projectDelay)
                .append("sufficientSourceOfFund", sufficientSourceOfFund)
                .append("primeCustomer", primeCustomer)
                .append("numberOfExistiongOD", numberOfExistiongOD)
                .append("numberOfRequestedOD", numberOfRequestedOD)
                .append("numberOfCoreAsset", numberOfCoreAsset)
                .append("numOfNonCoreAsset", numOfNonCoreAsset)
                .append("totalFixAssetValue", totalFixAssetValue)
                .append("ExistingODLimit", ExistingODLimit)
                .toString();
    }
}
