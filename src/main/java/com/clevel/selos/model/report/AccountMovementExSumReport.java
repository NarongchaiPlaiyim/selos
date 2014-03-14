package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

    public class AccountMovementExSumReport extends ReportModel{
    private BigDecimal odLimit;
    private BigDecimal utilization;
    private BigDecimal swing;
    private BigDecimal overLimitTimes;
    private BigDecimal overLimitDays;
    private BigDecimal chequeReturn;
    private BigDecimal cashFlow;
    private BigDecimal cashFlowLimit;
    private BigDecimal tradeChequeReturnAmount;
    private BigDecimal tradeChequeReturnPercent;

    public AccountMovementExSumReport() {
        this.odLimit = getDefaultBigDecimal();
        this.utilization = getDefaultBigDecimal();
        this.swing = getDefaultBigDecimal();
        this.overLimitTimes = getDefaultBigDecimal();
        this.overLimitDays = getDefaultBigDecimal();
        this.chequeReturn = getDefaultBigDecimal();
        this.cashFlow = getDefaultBigDecimal();
        this.cashFlowLimit = getDefaultBigDecimal();
        this.tradeChequeReturnAmount = getDefaultBigDecimal();
        this.tradeChequeReturnPercent = getDefaultBigDecimal();
    }

    public BigDecimal getOdLimit() {
        return odLimit;
    }

    public void setOdLimit(BigDecimal odLimit) {
        this.odLimit = odLimit;
    }

    public BigDecimal getUtilization() {
        return utilization;
    }

    public void setUtilization(BigDecimal utilization) {
        this.utilization = utilization;
    }

    public BigDecimal getSwing() {
        return swing;
    }

    public void setSwing(BigDecimal swing) {
        this.swing = swing;
    }

    public BigDecimal getOverLimitTimes() {
        return overLimitTimes;
    }

    public void setOverLimitTimes(BigDecimal overLimitTimes) {
        this.overLimitTimes = overLimitTimes;
    }

    public BigDecimal getOverLimitDays() {
        return overLimitDays;
    }

    public void setOverLimitDays(BigDecimal overLimitDays) {
        this.overLimitDays = overLimitDays;
    }

    public BigDecimal getChequeReturn() {
        return chequeReturn;
    }

    public void setChequeReturn(BigDecimal chequeReturn) {
        this.chequeReturn = chequeReturn;
    }

    public BigDecimal getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(BigDecimal cashFlow) {
        this.cashFlow = cashFlow;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("odLimit", odLimit).
                append("utilization", utilization).
                append("swing", swing).
                append("overLimitTimes", overLimitTimes).
                append("overLimitDays", overLimitDays).
                append("chequeReturn", chequeReturn).
                append("cashFlow", cashFlow).
                append("cashFlowLimit", cashFlowLimit).
                append("tradeChequeReturnAmount", tradeChequeReturnAmount).
                append("tradeChequeReturnPercent", tradeChequeReturnPercent).
                toString();
    }
}
