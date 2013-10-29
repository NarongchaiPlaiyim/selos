package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class BusinessTypeLevel implements Serializable {
    private String businessCodeRunningNumber;
    private String businessCode;
    private AttributeTypeLevelBusiness attributeType;

    public BusinessTypeLevel() {
    }

    public BusinessTypeLevel(String businessCodeRunningNumber, String businessCode, AttributeTypeLevelBusiness attributeType) {
        this.businessCodeRunningNumber = businessCodeRunningNumber;
        this.businessCode = businessCode;
        this.attributeType = attributeType;
    }

    public String getBusinessCodeRunningNumber() {
        return businessCodeRunningNumber;
    }

    public void setBusinessCodeRunningNumber(String businessCodeRunningNumber) {
        this.businessCodeRunningNumber = businessCodeRunningNumber;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public AttributeTypeLevelBusiness getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeTypeLevelBusiness attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("businessCodeRunningNumber", businessCodeRunningNumber)
                .append("businessCode", businessCode)
                .append("attributeType", attributeType)
                .toString();
    }
}
