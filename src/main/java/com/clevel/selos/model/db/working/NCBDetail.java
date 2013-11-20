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
    @JoinColumn(name = "history_payment_id")
    private SettlementStatus historyPayment;

    @Column(name = "outstanding_in_12_month")
    private BigDecimal outstandingIn12Month;

    @Column(name = "over_limit")
    private int overLimit;

    @Column(name = "refinance_flag")
    private int refinanceFlag;

    @Column(name = "no_of_months_payment")
    private int noOfMonthPayment;

    @Column(name = "month1")
    private BigDecimal month1;

    @Column(name = "month2")
    private BigDecimal month2;

    @Column(name = "month3")
    private BigDecimal month3;

    @Column(name = "month4")
    private BigDecimal month4;

    @Column(name = "month5")
    private BigDecimal month5;

    @Column(name = "month6")
    private BigDecimal month6;

    @Column(name = "wcFlag")
    private int wcFlag;


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

    public SettlementStatus getHistoryPayment() {
        return historyPayment;
    }

    public void setHistoryPayment(SettlementStatus historyPayment) {
        this.historyPayment = historyPayment;
    }

    public BigDecimal getOutstandingIn12Month() {
        return outstandingIn12Month;
    }

    public void setOutstandingIn12Month(BigDecimal outstandingIn12Month) {
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

    public BigDecimal getMonth1() {
        return month1;
    }

    public void setMonth1(BigDecimal month1) {
        this.month1 = month1;
    }

    public BigDecimal getMonth2() {
        return month2;
    }

    public void setMonth2(BigDecimal month2) {
        this.month2 = month2;
    }

    public BigDecimal getMonth3() {
        return month3;
    }

    public void setMonth3(BigDecimal month3) {
        this.month3 = month3;
    }

    public BigDecimal getMonth4() {
        return month4;
    }

    public void setMonth4(BigDecimal month4) {
        this.month4 = month4;
    }

    public BigDecimal getMonth5() {
        return month5;
    }

    public void setMonth5(BigDecimal month5) {
        this.month5 = month5;
    }

    public BigDecimal getMonth6() {
        return month6;
    }

    public void setMonth6(BigDecimal month6) {
        this.month6 = month6;
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

    public void setAccountTMBFlag(int accountTMBFlag) {
        this.accountTMBFlag = accountTMBFlag;
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
                .append("limit", limit)
                .append("outstanding", outstanding)
                .append("installment", installment)
                .append("lastReStructureDate", lastReStructureDate)
                .append("currentPayment", currentPayment)
                .append("historyPayment", historyPayment)
                .append("outstandingIn12Month", outstandingIn12Month)
                .append("overLimit", overLimit)
                .append("refinanceFlag", refinanceFlag)
                .append("noOfMonthPayment", noOfMonthPayment)
                .append("month1", month1)
                .append("month2", month2)
                .append("month3", month3)
                .append("month4", month4)
                .append("month5", month5)
                .append("month6", month6)
                .append("wcFlag", wcFlag)
                .toString();
    }
}
