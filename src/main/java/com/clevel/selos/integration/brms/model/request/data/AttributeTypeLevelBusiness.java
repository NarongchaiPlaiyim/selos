package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class AttributeTypeLevelBusiness implements Serializable {
    private boolean negativeFlag;
    private boolean highRiskFlag;

    public AttributeTypeLevelBusiness() {
    }

    public AttributeTypeLevelBusiness(boolean negativeFlag, boolean highRiskFlag) {
        this.negativeFlag = negativeFlag;
        this.highRiskFlag = highRiskFlag;
    }

    public boolean isNegativeFlag() {
        return negativeFlag;
    }

    public void setNegativeFlag(boolean negativeFlag) {
        this.negativeFlag = negativeFlag;
    }

    public boolean isHighRiskFlag() {
        return highRiskFlag;
    }

    public void setHighRiskFlag(boolean highRiskFlag) {
        this.highRiskFlag = highRiskFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("highRiskFlag", highRiskFlag)
                .append("negativeFlag", negativeFlag)
                .toString();
    }
}
