package com.clevel.selos.model.view;

import java.math.BigDecimal;
import java.util.List;

public class PrescreenResultView {
    private String interfaceResult;
    private String interfaceReason;
    private BigDecimal groupExposure;
    private BigDecimal groupIncome;
    private ExistingCreditView existingCreditView;

    private List<UWRuleResult> groupRuleResults;
    private List<UWRuleResult> customerRuleResults;

    public String getInterfaceResult() {
        return interfaceResult;
    }

    public void setInterfaceResult(String interfaceResult) {
        this.interfaceResult = interfaceResult;
    }

    public String getInterfaceReason() {
        return interfaceReason;
    }

    public void setInterfaceReason(String interfaceReason) {
        this.interfaceReason = interfaceReason;
    }

    public BigDecimal getGroupExposure() {
        return groupExposure;
    }

    public void setGroupExposure(BigDecimal groupExposure) {
        this.groupExposure = groupExposure;
    }

    public BigDecimal getGroupIncome() {
        return groupIncome;
    }

    public void setGroupIncome(BigDecimal groupIncome) {
        this.groupIncome = groupIncome;
    }

    public ExistingCreditView getExistingCreditView() {
        return existingCreditView;
    }

    public void setExistingCreditView(ExistingCreditView existingCreditView) {
        this.existingCreditView = existingCreditView;
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
