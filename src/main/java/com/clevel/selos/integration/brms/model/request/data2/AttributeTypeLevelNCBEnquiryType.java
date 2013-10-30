package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class AttributeTypeLevelNCBEnquiryType implements Serializable {
    private BigDecimal numberOfDaysNCBCheck;
    public AttributeTypeLevelNCBEnquiryType() {
    }

    public AttributeTypeLevelNCBEnquiryType(BigDecimal numberOfDaysNCBCheck) {
        this.numberOfDaysNCBCheck = numberOfDaysNCBCheck;
    }

    public BigDecimal getNumberOfDaysNCBCheck() {
        return numberOfDaysNCBCheck;
    }

    public void setNumberOfDaysNCBCheck(BigDecimal numberOfDaysNCBCheck) {
        this.numberOfDaysNCBCheck = numberOfDaysNCBCheck;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("numberOfDaysNCBCheck", numberOfDaysNCBCheck)
                .toString();
    }
}
