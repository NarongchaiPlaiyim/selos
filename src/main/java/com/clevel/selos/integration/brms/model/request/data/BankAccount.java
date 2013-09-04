package com.clevel.selos.integration.brms.model.request.data;

import java.math.BigDecimal;

public class BankAccount {
    public int chequeReturn;
    public BigDecimal tradeChequeReturnPercent;
    public BigDecimal utilizationPercent;
    public BigDecimal swingPercent;
    public BigDecimal averageIncomeGross;
    public int overLimitDays;
    public boolean isMainAccount;
    //todo add more field

    public BankAccount() {
    }

    public BankAccount(int chequeReturn, BigDecimal tradeChequeReturnPercent, BigDecimal utilizationPercent, BigDecimal swingPercent, BigDecimal averageIncomeGross, int overLimitDays, boolean mainAccount) {
        this.chequeReturn = chequeReturn;
        this.tradeChequeReturnPercent = tradeChequeReturnPercent;
        this.utilizationPercent = utilizationPercent;
        this.swingPercent = swingPercent;
        this.averageIncomeGross = averageIncomeGross;
        this.overLimitDays = overLimitDays;
        isMainAccount = mainAccount;
    }

    public int getChequeReturn() {
        return chequeReturn;
    }

    public void setChequeReturn(int chequeReturn) {
        this.chequeReturn = chequeReturn;
    }

    public BigDecimal getTradeChequeReturnPercent() {
        return tradeChequeReturnPercent;
    }

    public void setTradeChequeReturnPercent(BigDecimal tradeChequeReturnPercent) {
        this.tradeChequeReturnPercent = tradeChequeReturnPercent;
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

    public BigDecimal getAverageIncomeGross() {
        return averageIncomeGross;
    }

    public void setAverageIncomeGross(BigDecimal averageIncomeGross) {
        this.averageIncomeGross = averageIncomeGross;
    }

    public int getOverLimitDays() {
        return overLimitDays;
    }

    public void setOverLimitDays(int overLimitDays) {
        this.overLimitDays = overLimitDays;
    }

    public boolean isMainAccount() {
        return isMainAccount;
    }

    public void setMainAccount(boolean mainAccount) {
        isMainAccount = mainAccount;
    }
}
