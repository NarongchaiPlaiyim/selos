package com.clevel.selos.model.view;

import java.util.List;

public class PrescreenResultView {
    private Double groupExposure;
    private Double groupIncome;
    private List<ExistingCreditDetailView> existingCredit;
    private List<ExistingCreditDetailView> appInRLOS;
    private List<UWRuleResult> groupRuleResults;
    private List<UWRuleResult> customerRuleResults;

    public Double getGroupExposure() {
        return groupExposure;
    }

    public void setGroupExposure(Double groupExposure) {
        this.groupExposure = groupExposure;
    }

    public Double getGroupIncome() {
        return groupIncome;
    }

    public void setGroupIncome(Double groupIncome) {
        this.groupIncome = groupIncome;
    }

    public List<ExistingCreditDetailView> getExistingCredit() {
        return existingCredit;
    }

    public void setExistingCredit(List<ExistingCreditDetailView> existingCredit) {
        this.existingCredit = existingCredit;
    }

    public List<ExistingCreditDetailView> getAppInRLOS() {
        return appInRLOS;
    }

    public void setAppInRLOS(List<ExistingCreditDetailView> appInRLOS) {
        this.appInRLOS = appInRLOS;
    }

    public List<UWRuleResult> getGroupRuleResults() {
        return groupRuleResults;
    }

    public void setGroupRuleResults(List<UWRuleResult> groupRuleResults) {
        this.groupRuleResults = groupRuleResults;
    }

    public List<UWRuleResult> getCustomerRuleResults() {
        return customerRuleResults;
    }

    public void setCustomerRuleResults(List<UWRuleResult> customerRuleResults) {
        this.customerRuleResults = customerRuleResults;
    }
}
