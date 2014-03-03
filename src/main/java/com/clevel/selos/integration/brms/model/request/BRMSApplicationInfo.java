package com.clevel.selos.integration.brms.model.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BRMSApplicationInfo implements Serializable{

    private String statusCode;
    private String stepCode;
    private String applicationNo;
    private Date processDate;
    private Date bdmSubmitDate;
    private Date expectedSubmitDate;
    private String borrowerType;
    private boolean existingSMECustomer;
    private boolean requestLoanWithSameName;
    private boolean refinanceIN;
    private boolean refinanceOUT;
    private String loanRequestPattern;
    private boolean applyBA;
    private boolean topupBA;
    private boolean requestTCG;
    private boolean passAppraisalProcess;
    private boolean brmsRequestStep;
    private BigDecimal numberOfYearFinancialStmt;
    private BigDecimal shareHolderRatio;
    private BigDecimal finalDBR;
    private BigDecimal netMonthlyIncome;
    private BigDecimal borrowerGroupIncome;
    private BigDecimal totalGroupIncome;
    private BigDecimal existingGroupExposure;
    private BigDecimal finalGroupExposure;
    private boolean ableToGettingGuarantorJob;
    private boolean noClaimLGHistory;
    private boolean noRevokedLicense;
    private boolean noLateWorkDelivery;
    private boolean adequateOfCapital;
    private String creditCusType;
    private BigDecimal totalNumberProposeCredit;
    private BigDecimal totalNumberContingenPropose;
    private BigDecimal totalNumberOfExistingOD;
    private BigDecimal totalNumberOfRequestedOD;
    private BigDecimal totalNumberOfCoreAsset;
    private BigDecimal totalNumberOfNonCoreAsset;
    private BigDecimal netFixAsset;
    private BigDecimal totalExistingODLimit;
    private BigDecimal collateralPercent;
    private BigDecimal wcNeed;
    private BigDecimal case1WCminLimit;
    private BigDecimal case2WCminLimit;
    private BigDecimal case3WCminLimit;
    private BigDecimal totalWCTMB;
    private BigDecimal totalLoanWCTMB;
    private String bizLocation;
    private BigDecimal yearInBusinessMonth;
    private String countryOfRegistration;
    private BigDecimal tradeChequeReturnPercent;
    private String productGroup;
    private BigDecimal maximumSMELimit;
    private BigDecimal totalApprovedCredit;
    private String loanRequestType;
    private String referredDocType;
    private BigDecimal totalTCGGuaranteeAmount;
    private BigDecimal numberOfIndvGuarantor;
    private BigDecimal numberOfJurisGuarantor;
    private BigDecimal totalMortgageValue;
    private BigDecimal totalRedeemTransaction;

    private List<BRMSCustomerInfo> customerInfoList;
    private List<BRMSAccountStmtInfo> accountStmtInfoList;
    private List<BRMSBizInfo> bizInfoList;
    private List<BRMSAccountRequested> accountRequestedList;
    private List<BRMSCollateralInfo> collateralInfoList;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStepCode() {
        return stepCode;
    }

    public void setStepCode(String stepCode) {
        this.stepCode = stepCode;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Date getBdmSubmitDate() {
        return bdmSubmitDate;
    }

    public void setBdmSubmitDate(Date bdmSubmitDate) {
        this.bdmSubmitDate = bdmSubmitDate;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public String getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    public boolean isExistingSMECustomer() {
        return existingSMECustomer;
    }

    public void setExistingSMECustomer(boolean existingSMECustomer) {
        this.existingSMECustomer = existingSMECustomer;
    }

    public boolean isRequestLoanWithSameName() {
        return requestLoanWithSameName;
    }

    public void setRequestLoanWithSameName(boolean requestLoanWithSameName) {
        this.requestLoanWithSameName = requestLoanWithSameName;
    }

    public boolean isRefinanceIN() {
        return refinanceIN;
    }

    public void setRefinanceIN(boolean refinanceIN) {
        this.refinanceIN = refinanceIN;
    }

    public boolean isRefinanceOUT() {
        return refinanceOUT;
    }

    public void setRefinanceOUT(boolean refinanceOUT) {
        this.refinanceOUT = refinanceOUT;
    }

    public String getLoanRequestPattern() {
        return loanRequestPattern;
    }

    public void setLoanRequestPattern(String loanRequestPattern) {
        this.loanRequestPattern = loanRequestPattern;
    }

    public boolean isApplyBA() {
        return applyBA;
    }

    public void setApplyBA(boolean applyBA) {
        this.applyBA = applyBA;
    }

    public boolean isTopupBA() {
        return topupBA;
    }

    public void setTopupBA(boolean topupBA) {
        this.topupBA = topupBA;
    }

    public boolean isRequestTCG() {
        return requestTCG;
    }

    public void setRequestTCG(boolean requestTCG) {
        this.requestTCG = requestTCG;
    }

    public boolean isPassAppraisalProcess() {
        return passAppraisalProcess;
    }

    public void setPassAppraisalProcess(boolean passAppraisalProcess) {
        this.passAppraisalProcess = passAppraisalProcess;
    }

    public boolean isBrmsRequestStep() {
        return brmsRequestStep;
    }

    public void setBrmsRequestStep(boolean brmsRequestStep) {
        this.brmsRequestStep = brmsRequestStep;
    }

    public BigDecimal getNumberOfYearFinancialStmt() {
        return numberOfYearFinancialStmt;
    }

    public void setNumberOfYearFinancialStmt(BigDecimal numberOfYearFinancialStmt) {
        this.numberOfYearFinancialStmt = numberOfYearFinancialStmt;
    }

    public BigDecimal getShareHolderRatio() {
        return shareHolderRatio;
    }

    public void setShareHolderRatio(BigDecimal shareHolderRatio) {
        this.shareHolderRatio = shareHolderRatio;
    }

    public BigDecimal getFinalDBR() {
        return finalDBR;
    }

    public void setFinalDBR(BigDecimal finalDBR) {
        this.finalDBR = finalDBR;
    }

    public BigDecimal getNetMonthlyIncome() {
        return netMonthlyIncome;
    }

    public void setNetMonthlyIncome(BigDecimal netMonthlyIncome) {
        this.netMonthlyIncome = netMonthlyIncome;
    }

    public BigDecimal getBorrowerGroupIncome() {
        return borrowerGroupIncome;
    }

    public void setBorrowerGroupIncome(BigDecimal borrowerGroupIncome) {
        this.borrowerGroupIncome = borrowerGroupIncome;
    }

    public BigDecimal getTotalGroupIncome() {
        return totalGroupIncome;
    }

    public void setTotalGroupIncome(BigDecimal totalGroupIncome) {
        this.totalGroupIncome = totalGroupIncome;
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

    public boolean isAbleToGettingGuarantorJob() {
        return ableToGettingGuarantorJob;
    }

    public void setAbleToGettingGuarantorJob(boolean ableToGettingGuarantorJob) {
        this.ableToGettingGuarantorJob = ableToGettingGuarantorJob;
    }

    public boolean isNoClaimLGHistory() {
        return noClaimLGHistory;
    }

    public void setNoClaimLGHistory(boolean noClaimLGHistory) {
        this.noClaimLGHistory = noClaimLGHistory;
    }

    public boolean isNoRevokedLicense() {
        return noRevokedLicense;
    }

    public void setNoRevokedLicense(boolean noRevokedLicense) {
        this.noRevokedLicense = noRevokedLicense;
    }

    public boolean isNoLateWorkDelivery() {
        return noLateWorkDelivery;
    }

    public void setNoLateWorkDelivery(boolean noLateWorkDelivery) {
        this.noLateWorkDelivery = noLateWorkDelivery;
    }

    public boolean isAdequateOfCapital() {
        return adequateOfCapital;
    }

    public void setAdequateOfCapital(boolean adequateOfCapital) {
        this.adequateOfCapital = adequateOfCapital;
    }

    public String getCreditCusType() {
        return creditCusType;
    }

    public void setCreditCusType(String creditCusType) {
        this.creditCusType = creditCusType;
    }

    public BigDecimal getTotalNumberProposeCredit() {
        return totalNumberProposeCredit;
    }

    public void setTotalNumberProposeCredit(BigDecimal totalNumberProposeCredit) {
        this.totalNumberProposeCredit = totalNumberProposeCredit;
    }

    public BigDecimal getTotalNumberContingenPropose() {
        return totalNumberContingenPropose;
    }

    public void setTotalNumberContingenPropose(BigDecimal totalNumberContingenPropose) {
        this.totalNumberContingenPropose = totalNumberContingenPropose;
    }

    public BigDecimal getTotalNumberOfExistingOD() {
        return totalNumberOfExistingOD;
    }

    public void setTotalNumberOfExistingOD(BigDecimal totalNumberOfExistingOD) {
        this.totalNumberOfExistingOD = totalNumberOfExistingOD;
    }

    public BigDecimal getTotalNumberOfRequestedOD() {
        return totalNumberOfRequestedOD;
    }

    public void setTotalNumberOfRequestedOD(BigDecimal totalNumberOfRequestedOD) {
        this.totalNumberOfRequestedOD = totalNumberOfRequestedOD;
    }

    public BigDecimal getTotalNumberOfCoreAsset() {
        return totalNumberOfCoreAsset;
    }

    public void setTotalNumberOfCoreAsset(BigDecimal totalNumberOfCoreAsset) {
        this.totalNumberOfCoreAsset = totalNumberOfCoreAsset;
    }

    public BigDecimal getTotalNumberOfNonCoreAsset() {
        return totalNumberOfNonCoreAsset;
    }

    public void setTotalNumberOfNonCoreAsset(BigDecimal totalNumberOfNonCoreAsset) {
        this.totalNumberOfNonCoreAsset = totalNumberOfNonCoreAsset;
    }

    public BigDecimal getNetFixAsset() {
        return netFixAsset;
    }

    public void setNetFixAsset(BigDecimal netFixAsset) {
        this.netFixAsset = netFixAsset;
    }

    public BigDecimal getTotalExistingODLimit() {
        return totalExistingODLimit;
    }

    public void setTotalExistingODLimit(BigDecimal totalExistingODLimit) {
        this.totalExistingODLimit = totalExistingODLimit;
    }

    public BigDecimal getCollateralPercent() {
        return collateralPercent;
    }

    public void setCollateralPercent(BigDecimal collateralPercent) {
        this.collateralPercent = collateralPercent;
    }

    public BigDecimal getWcNeed() {
        return wcNeed;
    }

    public void setWcNeed(BigDecimal wcNeed) {
        this.wcNeed = wcNeed;
    }

    public BigDecimal getCase1WCminLimit() {
        return case1WCminLimit;
    }

    public void setCase1WCminLimit(BigDecimal case1WCminLimit) {
        this.case1WCminLimit = case1WCminLimit;
    }

    public BigDecimal getCase2WCminLimit() {
        return case2WCminLimit;
    }

    public void setCase2WCminLimit(BigDecimal case2WCminLimit) {
        this.case2WCminLimit = case2WCminLimit;
    }

    public BigDecimal getCase3WCminLimit() {
        return case3WCminLimit;
    }

    public void setCase3WCminLimit(BigDecimal case3WCminLimit) {
        this.case3WCminLimit = case3WCminLimit;
    }

    public BigDecimal getTotalWCTMB() {
        return totalWCTMB;
    }

    public void setTotalWCTMB(BigDecimal totalWCTMB) {
        this.totalWCTMB = totalWCTMB;
    }

    public BigDecimal getTotalLoanWCTMB() {
        return totalLoanWCTMB;
    }

    public void setTotalLoanWCTMB(BigDecimal totalLoanWCTMB) {
        this.totalLoanWCTMB = totalLoanWCTMB;
    }

    public String getBizLocation() {
        return bizLocation;
    }

    public void setBizLocation(String bizLocation) {
        this.bizLocation = bizLocation;
    }

    public BigDecimal getYearInBusinessMonth() {
        return yearInBusinessMonth;
    }

    public void setYearInBusinessMonth(BigDecimal yearInBusinessMonth) {
        this.yearInBusinessMonth = yearInBusinessMonth;
    }

    public String getCountryOfRegistration() {
        return countryOfRegistration;
    }

    public void setCountryOfRegistration(String countryOfRegistration) {
        this.countryOfRegistration = countryOfRegistration;
    }

    public BigDecimal getTradeChequeReturnPercent() {
        return tradeChequeReturnPercent;
    }

    public void setTradeChequeReturnPercent(BigDecimal tradeChequeReturnPercent) {
        this.tradeChequeReturnPercent = tradeChequeReturnPercent;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public BigDecimal getMaximumSMELimit() {
        return maximumSMELimit;
    }

    public void setMaximumSMELimit(BigDecimal maximumSMELimit) {
        this.maximumSMELimit = maximumSMELimit;
    }

    public BigDecimal getTotalApprovedCredit() {
        return totalApprovedCredit;
    }

    public void setTotalApprovedCredit(BigDecimal totalApprovedCredit) {
        this.totalApprovedCredit = totalApprovedCredit;
    }

    public String getLoanRequestType() {
        return loanRequestType;
    }

    public void setLoanRequestType(String loanRequestType) {
        this.loanRequestType = loanRequestType;
    }

    public String getReferredDocType() {
        return referredDocType;
    }

    public void setReferredDocType(String referredDocType) {
        this.referredDocType = referredDocType;
    }

    public BigDecimal getTotalTCGGuaranteeAmount() {
        return totalTCGGuaranteeAmount;
    }

    public void setTotalTCGGuaranteeAmount(BigDecimal totalTCGGuaranteeAmount) {
        this.totalTCGGuaranteeAmount = totalTCGGuaranteeAmount;
    }

    public BigDecimal getNumberOfIndvGuarantor() {
        return numberOfIndvGuarantor;
    }

    public void setNumberOfIndvGuarantor(BigDecimal numberOfIndvGuarantor) {
        this.numberOfIndvGuarantor = numberOfIndvGuarantor;
    }

    public BigDecimal getNumberOfJurisGuarantor() {
        return numberOfJurisGuarantor;
    }

    public void setNumberOfJurisGuarantor(BigDecimal numberOfJurisGuarantor) {
        this.numberOfJurisGuarantor = numberOfJurisGuarantor;
    }

    public BigDecimal getTotalMortgageValue() {
        return totalMortgageValue;
    }

    public void setTotalMortgageValue(BigDecimal totalMortgageValue) {
        this.totalMortgageValue = totalMortgageValue;
    }

    public BigDecimal getTotalRedeemTransaction() {
        return totalRedeemTransaction;
    }

    public void setTotalRedeemTransaction(BigDecimal totalRedeemTransaction) {
        this.totalRedeemTransaction = totalRedeemTransaction;
    }

    public List<BRMSCustomerInfo> getCustomerInfoList() {
        return customerInfoList;
    }

    public void setCustomerInfoList(List<BRMSCustomerInfo> customerInfoList) {
        this.customerInfoList = customerInfoList;
    }

    public List<BRMSAccountStmtInfo> getAccountStmtInfoList() {
        return accountStmtInfoList;
    }

    public void setAccountStmtInfoList(List<BRMSAccountStmtInfo> accountStmtInfoList) {
        this.accountStmtInfoList = accountStmtInfoList;
    }

    public List<BRMSBizInfo> getBizInfoList() {
        return bizInfoList;
    }

    public void setBizInfoList(List<BRMSBizInfo> bizInfoList) {
        this.bizInfoList = bizInfoList;
    }

    public List<BRMSAccountRequested> getAccountRequestedList() {
        return accountRequestedList;
    }

    public void setAccountRequestedList(List<BRMSAccountRequested> accountRequestedList) {
        this.accountRequestedList = accountRequestedList;
    }

    public List<BRMSCollateralInfo> getCollateralInfoList() {
        return collateralInfoList;
    }

    public void setCollateralInfoList(List<BRMSCollateralInfo> collateralInfoList) {
        this.collateralInfoList = collateralInfoList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("statusCode", statusCode)
                .append("stepCode", stepCode)
                .append("applicationNo", applicationNo)
                .append("processDate", processDate)
                .append("bdmSubmitDate", bdmSubmitDate)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("borrowerType", borrowerType)
                .append("existingSMECustomer", existingSMECustomer)
                .append("requestLoanWithSameName", requestLoanWithSameName)
                .append("refinanceIN", refinanceIN)
                .append("refinanceOUT", refinanceOUT)
                .append("loanRequestPattern", loanRequestPattern)
                .append("applyBA", applyBA)
                .append("topupBA", topupBA)
                .append("requestTCG", requestTCG)
                .append("passAppraisalProcess", passAppraisalProcess)
                .append("brmsRequestStep", brmsRequestStep)
                .append("numberOfYearFinancialStmt", numberOfYearFinancialStmt)
                .append("shareHolderRatio", shareHolderRatio)
                .append("finalDBR", finalDBR)
                .append("netMonthlyIncome", netMonthlyIncome)
                .append("borrowerGroupIncome", borrowerGroupIncome)
                .append("totalGroupIncome", totalGroupIncome)
                .append("existingGroupExposure", existingGroupExposure)
                .append("finalGroupExposure", finalGroupExposure)
                .append("ableToGettingGuarantorJob", ableToGettingGuarantorJob)
                .append("noClaimLGHistory", noClaimLGHistory)
                .append("noRevokedLicense", noRevokedLicense)
                .append("noLateWorkDelivery", noLateWorkDelivery)
                .append("adequateOfCapital", adequateOfCapital)
                .append("creditCusType", creditCusType)
                .append("totalNumberProposeCredit", totalNumberProposeCredit)
                .append("totalNumberContingenPropose", totalNumberContingenPropose)
                .append("totalNumberOfExistingOD", totalNumberOfExistingOD)
                .append("totalNumberOfRequestedOD", totalNumberOfRequestedOD)
                .append("totalNumberOfCoreAsset", totalNumberOfCoreAsset)
                .append("totalNumberOfNonCoreAsset", totalNumberOfNonCoreAsset)
                .append("netFixAsset", netFixAsset)
                .append("totalExistingODLimit", totalExistingODLimit)
                .append("collateralPercent", collateralPercent)
                .append("wcNeed", wcNeed)
                .append("case1WCminLimit", case1WCminLimit)
                .append("case2WCminLimit", case2WCminLimit)
                .append("case3WCminLimit", case3WCminLimit)
                .append("totalWCTMB", totalWCTMB)
                .append("totalLoanWCTMB", totalLoanWCTMB)
                .append("bizLocation", bizLocation)
                .append("yearInBusinessMonth", yearInBusinessMonth)
                .append("countryOfRegistration", countryOfRegistration)
                .append("tradeChequeReturnPercent", tradeChequeReturnPercent)
                .append("productGroup", productGroup)
                .append("maximumSMELimit", maximumSMELimit)
                .append("totalApprovedCredit", totalApprovedCredit)
                .append("loanRequestType", loanRequestType)
                .append("referredDocType", referredDocType)
                .append("totalTCGGuaranteeAmount", totalTCGGuaranteeAmount)
                .append("numberOfIndvGuarantor", numberOfIndvGuarantor)
                .append("numberOfJurisGuarantor", numberOfJurisGuarantor)
                .append("totalMortgageValue", totalMortgageValue)
                .append("totalRedeemTransaction", totalRedeemTransaction)
                .append("customerInfoList", customerInfoList)
                .append("accountStmtInfoList", accountStmtInfoList)
                .append("bizInfoList", bizInfoList)
                .append("accountRequestedList", accountRequestedList)
                .append("collateralInfoList", collateralInfoList)
                .toString();
    }
}
