package com.clevel.selos.integration.brms.model.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class BRMSAccountStmtInfo implements Serializable{
    private BigDecimal avgUtilizationPercent;
    private BigDecimal avgSwingPercent;
    private BigDecimal avgIncomeGross;
    private BigDecimal totalTransaction; // total of Inflow and Outflow Transaction
    private BigDecimal checkReturn;
    private BigDecimal overLimitDays; //OD Over limit Days;
    private BigDecimal avgGrossInflowPerLimit;
    private boolean mainAccount;
    private boolean highestInflow;
    private boolean tmb;
    private boolean notCountIncome;

    public BigDecimal getAvgUtilizationPercent() {
        return avgUtilizationPercent;
    }

    public void setAvgUtilizationPercent(BigDecimal avgUtilizationPercent) {
        this.avgUtilizationPercent = avgUtilizationPercent;
    }

    public BigDecimal getAvgSwingPercent() {
        return avgSwingPercent;
    }

    public void setAvgSwingPercent(BigDecimal avgSwingPercent) {
        this.avgSwingPercent = avgSwingPercent;
    }

    public BigDecimal getAvgIncomeGross() {
        return avgIncomeGross;
    }

    public void setAvgIncomeGross(BigDecimal avgIncomeGross) {
        this.avgIncomeGross = avgIncomeGross;
    }

    public BigDecimal getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(BigDecimal totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public BigDecimal getCheckReturn() {
        return checkReturn;
    }

    public void setCheckReturn(BigDecimal checkReturn) {
        this.checkReturn = checkReturn;
    }

    public BigDecimal getOverLimitDays() {
        return overLimitDays;
    }

    public void setOverLimitDays(BigDecimal overLimitDays) {
        this.overLimitDays = overLimitDays;
    }

    public BigDecimal getAvgGrossInflowPerLimit() {
        return avgGrossInflowPerLimit;
    }

    public void setAvgGrossInflowPerLimit(BigDecimal avgGrossInflowPerLimit) {
        this.avgGrossInflowPerLimit = avgGrossInflowPerLimit;
    }

    public boolean isMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(boolean mainAccount) {
        this.mainAccount = mainAccount;
    }

    public boolean isHighestInflow() {
        return highestInflow;
    }

    public void setHighestInflow(boolean highestInflow) {
        this.highestInflow = highestInflow;
    }

    public boolean isTmb() {
        return tmb;
    }

    public void setTmb(boolean tmb) {
        this.tmb = tmb;
    }

    public boolean isNotCountIncome() {
        return notCountIncome;
    }

    public void setNotCountIncome(boolean notCountIncome) {
        this.notCountIncome = notCountIncome;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("avgUtilizationPercent", avgUtilizationPercent)
                .append("avgSwingPercent", avgSwingPercent)
                .append("avgIncomeGross", avgIncomeGross)
                .append("totalTransaction", totalTransaction)
                .append("checkReturn", checkReturn)
                .append("overLimitDays", overLimitDays)
                .append("avgGrossInflowPerLimit", avgGrossInflowPerLimit)
                .append("mainAccount", mainAccount)
                .append("highestInflow", highestInflow)
                .append("tmb", tmb)
                .append("notCountIncome", notCountIncome)
                .toString();
    }
}
