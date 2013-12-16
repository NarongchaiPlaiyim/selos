package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class PrescreenResultView extends PrescreenView {
    private String interfaceResult;
    private String interfaceReason;
    private ExistingCreditFacilityView existingCreditFacilityView;
    private BankStmtSummaryView bankStmtSummaryView;

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

    public ExistingCreditFacilityView getExistingCreditFacilityView() {
        return existingCreditFacilityView;
    }

    public void setExistingCreditFacilityView(ExistingCreditFacilityView existingCreditFacilityView) {
        this.existingCreditFacilityView = existingCreditFacilityView;
    }

    public List<UWRuleResult> getGroupRuleResults() {
        return groupRuleResults;
    }

    public BankStmtSummaryView getBankStmtSummaryView() {
        return bankStmtSummaryView;
    }

    public void setBankStmtSummaryView(BankStmtSummaryView bankStmtSummaryView) {
        this.bankStmtSummaryView = bankStmtSummaryView;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("interfaceResult", interfaceResult)
                .append("interfaceReason", interfaceReason)
                .append("existingCreditFacilityView", existingCreditFacilityView)
                .append("bankStmtSummaryView", bankStmtSummaryView)
                .append("groupRuleResults", groupRuleResults)
                .append("customerRuleResults", customerRuleResults)
                .toString();
    }
}
