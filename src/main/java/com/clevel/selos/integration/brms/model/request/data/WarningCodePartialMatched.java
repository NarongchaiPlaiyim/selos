package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class WarningCodePartialMatched implements Serializable {
    private String riskCodeWithSomeIdentifyMatched;

    public WarningCodePartialMatched() {
    }

    public WarningCodePartialMatched(String riskCodeWithSomeIdentifyMatched) {
        this.riskCodeWithSomeIdentifyMatched = riskCodeWithSomeIdentifyMatched;
    }

    public String getRiskCodeWithSomeIdentifyMatched() {
        return riskCodeWithSomeIdentifyMatched;
    }

    public void setRiskCodeWithSomeIdentifyMatched(String riskCodeWithSomeIdentifyMatched) {
        this.riskCodeWithSomeIdentifyMatched = riskCodeWithSomeIdentifyMatched;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("riskCodeWithSomeIdentifyMatched", riskCodeWithSomeIdentifyMatched)
                .toString();
    }
}
