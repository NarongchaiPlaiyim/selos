package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Map;

public class MandateDocResponseView implements Serializable {

    private ActionResult actionResult;
    private Map<String, MandateDocView> mandateDocViewMap;
    private String reason;

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public Map<String, MandateDocView> getMandateDocViewMap() {
        return mandateDocViewMap;
    }

    public void setMandateDocViewMap(Map<String, MandateDocView> mandateDocViewMap) {
        this.mandateDocViewMap = mandateDocViewMap;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("actionResult", actionResult)
                .append("mandateDocViewMap", mandateDocViewMap)
                .append("reason", reason)
                .toString();
    }
}
