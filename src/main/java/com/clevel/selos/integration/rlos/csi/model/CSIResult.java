package com.clevel.selos.integration.rlos.csi.model;

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
}
