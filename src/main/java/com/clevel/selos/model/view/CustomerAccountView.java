package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerAccountView implements Serializable {
    private String customerId;
    private ActionResult actionResult;
    private String reason;
    private List<String> accountList = new ArrayList<String>();

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

    public List<String> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<String> accountList) {
        this.accountList = accountList;
    }
}
