package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Map;

public class PrescreenResultView extends PrescreenView {
    private String interfaceResult;
    private String interfaceReason;
    private ExistingCreditFacilityView existingCreditFacilityView;
    private BankStmtSummaryView bankStmtSummaryView;


    private UWRuleResultSummaryView uwRuleResultSummaryView;
    private Map<Integer, UWRuleResultDetailView> groupRuleResults;
    private Map<Integer, UWRuleResultDetailView> customerRuleResults;

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

    public BankStmtSummaryView getBankStmtSummaryView() {
        return bankStmtSummaryView;
    }

    public void setBankStmtSummaryView(BankStmtSummaryView bankStmtSummaryView) {
        this.bankStmtSummaryView = bankStmtSummaryView;
    }

    public UWRuleResultSummaryView getUwRuleResultSummaryView() {
        return uwRuleResultSummaryView;
    }

    public void setUwRuleResultSummaryView(UWRuleResultSummaryView uwRuleResultSummaryView) {
        this.uwRuleResultSummaryView = uwRuleResultSummaryView;
    }

    public Map<Integer, UWRuleResultDetailView> getGroupRuleResults() {
        return groupRuleResults;
    }

    public void setGroupRuleResults(Map<Integer, UWRuleResultDetailView> groupRuleResults) {
        this.groupRuleResults = groupRuleResults;
    }

    public Map<Integer, UWRuleResultDetailView> getCustomerRuleResults() {
        return customerRuleResults;
    }

    public void setCustomerRuleResults(Map<Integer, UWRuleResultDetailView> customerRuleResults) {
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
