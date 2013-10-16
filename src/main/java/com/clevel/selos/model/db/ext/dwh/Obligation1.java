package com.clevel.selos.model.db.ext.dwh;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ext_dwh_obligation1")
public class Obligation1 implements Serializable {
    @Id
    private long id;

    @Column(name="tmb_cus_id", length = 30)
    private String tmbCusId;

    @Column(name="tmb_bot_cus_id", length = 140)
    private String tmbBotCusId;

    @Column(name="service_segment", length = 1)
    private String serviceSegment;

    @Column(name="adjust_class", length = 2)
    private String adjustClass;

    @Column(name="last_contract_date")
    private Date lastContractDate;

    @Column(name="mis_product_group", length = 10)
    private String misProductGroup;

    @Column(name="account_service_segment", length = 1)
    private String accountServiceSegment;

    @Column(name="product_code", length = 10)
    private String productCode;

    @Column(name="project_code", length = 5)
    private String projectCode;

    @Column(name="data_source", length = 2)
    private String dataSource;

    @Column(name="account_name", length = 50)
    private String accountName;

    @Column(name="last_review_date")
    private Date lastReviewDate;

    @Column(name="next_review_date")
    private Date nextReviewDate;

    @Column(name="extended_review_date")
    private Date extendedReviewDate;

    @Column(name="scfscore_final_rate", length = 4)
    private String scfScoreFinalRate;

    @Column(name="scfscore_ms_final", length = 8)
    private String scfScoreMsFinal;

    @Column(name="scfscore_model_type_ibnr", length = 50)
    private String scfScoreModelTypeIbnr;

    @Column(name="claim_amount")
    private BigDecimal claimAmount;

    @Column(name="com_amount")
    private BigDecimal comAmount;

    @Column(name="tmb_paid_expense_amount")
    private BigDecimal tmbPaidExpenseAmount;

    @Column(name="int_unearned")
    private BigDecimal intUnEarned;

    @Column(name="int_accrued")
    private BigDecimal intAccrued;

    @Column(name="limit")
    private BigDecimal limit;

    @Column(name="outstanding")
    private BigDecimal outstanding;

    @Column(name="account_number", length =  10)
    private String accountNumber;

    @Column(name="account_suffix", length = 3)
    private String accountSuffix;

    @Column(name="account_ref", length = 17)
    private String accountRef;

    @Column(name="account_status", length = 2)
    private String accountStatus;

    @Column(name="card_block_code", length = 1)
    private String cardBlockCode;

    @Column(name="cus_rel_account", length = 3)
    private String cusRelAccount;

    @Column(name="tdr_flag", length = 1)
    private String tdrFlag;

    @Column(name="num_month_int_past_due")
    private BigDecimal numMonthIntPastDue;

    @Column(name="num_month_int_past_due_tdr_acc")
    private BigDecimal numMonthIntPastDueTDRAcc;

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

    public BigDecimal getIntUnEarned() {
        return intUnEarned;
    }

    public void setIntUnEarned(BigDecimal intUnEarned) {
        this.intUnEarned = intUnEarned;
    }

    public BigDecimal getIntAccrued() {
        return intAccrued;
    }

    public void setIntAccrued(BigDecimal intAccrued) {
        this.intAccrued = intAccrued;
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
}
