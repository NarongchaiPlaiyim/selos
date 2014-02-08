package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExSumCollateralView implements Serializable {

    private BigDecimal cashCollateralValue;
    private BigDecimal coreAssetValue;
    private BigDecimal noneCoreAssetValue;
    private BigDecimal limitApprove;
    private BigDecimal percentLTV;

    public ExSumCollateralView() {
        reset();
    }

    public void reset() {
        this.cashCollateralValue = BigDecimal.ZERO;
        this.coreAssetValue = BigDecimal.ZERO;
        this.noneCoreAssetValue = BigDecimal.ZERO;
        this.limitApprove = BigDecimal.ZERO;
        this.percentLTV = BigDecimal.ZERO;
    }

    public BigDecimal getCashCollateralValue() {
        return cashCollateralValue;
    }

    public void setCashCollateralValue(BigDecimal cashCollateralValue) {
        this.cashCollateralValue = cashCollateralValue;
    }

    public BigDecimal getCoreAssetValue() {
        return coreAssetValue;
    }

    public void setCoreAssetValue(BigDecimal coreAssetValue) {
        this.coreAssetValue = coreAssetValue;
    }

    public BigDecimal getNoneCoreAssetValue() {
        return noneCoreAssetValue;
    }

    public void setNoneCoreAssetValue(BigDecimal noneCoreAssetValue) {
        this.noneCoreAssetValue = noneCoreAssetValue;
    }

    public BigDecimal getLimitApprove() {
        return limitApprove;
    }

    public void setLimitApprove(BigDecimal limitApprove) {
        this.limitApprove = limitApprove;
    }

    public BigDecimal getPercentLTV() {
        return percentLTV;
    }

    public void setPercentLTV(BigDecimal percentLTV) {
        this.percentLTV = percentLTV;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("cashCollateralValue", cashCollateralValue).
                append("coreAssetValue", coreAssetValue).
                append("noneCoreAssetValue", noneCoreAssetValue).
                append("limitApprove", limitApprove).
                append("percentLTV", percentLTV).
                toString();
    }
}
