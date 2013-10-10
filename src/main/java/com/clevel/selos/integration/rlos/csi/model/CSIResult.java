package com.clevel.selos.integration.rlos.csi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CSIResult implements Serializable {
    private List<CSIData> warningCodeFullMatched;
    private List<CSIData> warningCodePartialMatched;

    public List<CSIData> getWarningCodeFullMatched() {
        return warningCodeFullMatched;
    }

    public void setWarningCodeFullMatched(List<CSIData> warningCodeFullMatched) {
        this.warningCodeFullMatched = warningCodeFullMatched;
    }

    public List<CSIData> getWarningCodePartialMatched() {
        return warningCodePartialMatched;
    }

    public void setWarningCodePartialMatched(List<CSIData> warningCodePartialMatched) {
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
