package com.clevel.selos.integration.corebanking.model.customeraccount;

import com.clevel.selos.model.ActionResult;

import java.io.Serializable;
import java.util.List;

public class CustomerAccountResult implements Serializable {
    private String customerId;
    private ActionResult actionResult;
    private String reason;
    private List<CustomerAccountListModel> accountListModels;

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

    public List<CustomerAccountListModel> getAccountListModels() {
        return accountListModels;
    }

    public void setAccountListModels(List<CustomerAccountListModel> accountListModels) {
        this.accountListModels = accountListModels;
    }
}
