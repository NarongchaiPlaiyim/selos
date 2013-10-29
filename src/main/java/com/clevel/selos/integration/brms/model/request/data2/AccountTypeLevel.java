package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountTypeLevel implements Serializable {
    private BigDecimal utilizationPercent;
    private BigDecimal swingPercent;
    private BigDecimal avgL6MInFlowLimit;
    private BigDecimal noOfTransaction;
    private BigDecimal chequeReturnTime;
    private BigDecimal odOverLimitDay;
    private BigDecimal cashInflow;
    private boolean mainAccountFlag;
    private boolean highestInflowFlag;
    private boolean tmbAccountFlag;
    private boolean excludeIncomeFlag;

    public AccountTypeLevel() {
    }

    public AccountTypeLevel(BigDecimal utilizationPercent, BigDecimal swingPercent, BigDecimal avgL6MInFlowLimit, BigDecimal noOfTransaction, BigDecimal chequeReturnTime, BigDecimal odOverLimitDay, BigDecimal cashInflow, boolean mainAccountFlag, boolean highestInflowFlag, boolean tmbAccountFlag, boolean excludeIncomeFlag) {
        this.utilizationPercent = utilizationPercent;
        this.swingPercent = swingPercent;
        this.avgL6MInFlowLimit = avgL6MInFlowLimit;
        this.noOfTransaction = noOfTransaction;
        this.chequeReturnTime = chequeReturnTime;
        this.odOverLimitDay = odOverLimitDay;
        this.cashInflow = cashInflow;
        this.mainAccountFlag = mainAccountFlag;
        this.highestInflowFlag = highestInflowFlag;
        this.tmbAccountFlag = tmbAccountFlag;
        this.excludeIncomeFlag = excludeIncomeFlag;
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

    public BigDecimal getAvgL6MInFlowLimit() {
        return avgL6MInFlowLimit;
    }

    public void setAvgL6MInFlowLimit(BigDecimal avgL6MInFlowLimit) {
        this.avgL6MInFlowLimit = avgL6MInFlowLimit;
    }

    public BigDecimal getNoOfTransaction() {
        return noOfTransaction;
    }

    public void setNoOfTransaction(BigDecimal noOfTransaction) {
        this.noOfTransaction = noOfTransaction;
    }

    public BigDecimal getChequeReturnTime() {
        return chequeReturnTime;
    }

    public void setChequeReturnTime(BigDecimal chequeReturnTime) {
        this.chequeReturnTime = chequeReturnTime;
    }

    public BigDecimal getOdOverLimitDay() {
        return odOverLimitDay;
    }

    public void setOdOverLimitDay(BigDecimal odOverLimitDay) {
        this.odOverLimitDay = odOverLimitDay;
    }

    public BigDecimal getCashInflow() {
        return cashInflow;
    }

    public void setCashInflow(BigDecimal cashInflow) {
        this.cashInflow = cashInflow;
    }

    public boolean isMainAccountFlag() {
        return mainAccountFlag;
    }

    public void setMainAccountFlag(boolean mainAccountFlag) {
        this.mainAccountFlag = mainAccountFlag;
    }

    public boolean isHighestInflowFlag() {
        return highestInflowFlag;
    }

    public void setHighestInflowFlag(boolean highestInflowFlag) {
        this.highestInflowFlag = highestInflowFlag;
    }

    public boolean isTmbAccountFlag() {
        return tmbAccountFlag;
    }

    public void setTmbAccountFlag(boolean tmbAccountFlag) {
        this.tmbAccountFlag = tmbAccountFlag;
    }

    public boolean isExcludeIncomeFlag() {
        return excludeIncomeFlag;
    }

    public void setExcludeIncomeFlag(boolean excludeIncomeFlag) {
        this.excludeIncomeFlag = excludeIncomeFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("utilizationPercent", utilizationPercent)
                .append("swingPercent", swingPercent)
                .append("avgL6MInFlowLimit", avgL6MInFlowLimit)
                .append("noOfTransaction", noOfTransaction)
                .append("chequeReturnTime", chequeReturnTime)
                .append("odOverLimitDay", odOverLimitDay)
                .append("cashInflow", cashInflow)
                .append("mainAccountFlag", mainAccountFlag)
                .append("highestInflowFlag", highestInflowFlag)
                .append("tmbAccountFlag", tmbAccountFlag)
                .append("excludeIncomeFlag", excludeIncomeFlag)
                .toString();
    }
}
