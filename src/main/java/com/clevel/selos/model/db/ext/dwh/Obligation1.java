package com.clevel.selos.model.db.ext.dwh;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ext_dwh_obligation1")
public class Obligation1 implements Serializable {
    @Id
    private long id;

    @Column(name = "tmb_cus_id", length = 30)
    private String tmbCusId;

    @Column(name = "tmb_bot_cus_id", length = 140)
    private String tmbBotCusId;

    @Column(name = "service_segment", length = 1)
    private String serviceSegment;

    @Column(name = "adjust_class", length = 2)
    private String adjustClass;

    @Column(name = "last_contract_date")
    private Date lastContractDate;

    @Column(name = "mis_product_group", length = 10)
    private String misProductGroup;

    @Column(name = "account_service_segment", length = 1)
    private String accountServiceSegment;

    @Column(name = "product_code", length = 10)
    private String productCode;

    @Column(name = "project_code", length = 5)
    private String projectCode;

    @Column(name = "data_source", length = 2)
    private String dataSource;

    @Column(name = "account_name", length = 50)
    private String accountName;

    @Column(name = "last_review_date")
    private Date lastReviewDate;

    @Column(name = "next_review_date")
    private Date nextReviewDate;

    @Column(name = "extended_review_date")
    private Date extendedReviewDate;

    @Column(name = "scfscore_final_rate", length = 4)
    private String scfScoreFinalRate;

    @Column(name = "scfscore_ms_final", length = 8)
    private String scfScoreMsFinal;

    @Column(name = "scfscore_model_type_ibnr", length = 50)
    private String scfScoreModelTypeIbnr;

    @Column(name = "claim_amount")
    private BigDecimal claimAmount;

    @Column(name = "com_amount")
    private BigDecimal comAmount;

    @Column(name = "tmb_paid_expense_amount")
    private BigDecimal tmbPaidExpenseAmount;

    @Column(name = "int_unearned")
    private BigDecimal intUnEarned;

    @Column(name = "int_accrued")
    private BigDecimal intAccrued;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "outstanding")
    private BigDecimal outstanding;

    @Column(name = "maturity_date")
    private Date maturityDate;

    @Column(name = "account_number", length = 10)
    private String accountNumber;

    @Column(name = "account_suffix", length = 3)
    private String accountSuffix;

    @Column(name = "account_ref", length = 17)
    private String accountRef;

    @Column(name = "account_status", length = 2)
    private String accountStatus;

    @Column(name = "card_block_code", length = 1)
    private String cardBlockCode;

    @Column(name = "cus_rel_account", length = 3)
    private String cusRelAccount;

    @Column(name = "tdr_flag", length = 1)
    private String tdrFlag;

    @Column(name = "num_month_int_past_due", length = 5, precision = 0)
    private BigDecimal numMonthIntPastDue;

    @Column(name = "num_month_int_past_due_tdr_acc", length = 5, precision = 0)
    private BigDecimal numMonthIntPastDueTDRAcc;

    @Column(name = "tmb_del_pri_day", length = 5, precision = 0)
    private BigDecimal tmbDelPriDay;

    @Column(name = "tmb_del_int_day", length = 5, precision = 0)
    private BigDecimal tmbDelIntDay;

    @Column(name = "tmb_installment_amt")
    private BigDecimal tmbInstallmentAmt;

    @Column(name = "covenant_flag", length = 1)
    private String covenantFlag;

    @Column(name = "review_flag", length = 1)
    private String reviewFlag;

    @Column(name = "tmb_unused_bal")
    private BigDecimal tmbUnusedBalance;

    @Column(name = "tmb_ext_product_type_cd", length = 3)
    private String tmbExtProductTypeCD;

    @Column(name = "tmb_type_crd", length = 4)
    private String tmbTypeCRD;

    @Column(name = "tmb_rm_ref", length = 6)
    private String tmbRMRef;

    public Obligation1() {
    }

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
        if (claimAmount == null) {
            return BigDecimal.ZERO;
        }
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public BigDecimal getComAmount() {
        if (comAmount == null) {
            return BigDecimal.ZERO;
        }
        return comAmount;
    }

    public void setComAmount(BigDecimal comAmount) {
        this.comAmount = comAmount;
    }

    public BigDecimal getTmbPaidExpenseAmount() {
        if (tmbPaidExpenseAmount == null) {
            return BigDecimal.ZERO;
        }
        return tmbPaidExpenseAmount;
    }

    public void setTmbPaidExpenseAmount(BigDecimal tmbPaidExpenseAmount) {
        this.tmbPaidExpenseAmount = tmbPaidExpenseAmount;
    }

    public BigDecimal getIntUnEarned() {
        if (intUnEarned == null) {
            return BigDecimal.ZERO;
        }
        return intUnEarned;
    }

    public void setIntUnEarned(BigDecimal intUnEarned) {
        this.intUnEarned = intUnEarned;
    }

    public BigDecimal getIntAccrued() {
        if (intAccrued == null) {
            return BigDecimal.ZERO;
        }
        return intAccrued;
    }

    public void setIntAccrued(BigDecimal intAccrued) {
        this.intAccrued = intAccrued;
    }

    public BigDecimal getLimit() {
        if (limit == null) {
            return BigDecimal.ZERO;
        }
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getOutstanding() {
        if (outstanding == null) {
            return BigDecimal.ZERO;
        }
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
        if (numMonthIntPastDue == null) {
            return BigDecimal.ZERO;
        }
        return numMonthIntPastDue;
    }

    public void setNumMonthIntPastDue(BigDecimal numMonthIntPastDue) {
        this.numMonthIntPastDue = numMonthIntPastDue;
    }

    public BigDecimal getNumMonthIntPastDueTDRAcc() {
        if (numMonthIntPastDueTDRAcc == null) {
            return BigDecimal.ZERO;
        }
        return numMonthIntPastDueTDRAcc;
    }

    public void setNumMonthIntPastDueTDRAcc(BigDecimal numMonthIntPastDueTDRAcc) {
        this.numMonthIntPastDueTDRAcc = numMonthIntPastDueTDRAcc;
    }

    public BigDecimal getTmbDelPriDay() {
        return tmbDelPriDay;
    }

    public void setTmbDelPriDay(BigDecimal tmbDelPriDay) {
        this.tmbDelPriDay = tmbDelPriDay;
    }

    public BigDecimal getTmbDelIntDay() {
        return tmbDelIntDay;
    }

    public void setTmbDelIntDay(BigDecimal tmbDelIntDay) {
        this.tmbDelIntDay = tmbDelIntDay;
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
                .append("intUnEarned", intUnEarned)
                .append("intAccrued", intAccrued)
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
                .toString();
    }
}
