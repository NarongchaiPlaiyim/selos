package com.clevel.selos.integration.corebanking.model.corporateInfo;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CorporateResult {
    private String customerId;
    private ActionResult actionResult;
    private String reason;
    private CorporateModel corporateModel;

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

    public CorporateModel getCorporateModel() {
        return corporateModel;
    }

    public void setCorporateModel(CorporateModel corporateModel) {
        this.corporateModel = corporateModel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("customerId", customerId)
                .append("actionResult", actionResult)
                .append("reason", reason)
                .append("corporateModel", corporateModel)
                .toString();
    }
}
