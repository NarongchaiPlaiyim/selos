package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.model.ActionResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UWRulesResponse implements Serializable {
    private String decisionID;
    private String applicationNo;
    private ActionResult actionResult;
    private String reason;
    private Map<String, UWRulesResult> uwRulesResultMap;

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

    public Map<String, UWRulesResult> getUwRulesResultMap() {
        return uwRulesResultMap;
    }

    public void setUwRulesResultMap(Map<String, UWRulesResult> uwRulesResultMap) {
        this.uwRulesResultMap = uwRulesResultMap;
    }
}
