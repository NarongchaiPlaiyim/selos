package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class AppraisalTypeLevel implements Serializable {
    private AttributeTypeLevelAppraisal attributeType;

    public AppraisalTypeLevel() {
    }

    public AppraisalTypeLevel(AttributeTypeLevelAppraisal attributeType) {
        this.attributeType = attributeType;
    }

    public AttributeTypeLevelAppraisal getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeTypeLevelAppraisal attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("attributeType", attributeType)
                .toString();
    }
}
