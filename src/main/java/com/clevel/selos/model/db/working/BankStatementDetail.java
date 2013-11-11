package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_bankstatement_detail")
public class BankStatementDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_BANKSTMT_DETAIL_ID", sequenceName = "SEQ_WRK_BANKSTMT_DETAIL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BANKSTMT_DETAIL_ID")
    private long id;

    @Column(name = "over_limit_amount")
    private BigDecimal overLimitAmount;

    @Column(name = "gross_credit_balance")
    private BigDecimal grossCreditBalance;

    @Column(name = "number_of_credit_txn")
    private int numberOfCreditTxn;

    @Column(name = "exclude_list_bdm")
    private BigDecimal excludeListBDM;

    @Column(name = "exclude_list_uw")
    private BigDecimal excludeListUW;

    @Column(name = "credit_amount_bdm")
    private BigDecimal creditAmountBDM;

    @Column(name = "credit_amount_uw")
    private BigDecimal creditAmountUW;

    @Column(name = "times_of_avg_credit_bdm")
    private BigDecimal timesOfAverageCreditBDM;

    @Column(name = "times_of_avg_credit_uw")
    private BigDecimal timesOfAverageCreditUW;

    @Column(name = "debit_amount")
    private BigDecimal debitAmount;

    @Column(name = "number_of_debit_txn")
    private int numberOfDebitTxn;

    @Column(name = "date_of_max_balance")
    private Date dateOfMaxBalance;

    @Column(name = "max_balance")
    private BigDecimal maxBalance;

    @Column(name = "date_of_min_balance")
    private Date dateOfMinBalance;

    @Column(name = "min_balance")
    private BigDecimal minBalance;

    @Column(name = "month_balance")
    private BigDecimal monthBalance;

    @Column(name = "number_of_chq_return")
    private int numberOfChequeReturn;

    @Column(name = "cheque_return_amount")
    private BigDecimal chequeReturnAmount;

    @Column(name = "over_limit_times")
    private int overLimitTimes;

    @Column(name = "over_limit_days")
    private int overLimitDays;

    @Column(name = "swing_percent")
    private BigDecimal swingPercent;

    @Column(name = "utilization_percent")
    private BigDecimal utilizationPercent;

    @Column(name = "gross_inflow_per_limit")
    private BigDecimal grossInflowPerLimit;

    @Column(name = "total_transaction")
    private int totalTransaction;

    @Column(name = "as_of_date")
    private Date asOfDate;

    @ManyToOne
    @JoinColumn(name = "bank_stmt_id")
    private BankStatement bankStatement;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getOverLimitAmount() {
        return overLimitAmount;
    }

    public void setOverLimitAmount(BigDecimal overLimitAmount) {
        this.overLimitAmount = overLimitAmount;
    }

    public BigDecimal getGrossCreditBalance() {
        return grossCreditBalance;
    }

    public void setGrossCreditBalance(BigDecimal grossCreditBalance) {
        this.grossCreditBalance = grossCreditBalance;
    }

    public int getNumberOfCreditTxn() {
        return numberOfCreditTxn;
    }

    public void setNumberOfCreditTxn(int numberOfCreditTxn) {
        this.numberOfCreditTxn = numberOfCreditTxn;
    }

    public BigDecimal getExcludeListBDM() {
        return excludeListBDM;
    }

    public void setExcludeListBDM(BigDecimal excludeListBDM) {
        this.excludeListBDM = excludeListBDM;
    }

    public BigDecimal getExcludeListUW() {
        return excludeListUW;
    }

    public void setExcludeListUW(BigDecimal excludeListUW) {
        this.excludeListUW = excludeListUW;
    }

    public BigDecimal getCreditAmountBDM() {
        return creditAmountBDM;
    }

    public void setCreditAmountBDM(BigDecimal creditAmountBDM) {
        this.creditAmountBDM = creditAmountBDM;
    }

    public BigDecimal getCreditAmountUW() {
        return creditAmountUW;
    }

    public void setCreditAmountUW(BigDecimal creditAmountUW) {
        this.creditAmountUW = creditAmountUW;
    }

    public BigDecimal getTimesOfAverageCreditBDM() {
        return timesOfAverageCreditBDM;
    }

    public void setTimesOfAverageCreditBDM(BigDecimal timesOfAverageCreditBDM) {
        this.timesOfAverageCreditBDM = timesOfAverageCreditBDM;
    }

    public BigDecimal getTimesOfAverageCreditUW() {
        return timesOfAverageCreditUW;
    }

    public void setTimesOfAverageCreditUW(BigDecimal timesOfAverageCreditUW) {
        this.timesOfAverageCreditUW = timesOfAverageCreditUW;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public int getNumberOfDebitTxn() {
        return numberOfDebitTxn;
    }

    public void setNumberOfDebitTxn(int numberOfDebitTxn) {
        this.numberOfDebitTxn = numberOfDebitTxn;
    }

    public Date getDateOfMaxBalance() {
        return dateOfMaxBalance;
    }

    public void setDateOfMaxBalance(Date dateOfMaxBalance) {
        this.dateOfMaxBalance = dateOfMaxBalance;
    }

    public BigDecimal getMaxBalance() {
        return maxBalance;
    }

    public void setMaxBalance(BigDecimal maxBalance) {
        this.maxBalance = maxBalance;
    }

    public Date getDateOfMinBalance() {
        return dateOfMinBalance;
    }

    public void setDateOfMinBalance(Date dateOfMinBalance) {
        this.dateOfMinBalance = dateOfMinBalance;
    }

    public BigDecimal getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(BigDecimal minBalance) {
        this.minBalance = minBalance;
    }

    public BigDecimal getMonthBalance() {
        return monthBalance;
    }

    public void setMonthBalance(BigDecimal monthBalance) {
        this.monthBalance = monthBalance;
    }

    public int getNumberOfChequeReturn() {
        return numberOfChequeReturn;
    }

    public void setNumberOfChequeReturn(int numberOfChequeReturn) {
        this.numberOfChequeReturn = numberOfChequeReturn;
    }

    public BigDecimal getChequeReturnAmount() {
        return chequeReturnAmount;
    }

    public void setChequeReturnAmount(BigDecimal chequeReturnAmount) {
        this.chequeReturnAmount = chequeReturnAmount;
    }

    public int getOverLimitTimes() {
        return overLimitTimes;
    }

    public void setOverLimitTimes(int overLimitTimes) {
        this.overLimitTimes = overLimitTimes;
    }

    public int getOverLimitDays() {
        return overLimitDays;
    }

    public void setOverLimitDays(int overLimitDays) {
        this.overLimitDays = overLimitDays;
    }

    public BigDecimal getSwingPercent() {
        return swingPercent;
    }

    public void setSwingPercent(BigDecimal swingPercent) {
        this.swingPercent = swingPercent;
    }

    public BigDecimal getUtilizationPercent() {
        return utilizationPercent;
    }

    public void setUtilizationPercent(BigDecimal utilizationPercent) {
        this.utilizationPercent = utilizationPercent;
    }

    public BigDecimal getGrossInflowPerLimit() {
        return grossInflowPerLimit;
    }

    public void setGrossInflowPerLimit(BigDecimal grossInflowPerLimit) {
        this.grossInflowPerLimit = grossInflowPerLimit;
    }

    public int getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(int totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public Date getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Date asOfDate) {
        this.asOfDate = asOfDate;
    }

    public BankStatement getBankStatement() {
        return bankStatement;
    }

    public void setBankStatement(BankStatement bankStatement) {
        this.bankStatement = bankStatement;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("overLimitAmount", overLimitAmount)
                .append("grossCreditBalance", grossCreditBalance)
                .append("numberOfCreditTxn", numberOfCreditTxn)
                .append("excludeListBDM", excludeListBDM)
                .append("excludeListUW", excludeListUW)
                .append("creditAmountBDM", creditAmountBDM)
                .append("creditAmountUW", creditAmountUW)
                .append("timesOfAverageCreditBDM", timesOfAverageCreditBDM)
                .append("timesOfAverageCreditUW", timesOfAverageCreditUW)
                .append("debitAmount", debitAmount)
                .append("numberOfDebitTxn", numberOfDebitTxn)
                .append("dateOfMaxBalance", dateOfMaxBalance)
                .append("maxBalance", maxBalance)
                .append("dateOfMinBalance", dateOfMinBalance)
                .append("minBalance", minBalance)
                .append("monthBalance", monthBalance)
                .append("numberOfChequeReturn", numberOfChequeReturn)
                .append("chequeReturnAmount", chequeReturnAmount)
                .append("overLimitTimes", overLimitTimes)
                .append("overLimitDays", overLimitDays)
                .append("swingPercent", swingPercent)
                .append("utilizationPercent", utilizationPercent)
                .append("grossInflowPerLimit", grossInflowPerLimit)
                .append("totalTransaction", totalTransaction)
                .append("asOfDate", asOfDate)
                .append("bankStatement", bankStatement)
                .toString();
    }
}
