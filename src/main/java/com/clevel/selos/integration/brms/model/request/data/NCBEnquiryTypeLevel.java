package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class NCBEnquiryTypeLevel implements Serializable {
    private int numberOfSearchInLast6Months;
    private AttributeTypeLevelNCBEnquiryType attributeType;

    public NCBEnquiryTypeLevel() {
    }

    public NCBEnquiryTypeLevel(int numberOfSearchInLast6Months, AttributeTypeLevelNCBEnquiryType attributeType) {
        this.numberOfSearchInLast6Months = numberOfSearchInLast6Months;
        this.attributeType = attributeType;
    }

    public int getNumberOfSearchInLast6Months() {
        return numberOfSearchInLast6Months;
    }

    public void setNumberOfSearchInLast6Months(int numberOfSearchInLast6Months) {
        this.numberOfSearchInLast6Months = numberOfSearchInLast6Months;
    }

    public AttributeTypeLevelNCBEnquiryType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeTypeLevelNCBEnquiryType attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("numberOfSearchInLast6Months", numberOfSearchInLast6Months)
                .append("attributeType", attributeType)
                .toString();
    }
}
