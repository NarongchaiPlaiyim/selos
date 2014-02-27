package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.model.ActionResult;

import java.io.Serializable;
import java.util.List;

public class StandardPricingResponse implements Serializable {
    private String decisionID;
    private String applicationNo;
    private ActionResult actionResult;
    private String reason;
    private List<PricingInterest> pricingInterest;
    private List<PricingFee> pricingFeeList;

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

    public List<PricingInterest> getPricingInterest() {
        return pricingInterest;
    }

    public void setPricingInterest(List<PricingInterest> pricingInterest) {
        this.pricingInterest = pricingInterest;
    }

    public List<PricingFee> getPricingFeeList() {
        return pricingFeeList;
    }

    public void setPricingFeeList(List<PricingFee> pricingFeeList) {
        this.pricingFeeList = pricingFeeList;
    }
}
