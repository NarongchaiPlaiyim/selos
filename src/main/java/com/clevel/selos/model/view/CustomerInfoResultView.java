package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CustomerInfoResultView implements Serializable {
    private String customerId;
    private ActionResult actionResult;
    private String reason;
    private CustomerInfoView customerInfoView;

    public CustomerInfoResultView() {
        //reset();
    }

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

    public CustomerInfoView getCustomerInfoView() {
        return customerInfoView;
    }

    public void setCustomerInfoView(CustomerInfoView customerInfoView) {
        this.customerInfoView = customerInfoView;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("customerId", customerId)
                .append("actionResult", actionResult)
                .append("reason", reason)
                .append("customerInfoView", customerInfoView)
                .toString();
    }
}
