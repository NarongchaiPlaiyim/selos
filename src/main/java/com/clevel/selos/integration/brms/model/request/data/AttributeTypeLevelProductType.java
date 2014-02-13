package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class AttributeTypeLevelProductType implements Serializable {
    private String maxCreditLimitByCollateral;

    public AttributeTypeLevelProductType() {
    }

    public AttributeTypeLevelProductType(String maxCreditLimitByCollateral) {
        this.maxCreditLimitByCollateral = maxCreditLimitByCollateral;
    }

    public String getMaxCreditLimitByCollateral() {
        return maxCreditLimitByCollateral;
    }

    public void setMaxCreditLimitByCollateral(String maxCreditLimitByCollateral) {
        this.maxCreditLimitByCollateral = maxCreditLimitByCollateral;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("maxCreditLimitByCollateral", maxCreditLimitByCollateral)
                .toString();
    }
}
