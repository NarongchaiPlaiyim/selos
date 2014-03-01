package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_ncb_detail")
public class NCBDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NCB_DETAIL_ID", sequenceName = "SEQ_WRK_NCB_DETAIL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NCB_DETAIL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "ncb_id")
    private NCB ncb;

    @OneToOne
    @JoinColumn(name = "account_type_id")
    private AccountType accountType;

    @Column(name = "account_tmb_flag")
    private int accountTMBFlag;

    @OneToOne
    @JoinColumn(name = "account_status_id")
    private AccountStatus accountStatus;

    @Column(name = "as_of_date")
    private Date asOfDate;

    @Column(name = "account_open_date")
    private Date accountOpenDate;

    @Column(name = "account_close_date")
    private Date accountCloseDate;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "outstanding")
    private BigDecimal outstanding;

    @Column(name = "installment")
    private BigDecimal installment;

    @Column(name = "last_restructure_date")
    private Date lastReStructureDate;

    @OneToOne
    @JoinColumn(name = "current_payment_id")
    private SettlementStatus currentPayment;

    @OneToOne
    @JoinColumn(name = "history_six_payment_id")
    private SettlementStatus historySixPayment;

    @OneToOne
    @JoinColumn(name = "history_twl_payment_id")
    private SettlementStatus historyTwelvePayment;

    @Column(name = "outstanding_in_12_month")
    private int outstandingIn12Month;

    @Column(name = "over_limit")
    private int overLimit;

    @Column(name = "refinance_flag")
    private int refinanceFlag;

    @Column(name = "no_of_months_payment")
    private int noOfMonthPayment;

    @Column(name = "npl_flag")
    private int nplFlag;

    @Column(name = "npl_credit_amount")
    private BigDecimal nplCreditAmount;

    @Column(name = "tdr_flag")
    private int tdrFlag;

    @Column(name = "wcFlag")
    private int wcFlag;

    @Column(name = "can_edit")
    private boolean canToEdit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NCB getNcb() {
        return ncb;
    }

    public void setNcb(NCB ncb) {
        this.ncb = ncb;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Date asOfDate) {
        this.asOfDate = asOfDate;
    }

    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    public Date getAccountCloseDate() {
        return accountCloseDate;
    }

    public void setAccountCloseDate(Date accountCloseDate) {
        this.accountCloseDate = accountCloseDate;
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

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public Date getLastReStructureDate() {
        return lastReStructureDate;
    }

    public void setLastReStructureDate(Date lastReStructureDate) {
        this.lastReStructureDate = lastReStructureDate;
    }

    public SettlementStatus getCurrentPayment() {
        return currentPayment;
    }

    public void setCurrentPayment(SettlementStatus currentPayment) {
        this.currentPayment = currentPayment;
    }

    public SettlementStatus getHistorySixPayment() {
        return historySixPayment;
    }

    public void setHistorySixPayment(SettlementStatus historySixPayment) {
        this.historySixPayment = historySixPayment;
    }

    public SettlementStatus getHistoryTwelvePayment() {
        return historyTwelvePayment;
    }

    public void setHistoryTwelvePayment(SettlementStatus historyTwelvePayment) {
        this.historyTwelvePayment = historyTwelvePayment;
    }

    public int getOutstandingIn12Month() {
        return outstandingIn12Month;
    }

    public void setOutstandingIn12Month(int outstandingIn12Month) {
        this.outstandingIn12Month = outstandingIn12Month;
    }

    public int getOverLimit() {
        return overLimit;
    }

    public void setOverLimit(int overLimit) {
        this.overLimit = overLimit;
    }

    public int getNoOfMonthPayment() {
        return noOfMonthPayment;
    }

    public void setNoOfMonthPayment(int noOfMonthPayment) {
        this.noOfMonthPayment = noOfMonthPayment;
    }

    public int getRefinanceFlag() {
        return refinanceFlag;
    }

    public void setRefinanceFlag(int refinanceFlag) {
        this.refinanceFlag = refinanceFlag;
    }

    public int getWcFlag() {
        return wcFlag;
    }

    public void setWcFlag(int wcFlag) {
        this.wcFlag = wcFlag;
    }

    public int getAccountTMBFlag() {
        return accountTMBFlag;
    }

    public int getNplFlag() {
        return nplFlag;
    }

    public void setNplFlag(int nplFlag) {
        this.nplFlag = nplFlag;
    }

    public BigDecimal getNplCreditAmount() {
        return nplCreditAmount;
    }

    public void setNplCreditAmount(BigDecimal nplCreditAmount) {
        this.nplCreditAmount = nplCreditAmount;
    }

    public int getTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(int tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public void setAccountTMBFlag(int accountTMBFlag) {
        this.accountTMBFlag = accountTMBFlag;
    }

    public boolean isCanToEdit() {
        return canToEdit;
    }

    public void setCanToEdit(boolean canToEdit) {
        this.canToEdit = canToEdit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("ncb", ncb)
                .append("accountType", accountType)
                .append("accountTMBFlag", accountTMBFlag)
                .append("accountStatus", accountStatus)
                .append("asOfDate", asOfDate)
                .append("accountOpenDate", accountOpenDate)
                .append("accountCloseDate", accountCloseDate)
                .append("limit", limit)
                .append("outstanding", outstanding)
                .append("installment", installment)
                .append("lastReStructureDate", lastReStructureDate)
                .append("currentPayment", currentPayment)
                .append("historySixPayment", historySixPayment)
                .append("historyTwelvePayment", historyTwelvePayment)
                .append("outstandingIn12Month", outstandingIn12Month)
                .append("overLimit", overLimit)
                .append("refinanceFlag", refinanceFlag)
                .append("noOfMonthPayment", noOfMonthPayment)
                .append("nplFlag", nplFlag)
                .append("nplCreditAmount", nplCreditAmount)
                .append("tdrFlag", tdrFlag)
                .append("wcFlag", wcFlag)
                .append("canToEdit", canToEdit)
                .toString();
    }
}
