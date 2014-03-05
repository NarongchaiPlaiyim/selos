package com.clevel.selos.integration.brms.model.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class PricingIntTier implements Serializable{

    private String rateType;
    private String spread;
    private BigDecimal rateVariance;
    private BigDecimal maxRateVariance;

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getSpread() {
        return spread;
    }

    public void setSpread(String spread) {
        this.spread = spread;
    }

    public BigDecimal getRateVariance() {
        return rateVariance;
    }

    public void setRateVariance(BigDecimal rateVariance) {
        this.rateVariance = rateVariance;
    }

    public BigDecimal getMaxRateVariance() {
        return maxRateVariance;
    }

    public void setMaxRateVariance(BigDecimal maxRateVariance) {
        this.maxRateVariance = maxRateVariance;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("rateType", rateType)
                .append("spread", spread)
                .append("rateVariance", rateVariance)
                .append("maxRateVariance", maxRateVariance)
                .toString();
    }
}
