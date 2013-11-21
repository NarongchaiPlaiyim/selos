package com.clevel.selos.model.view;

import java.math.BigDecimal;
import java.util.Date;

public class ExSumAccountMovementView {

    private BigDecimal odLimit;
    private BigDecimal utilization;
    private BigDecimal swing;
    private int overLimitTimes;
    private int overLimitDays;
    private int chequeReturn;
    private BigDecimal cashFlow;
    private BigDecimal cashFlowLimit;
    private BigDecimal tradeChequeReturnAmount;
    private BigDecimal tradeChequeReturnPercent;

    public ExSumAccountMovementView() {
        reset();
    }

    public void reset() {
        this.odLimit = BigDecimal.ZERO;
        this.utilization = BigDecimal.ZERO;
        this.swing = BigDecimal.ZERO;
        this.overLimitTimes = 0;
        this.overLimitDays = 0;
        this.chequeReturn = 0;
        this.cashFlow = BigDecimal.ZERO;
        this.cashFlowLimit = BigDecimal.ZERO;
        this.tradeChequeReturnAmount = BigDecimal.ZERO;
        this.tradeChequeReturnPercent = BigDecimal.ZERO;
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
}
