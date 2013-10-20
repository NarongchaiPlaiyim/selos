package com.clevel.selos.integration.dwh.bankstatement.model;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class DWHBankStatementResult implements Serializable {
    private ActionResult actionResult;
    private String reason;
    private List<DWHBankStatement> bankStatementList;

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

    public List<DWHBankStatement> getBankStatementList() {
        return bankStatementList;
    }

    public void setBankStatementList(List<DWHBankStatement> bankStatementList) {
        this.bankStatementList = bankStatementList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("actionResult", actionResult)
                .append("reason", reason)
                .append("bankStatementList", bankStatementList)
                .toString();
    }
}
