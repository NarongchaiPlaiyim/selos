package com.clevel.selos.integration.rlos.csi.model;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CSIResult implements Serializable {
    private List<CSIData> warningCodeFullMatched;
    private List<CSIData> warningCodePartialMatched;
    private String idNumber;
    private ActionResult actionResult;
    private String resultReason;

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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public String getResultReason() {
        return resultReason;
    }

    public void setResultReason(String resultReason) {
        this.resultReason = resultReason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("warningCodeFullMatched", warningCodeFullMatched)
                .append("warningCodePartialMatched", warningCodePartialMatched)
                .append("idNumber", idNumber)
                .append("actionResult", actionResult)
                .toString();
    }
}
