package com.clevel.selos.integration.brms.model.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class PricingInterest implements Serializable{

    private String creditDetailId;
    private List<PricingIntTier> pricingIntTierList;

    public String getCreditDetailId() {
        return creditDetailId;
    }

    public void setCreditDetailId(String creditDetailId) {
        this.creditDetailId = creditDetailId;
    }

    public List<PricingIntTier> getPricingIntTierList() {
        return pricingIntTierList;
    }

    public void setPricingIntTierList(List<PricingIntTier> pricingIntTierList) {
        this.pricingIntTierList = pricingIntTierList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("creditDetailId", creditDetailId)
                .append("pricingIntTierList", pricingIntTierList)
                .toString();
    }
}
