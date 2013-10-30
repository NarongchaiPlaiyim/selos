package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class AppraisalType implements Serializable {
    private List<AttributeTypeLevelAppraisal> attributeType;

    public AppraisalType() {
    }

    public AppraisalType(List<AttributeTypeLevelAppraisal> attributeType) {
        this.attributeType = attributeType;
    }

    public List<AttributeTypeLevelAppraisal> getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(List<AttributeTypeLevelAppraisal> attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("attributeType", attributeType)
                .toString();
    }
}
