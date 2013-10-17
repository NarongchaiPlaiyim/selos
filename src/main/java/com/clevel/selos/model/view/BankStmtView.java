package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankStmtView implements Serializable {
    private String accountName;
    private String accountNumber;
    private String accountType;
    private boolean isMainAccount;
    private BigDecimal limit;
    private BigDecimal incomeGross;
    private BigDecimal incomeNetBDM;
    private BigDecimal incomeNetUW;
    private BigDecimal utilizationPercent;
    private BigDecimal swingPercent;
    private int overLimitTimes;
    private int overLimitDays;
    private int chequeReturn;
    private BigDecimal cashFlowLimit;
    private BigDecimal tradeChequeReturnAmount;
    private BigDecimal tradeChequeReturnPercent;

    public BankStmtView() {
        reset();
    }

    public void reset() {
        this.accountName = "";
        this.accountNumber = "";
        this.accountType = "";
        this.limit = BigDecimal.ZERO;
        this.incomeGross = BigDecimal.ZERO;
        this.incomeNetBDM = BigDecimal.ZERO;
        this.incomeNetUW = BigDecimal.ZERO;
        this.utilizationPercent = BigDecimal.ZERO;
        this.swingPercent = BigDecimal.ZERO;
        this.cashFlowLimit = BigDecimal.ZERO;
        this.tradeChequeReturnAmount = BigDecimal.ZERO;
        this.tradeChequeReturnPercent = BigDecimal.ZERO;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isMainAccount() {
        return isMainAccount;
    }

    public void setMainAccount(boolean mainAccount) {
        isMainAccount = mainAccount;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getIncomeGross() {
        return incomeGross;
    }

    public void setIncomeGross(BigDecimal incomeGross) {
        this.incomeGross = incomeGross;
    }

    public BigDecimal getIncomeNetBDM() {
        return incomeNetBDM;
    }

    public void setIncomeNetBDM(BigDecimal incomeNetBDM) {
        this.incomeNetBDM = incomeNetBDM;
    }

    public BigDecimal getIncomeNetUW() {
        return incomeNetUW;
    }

    public void setIncomeNetUW(BigDecimal incomeNetUW) {
        this.incomeNetUW = incomeNetUW;
    }

    public BigDecimal getUtilizationPercent() {
        return utilizationPercent;
    }

    public void setUtilizationPercent(BigDecimal utilizationPercent) {
        this.utilizationPercent = utilizationPercent;
    }

    public BigDecimal getSwingPercent() {
        return swingPercent;
    }

    public void setSwingPercent(BigDecimal swingPercent) {
        this.swingPercent = swingPercent;
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

    public int getChequeReturn() {
        return chequeReturn;
    }

    public void setChequeReturn(int chequeReturn) {
        this.chequeReturn = chequeReturn;
    }

    public BigDecimal getCashFlowLimit() {
        return cashFlowLimit;
    }

    public void setCashFlowLimit(BigDecimal cashFlowLimit) {
        this.cashFlowLimit = cashFlowLimit;
    }

    public BigDecimal getTradeChequeReturnAmount() {
        return tradeChequeReturnAmount;
    }

    public void setTradeChequeReturnAmount(BigDecimal tradeChequeReturnAmount) {
        this.tradeChequeReturnAmount = tradeChequeReturnAmount;
    }

    public BigDecimal getTradeChequeReturnPercent() {
        return tradeChequeReturnPercent;
    }

    public void setTradeChequeReturnPercent(BigDecimal tradeChequeReturnPercent) {
        this.tradeChequeReturnPercent = tradeChequeReturnPercent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("accountName", accountName)
                .append("accountNumber", accountNumber)
                .append("accountType", accountType)
                .append("isMainAccount", isMainAccount)
                .append("limit", limit)
                .append("incomeGross", incomeGross)
                .append("incomeNetBDM", incomeNetBDM)
                .append("incomeNetUW", incomeNetUW)
                .append("utilizationPercent", utilizationPercent)
                .append("swingPercent", swingPercent)
                .append("overLimitTimes", overLimitTimes)
                .append("overLimitDays", overLimitDays)
                .append("chequeReturn", chequeReturn)
                .append("cashFlowLimit", cashFlowLimit)
                .append("tradeChequeReturnAmount", tradeChequeReturnAmount)
                .append("tradeChequeReturnPercent", tradeChequeReturnPercent)
                .toString();
    }
}
