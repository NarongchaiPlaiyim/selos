package com.clevel.selos.integration.dwh.obligation.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Obligation implements Serializable {
    private long id;
    private String tmbCusId;
    private String tmbBotCusId;
    private String serviceSegment;
    private String adjustClass;
    private Date lastContractDate;
    private String misProductGroup;
    private String accountServiceSegment;
    private String productCode;
    private String projectCode;
    private String dataSource;
    private String accountName;
    private Date lastReviewDate;
    private Date nextReviewDate;
    private Date extendedReviewDate;
    private String scfScoreFinalRate;
    private String scfScoreMsFinal;
    private String scfScoreModelTypeIbnr;
    private BigDecimal claimAmount;
    private BigDecimal comAmount;
    private BigDecimal tmbPaidExpenseAmount;
    private BigDecimal tmbIntUnEarned;
    private BigDecimal tmbIntAccrued;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private Date maturityDate;
    private String accountNumber;
    private String accountSuffix;
    private String accountRef;
    private String accountStatus;
    private String cardBlockCode;
    private String cusRelAccount;
    private String tdrFlag;
    private BigDecimal numMonthIntPastDue;
    private BigDecimal numMonthIntPastDueTDRAcc;
    private BigDecimal tmbInstallmentAmt;
    private String covenantFlag;
    private String reviewFlag;
    private BigDecimal tmbUnusedBalance;
    private String tmbExtProductTypeCD;
    private String tmbTypeCRD;
    private String tmbRMRef;
    private BigDecimal tenors;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTmbCusId() {
        return tmbCusId;
    }

    public void setTmbCusId(String tmbCusId) {
        this.tmbCusId = tmbCusId;
    }

    public String getTmbBotCusId() {
        return tmbBotCusId;
    }

    public void setTmbBotCusId(String tmbBotCusId) {
        this.tmbBotCusId = tmbBotCusId;
    }

    public String getServiceSegment() {
        return serviceSegment;
    }

    public void setServiceSegment(String serviceSegment) {
        this.serviceSegment = serviceSegment;
    }

    public String getAdjustClass() {
        return adjustClass;
    }

    public void setAdjustClass(String adjustClass) {
        this.adjustClass = adjustClass;
    }

    public Date getLastContractDate() {
        return lastContractDate;
    }

    public void setLastContractDate(Date lastContractDate) {
        this.lastContractDate = lastContractDate;
    }

    public String getMisProductGroup() {
        return misProductGroup;
    }

    public void setMisProductGroup(String misProductGroup) {
        this.misProductGroup = misProductGroup;
    }

    public String getAccountServiceSegment() {
        return accountServiceSegment;
    }

    public void setAccountServiceSegment(String accountServiceSegment) {
        this.accountServiceSegment = accountServiceSegment;
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

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Date getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(Date nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public Date getExtendedReviewDate() {
        return extendedReviewDate;
    }

    public void setExtendedReviewDate(Date extendedReviewDate) {
        this.extendedReviewDate = extendedReviewDate;
    }

    public String getScfScoreFinalRate() {
        return scfScoreFinalRate;
    }

    public void setScfScoreFinalRate(String scfScoreFinalRate) {
        this.scfScoreFinalRate = scfScoreFinalRate;
    }

    public String getScfScoreMsFinal() {
        return scfScoreMsFinal;
    }

    public void setScfScoreMsFinal(String scfScoreMsFinal) {
        this.scfScoreMsFinal = scfScoreMsFinal;
    }

    public String getScfScoreModelTypeIbnr() {
        return scfScoreModelTypeIbnr;
    }

    public void setScfScoreModelTypeIbnr(String scfScoreModelTypeIbnr) {
        this.scfScoreModelTypeIbnr = scfScoreModelTypeIbnr;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public BigDecimal getComAmount() {
        return comAmount;
    }

    public void setComAmount(BigDecimal comAmount) {
        this.comAmount = comAmount;
    }

    public BigDecimal getTmbPaidExpenseAmount() {
        return tmbPaidExpenseAmount;
    }

    public void setTmbPaidExpenseAmount(BigDecimal tmbPaidExpenseAmount) {
        this.tmbPaidExpenseAmount = tmbPaidExpenseAmount;
    }

    public BigDecimal getTmbIntUnEarned() {
        return tmbIntUnEarned;
    }

    public void setTmbIntUnEarned(BigDecimal tmbIntUnEarned) {
        this.tmbIntUnEarned = tmbIntUnEarned;
    }

    public BigDecimal getTmbIntAccrued() {
        return tmbIntAccrued;
    }

    public void setTmbIntAccrued(BigDecimal tmbIntAccrued) {
        this.tmbIntAccrued = tmbIntAccrued;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
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

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getCardBlockCode() {
        return cardBlockCode;
    }

    public void setCardBlockCode(String cardBlockCode) {
        this.cardBlockCode = cardBlockCode;
    }

    public String getCusRelAccount() {
        return cusRelAccount;
    }

    public void setCusRelAccount(String cusRelAccount) {
        this.cusRelAccount = cusRelAccount;
    }

    public String getTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(String tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public BigDecimal getNumMonthIntPastDue() {
        return numMonthIntPastDue;
    }

    public void setNumMonthIntPastDue(BigDecimal numMonthIntPastDue) {
        this.numMonthIntPastDue = numMonthIntPastDue;
    }

    public BigDecimal getNumMonthIntPastDueTDRAcc() {
        return numMonthIntPastDueTDRAcc;
    }

    public void setNumMonthIntPastDueTDRAcc(BigDecimal numMonthIntPastDueTDRAcc) {
        this.numMonthIntPastDueTDRAcc = numMonthIntPastDueTDRAcc;
    }

    public BigDecimal getTmbInstallmentAmt() {
        return tmbInstallmentAmt;
    }

    public void setTmbInstallmentAmt(BigDecimal tmbInstallmentAmt) {
        this.tmbInstallmentAmt = tmbInstallmentAmt;
    }

    public String getCovenantFlag() {
        return covenantFlag;
    }

    public void setCovenantFlag(String covenantFlag) {
        this.covenantFlag = covenantFlag;
    }

    public String getReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(String reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public BigDecimal getTmbUnusedBalance() {
        return tmbUnusedBalance;
    }

    public void setTmbUnusedBalance(BigDecimal tmbUnusedBalance) {
        this.tmbUnusedBalance = tmbUnusedBalance;
    }

    public String getTmbExtProductTypeCD() {
        return tmbExtProductTypeCD;
    }

    public void setTmbExtProductTypeCD(String tmbExtProductTypeCD) {
        this.tmbExtProductTypeCD = tmbExtProductTypeCD;
    }

    public String getTmbTypeCRD() {
        return tmbTypeCRD;
    }

    public void setTmbTypeCRD(String tmbTypeCRD) {
        this.tmbTypeCRD = tmbTypeCRD;
    }

    public String getTmbRMRef() {
        return tmbRMRef;
    }

    public void setTmbRMRef(String tmbRMRef) {
        this.tmbRMRef = tmbRMRef;
    }

    public BigDecimal getTenors() {
        return tenors;
    }

    public void setTenors(BigDecimal tenors) {
        this.tenors = tenors;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("tmbCusId", tmbCusId)
                .append("tmbBotCusId", tmbBotCusId)
                .append("serviceSegment", serviceSegment)
                .append("adjustClass", adjustClass)
                .append("lastContractDate", lastContractDate)
                .append("misProductGroup", misProductGroup)
                .append("accountServiceSegment", accountServiceSegment)
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("dataSource", dataSource)
                .append("accountName", accountName)
                .append("lastReviewDate", lastReviewDate)
                .append("nextReviewDate", nextReviewDate)
                .append("extendedReviewDate", extendedReviewDate)
                .append("scfScoreFinalRate", scfScoreFinalRate)
                .append("scfScoreMsFinal", scfScoreMsFinal)
                .append("scfScoreModelTypeIbnr", scfScoreModelTypeIbnr)
                .append("claimAmount", claimAmount)
                .append("comAmount", comAmount)
                .append("tmbPaidExpenseAmount", tmbPaidExpenseAmount)
                .append("tmbIntUnEarned", tmbIntUnEarned)
                .append("tmbIntAccrued", tmbIntAccrued)
                .append("limit", limit)
                .append("outstanding", outstanding)
                .append("maturityDate", maturityDate)
                .append("accountNumber", accountNumber)
                .append("accountSuffix", accountSuffix)
                .append("accountRef", accountRef)
                .append("accountStatus", accountStatus)
                .append("cardBlockCode", cardBlockCode)
                .append("cusRelAccount", cusRelAccount)
                .append("tdrFlag", tdrFlag)
                .append("numMonthIntPastDue", numMonthIntPastDue)
                .append("numMonthIntPastDueTDRAcc", numMonthIntPastDueTDRAcc)
                .append("tmbInstallmentAmt", tmbInstallmentAmt)
                .append("covenantFlag", covenantFlag)
                .append("reviewFlag", reviewFlag)
                .append("tmbUnusedBalance", tmbUnusedBalance)
                .append("tmbExtProductTypeCD", tmbExtProductTypeCD)
                .append("tmbTypeCRD", tmbTypeCRD)
                .append("tmbRMRef", tmbRMRef)
                .append("tenors", tenors)
                .toString();
    }
}
