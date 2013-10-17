package com.clevel.selos.integration.dwh.bankstatement.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BankStatement implements Serializable {
    private String accountStatus;
    Date accountOpenDate;
    private String branchCode;
    private String accountType;
    private String accountNumber;
    private String accountName;
    private BigDecimal overLimitAmount;
    private BigDecimal grossCreditBalance;
    private int numberOfCreditTxn;
    private BigDecimal debitAmount;
    private int numberOfDebitTxn;
    private Date highestBalanceDate;
    private BigDecimal highestBalance;
    private Date lowestBalanceDate;
    private BigDecimal lowestBalance;
    private BigDecimal monthEndBalance;
    private int numberOfChequeReturn;
    private int chequeReturnAmount;
    private int numberOfTimesOD;
    private Date startODDate;
    private Date endODDate;

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public BigDecimal getMonthEndBalance() {
        return monthEndBalance;
    }

    public void setMonthEndBalance(BigDecimal monthEndBalance) {
        this.monthEndBalance = monthEndBalance;
    }

    public int getNumberOfChequeReturn() {
        return numberOfChequeReturn;
    }

    public void setNumberOfChequeReturn(int numberOfChequeReturn) {
        this.numberOfChequeReturn = numberOfChequeReturn;
    }

    public int getChequeReturnAmount() {
        return chequeReturnAmount;
    }

    public void setChequeReturnAmount(int chequeReturnAmount) {
        this.chequeReturnAmount = chequeReturnAmount;
    }

    public int getNumberOfTimesOD() {
        return numberOfTimesOD;
    }

    public void setNumberOfTimesOD(int numberOfTimesOD) {
        this.numberOfTimesOD = numberOfTimesOD;
    }

    public Date getStartODDate() {
        return startODDate;
    }

    public void setStartODDate(Date startODDate) {
        this.startODDate = startODDate;
    }

    public Date getEndODDate() {
        return endODDate;
    }

    public void setEndODDate(Date endODDate) {
        this.endODDate = endODDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("accountStatus", accountStatus)
                .append("accountOpenDate", accountOpenDate)
                .append("branchCode", branchCode)
                .append("accountType", accountType)
                .append("accountNumber", accountNumber)
                .append("accountName", accountName)
                .append("overLimitAmount", overLimitAmount)
                .append("grossCreditBalance", grossCreditBalance)
                .append("numberOfCreditTxn", numberOfCreditTxn)
                .append("debitAmount", debitAmount)
                .append("numberOfDebitTxn", numberOfDebitTxn)
                .append("highestBalanceDate", highestBalanceDate)
                .append("highestBalance", highestBalance)
                .append("lowestBalanceDate", lowestBalanceDate)
                .append("lowestBalance", lowestBalance)
                .append("monthEndBalance", monthEndBalance)
                .append("numberOfChequeReturn", numberOfChequeReturn)
                .append("chequeReturnAmount", chequeReturnAmount)
                .append("numberOfTimesOD", numberOfTimesOD)
                .append("startODDate", startODDate)
                .append("endODDate", endODDate)
                .toString();
    }
}
