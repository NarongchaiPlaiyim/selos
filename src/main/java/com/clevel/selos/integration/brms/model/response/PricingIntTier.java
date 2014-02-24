package com.clevel.selos.integration.brms.model.response;

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
}
