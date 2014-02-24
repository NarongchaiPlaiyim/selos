package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.model.ActionResult;

import java.io.Serializable;
import java.util.List;

public class StandardPricingFeeResponse implements Serializable{
    private String appRefNumber;
    private ActionResult actionResult;
    private String reason;
    private List<PricingFee> pricingFeeList;

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public void setAppRefNumber(String appRefNumber) {
        this.appRefNumber = appRefNumber;
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

    public List<PricingFee> getPricingFeeList() {
        return pricingFeeList;
    }

    public void setPricingFeeList(List<PricingFee> pricingFeeList) {
        this.pricingFeeList = pricingFeeList;
    }
}
