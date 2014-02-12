package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class WarningCodeFullMatched implements Serializable {
    private String riskCodeWithFullyIdentifyMatched; //todo : to be enum

    public WarningCodeFullMatched() {
    }

    public WarningCodeFullMatched(String riskCodeWithFullyIdentifyMatched) {
        this.riskCodeWithFullyIdentifyMatched = riskCodeWithFullyIdentifyMatched;
    }

    public String getRiskCodeWithFullyIdentifyMatched() {
        return riskCodeWithFullyIdentifyMatched;
    }

    public void setRiskCodeWithFullyIdentifyMatched(String riskCodeWithFullyIdentifyMatched) {
        this.riskCodeWithFullyIdentifyMatched = riskCodeWithFullyIdentifyMatched;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("riskCodeWithFullyIdentifyMatched", riskCodeWithFullyIdentifyMatched)
                .toString();
    }
}
