package com.clevel.selos.integration.corebanking.model.individualInfo;

import com.clevel.selos.model.ActionResult;

public class IndividualResult {
    private String customerId;
    private ActionResult actionResult;
    private String reason;
    private IndividualModel individualModel;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public IndividualModel getIndividualModel() {
        return individualModel;
    }

    public void setIndividualModel(IndividualModel individualModel) {
        this.individualModel = individualModel;
    }
}
