package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class PricingDOAView implements Serializable {
    private BigDecimal standardPrice;
    private BigDecimal suggestPrice;
    private BigDecimal finalPrice;

    public PricingDOAView(){

    }

    public BigDecimal getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(BigDecimal standardPrice) {
        this.standardPrice = standardPrice;
    }

    public BigDecimal getSuggestPrice() {
        return suggestPrice;
    }

    public void setSuggestPrice(BigDecimal suggestPrice) {
        this.suggestPrice = suggestPrice;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }
}
