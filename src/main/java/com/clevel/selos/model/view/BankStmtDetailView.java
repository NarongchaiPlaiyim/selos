package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountType;
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
    private BigDecimal timesOfAverageCreditBDM;
    private BigDecimal timesOfAverageCreditUW;
    private BigDecimal debitAmount;
    private int numberOfDebitTxn;
    private Date dateOfMaxBalance;
    private BigDecimal maxBalance;
    private Date dateOfMinBalance;
    private BigDecimal minBalance;
    private BigDecimal monthBalance;
    private int numberOfChequeReturn;
    private BigDecimal chequeReturnAmount;
    private int overLimitTimes;
    private int overLimitDays;
    private BigDecimal swingPercent;
    private BigDecimal utilizationPercent;
    private BigDecimal grossInflowPerLimit;
    private int totalTransaction;

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
                .toString();
    }
}
