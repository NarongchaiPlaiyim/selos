package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class DocCustomerResponse {
    private String decisionID;
    private String applicationNo;
    private ActionResult actionResult;
    private String reason;
    private List<DocumentDetail> documentDetailList;

    public String getDecisionID() {
        return decisionID;
    }

    public void setDecisionID(String decisionID) {
        this.decisionID = decisionID;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
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

    public List<DocumentDetail> getDocumentDetailList() {
        return documentDetailList;
    }

    public void setDocumentDetailList(List<DocumentDetail> documentDetailList) {
        this.documentDetailList = documentDetailList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("decisionID", decisionID)
                .append("applicationNo", applicationNo)
                .append("actionResult", actionResult)
                .append("reason", reason)
                .append("documentDetailList", documentDetailList)
                .toString();
    }
}
