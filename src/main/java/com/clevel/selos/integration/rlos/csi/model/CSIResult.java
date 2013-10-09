package com.clevel.selos.integration.rlos.csi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CSIResult implements Serializable {
    private List<String> warningCodeFullMatched;
    private List<String> warningCodePartialMatched;

    public List<String> getWarningCodeFullMatched() {
        return warningCodeFullMatched;
    }

    public void setWarningCodeFullMatched(List<String> warningCodeFullMatched) {
        this.warningCodeFullMatched = warningCodeFullMatched;
    }

    public List<String> getWarningCodePartialMatched() {
        return warningCodePartialMatched;
    }

    public void setWarningCodePartialMatched(List<String> warningCodePartialMatched) {
        this.warningCodePartialMatched = warningCodePartialMatched;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("warningCodeFullMatched", warningCodeFullMatched)
                .append("warningCodePartialMatched", warningCodePartialMatched)
                .toString();
    }
}
