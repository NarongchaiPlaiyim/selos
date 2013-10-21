package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BankStmtDetailView implements Serializable{
    private BigDecimal overLimitAmount;
    private BigDecimal grossCreditBalance;
    private int numberOfCreditTxn;
    private BigDecimal excludeListBDM;
    private BigDecimal excludeListUW;
    private BigDecimal creditAmountBDM;
    private BigDecimal creditAmountUW;
    private BigDecimal timesOfAvgCreditBDM;
    private BigDecimal timesOfAvgCreditUW;
    private BigDecimal debitAmount;
    private int numberOfDebitTxn;
    private Date highestBalanceDate;
    private BigDecimal highestBalance;
    private Date lowestBalanceDate;
    private BigDecimal lowestBalance;
    private BigDecimal monthBalance;
    private int numberOfChequeReturn;
    private BigDecimal chequeReturnAmount;
    private int overLimitTimes;
    private int overLimitDays;
    private BigDecimal swingPercent;
    private BigDecimal utilizationPercent;
    private BigDecimal grossInflowPerLimit;
    private int totalTransaction;
    private Date asOfDate;

    public BankStmtDetailView() {
        reset();
    }

    public void reset() {
        this.overLimitAmount = BigDecimal.valueOf(0.00);
        this.grossCreditBalance = BigDecimal.valueOf(0.00);
        this.excludeListBDM = BigDecimal.valueOf(0.0);
        this.excludeListUW = BigDecimal.valueOf(0.0);
        this.creditAmountBDM = BigDecimal.valueOf(0.00);
        this.creditAmountUW = BigDecimal.valueOf(0.00);
        this.timesOfAvgCreditBDM = BigDecimal.valueOf(0.00);
        this.timesOfAvgCreditUW = BigDecimal.valueOf(0.00);
        this.debitAmount = BigDecimal.valueOf(0.00);
        this.highestBalanceDate = new Date();
        this.highestBalance = BigDecimal.valueOf(0.00);
        this.lowestBalanceDate = new Date();
        this.lowestBalance = BigDecimal.valueOf(0.00);
        this.monthBalance = BigDecimal.valueOf(0.00);
        this.chequeReturnAmount = BigDecimal.valueOf(0.0);
        this.swingPercent = BigDecimal.valueOf(0.00);
        this.utilizationPercent = BigDecimal.valueOf(0.00);
        this.grossInflowPerLimit = BigDecimal.valueOf(0.00);
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

    public BigDecimal getTimesOfAvgCreditBDM() {
        return timesOfAvgCreditBDM;
    }

    public void setTimesOfAvgCreditBDM(BigDecimal timesOfAvgCreditBDM) {
        this.timesOfAvgCreditBDM = timesOfAvgCreditBDM;
    }

    public BigDecimal getTimesOfAvgCreditUW() {
        return timesOfAvgCreditUW;
    }

    public void setTimesOfAvgCreditUW(BigDecimal timesOfAvgCreditUW) {
        this.timesOfAvgCreditUW = timesOfAvgCreditUW;
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

    public Date getHighestBalanceDate() {
        return highestBalanceDate;
    }

    public void setHighestBalanceDate(Date highestBalanceDate) {
        this.highestBalanceDate = highestBalanceDate;
    }

    public BigDecimal getHighestBalance() {
        return highestBalance;
    }

    public void setHighestBalance(BigDecimal highestBalance) {
        this.highestBalance = highestBalance;
    }

    public Date getLowestBalanceDate() {
        return lowestBalanceDate;
    }

    public void setLowestBalanceDate(Date lowestBalanceDate) {
        this.lowestBalanceDate = lowestBalanceDate;
    }

    public BigDecimal getLowestBalance() {
        return lowestBalance;
    }

    public void setLowestBalance(BigDecimal lowestBalance) {
        this.lowestBalance = lowestBalance;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("overLimitAmount", overLimitAmount)
                .append("grossCreditBalance", grossCreditBalance)
                .append("numberOfCreditTxn", numberOfCreditTxn)
                .append("excludeListBDM", excludeListBDM)
                .append("excludeListUW", excludeListUW)
                .append("creditAmountBDM", creditAmountBDM)
                .append("creditAmountUW", creditAmountUW)
                .append("timesOfAvgCreditBDM", timesOfAvgCreditBDM)
                .append("timesOfAvgCreditUW", timesOfAvgCreditUW)
                .append("debitAmount", debitAmount)
                .append("numberOfDebitTxn", numberOfDebitTxn)
                .append("highestBalanceDate", highestBalanceDate)
                .append("highestBalance", highestBalance)
                .append("lowestBalanceDate", lowestBalanceDate)
                .append("lowestBalance", lowestBalance)
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
                .toString();
    }
}
