package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankAccountLevel implements Serializable {
    private BigDecimal utilizationPercent;
    private BigDecimal swingPercent;
    private BigDecimal avgBalanceL6Mth;
    private int numberOfTransaction;
    private boolean excludeIncomeFlag;

    public BankAccountLevel() {
    }

    public BankAccountLevel(BigDecimal utilizationPercent, BigDecimal swingPercent, BigDecimal avgBalanceL6Mth, int numberOfTransaction, boolean excludeIncomeFlag) {
        this.utilizationPercent = utilizationPercent;
        this.swingPercent = swingPercent;
        this.avgBalanceL6Mth = avgBalanceL6Mth;
        this.numberOfTransaction = numberOfTransaction;
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

    public BigDecimal getAvgBalanceL6Mth() {
        return avgBalanceL6Mth;
    }

    public void setAvgBalanceL6Mth(BigDecimal avgBalanceL6Mth) {
        this.avgBalanceL6Mth = avgBalanceL6Mth;
    }

    public int getNumberOfTransaction() {
        return numberOfTransaction;
    }

    public void setNumberOfTransaction(int numberOfTransaction) {
        this.numberOfTransaction = numberOfTransaction;
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
                .append("avgBalanceL6Mth", avgBalanceL6Mth)
                .append("numberOfTransaction", numberOfTransaction)
                .append("excludeIncomeFlag", excludeIncomeFlag)
                .toString();
    }
}
