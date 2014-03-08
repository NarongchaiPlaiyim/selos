package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.model.ActionResult;

import java.util.List;

public class DocAppraisalResponse {
    private String decisionID;
    private String applicationNo;
    private ActionResult actionResult;
    private String reason;
    private List<DocumentDetail> documentDetailMap;

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

    public List<DocumentDetail> getDocumentDetailMap() {
        return documentDetailMap;
    }

    public void setDocumentDetailMap(List<DocumentDetail> documentDetailMap) {
        this.documentDetailMap = documentDetailMap;
    }
}
