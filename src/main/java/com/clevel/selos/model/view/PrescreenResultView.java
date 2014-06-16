package com.clevel.selos.model.view;

import com.clevel.selos.model.RelationValue;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrescreenResultView extends PrescreenView {
    private String interfaceResult;
    private String interfaceReason;
    private ExistingCreditFacilityView existingCreditFacilityView;
    private BankStmtSummaryView bankStmtSummaryView;
    private UWRuleResultSummaryView uwRuleResultSummaryView;
    private Map<Integer, UWRuleResultDetailView> groupRuleResults;
    private Map<RelationValue, Integer> numberOfCusRelation;
    private Map<String, CustomerInfoSimpleView> customerInfoSimpleViewMap;
    private Map<Integer, PrescreenCusRulesGroupView> prescreenCusRulesGroupViewMap;

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

    public List<Map.Entry<Integer, UWRuleResultDetailView>> getGroupResultsList(){
        Set<Map.Entry<Integer, UWRuleResultDetailView>> groupRuleSet =
                groupRuleResults.entrySet();
        return new ArrayList<Map.Entry<Integer, UWRuleResultDetailView>>(groupRuleSet);
    }

    public List<Map.Entry<RelationValue, Integer>> getNumberOfCusRelation() {
        Set<Map.Entry<RelationValue, Integer>> numberOfCusRelationEntry = numberOfCusRelation.entrySet();
        return new ArrayList<Map.Entry<RelationValue, Integer>>(numberOfCusRelationEntry);
    }

    public void setNumberOfCusRelation(Map<RelationValue, Integer> numberOfCusRelation) {
        this.numberOfCusRelation = numberOfCusRelation;
    }

    public List<Map.Entry<String, CustomerInfoSimpleView>> getCustomerInfoSimpleViewMap() {
        Set<Map.Entry<String, CustomerInfoSimpleView>> customerInfoSimpleViewEntry = customerInfoSimpleViewMap.entrySet();
        return new ArrayList<Map.Entry<String, CustomerInfoSimpleView>>(customerInfoSimpleViewEntry);
    }

    public void setCustomerInfoSimpleViewMap(Map<String, CustomerInfoSimpleView> customerInfoSimpleViewMap) {
        this.customerInfoSimpleViewMap = customerInfoSimpleViewMap;
    }

    public Map<Integer, PrescreenCusRulesGroupView> getPrescreenCusRulesGroupViewMap() {
        return prescreenCusRulesGroupViewMap;
    }

    public void setPrescreenCusRulesGroupViewMap(Map<Integer, PrescreenCusRulesGroupView> prescreenCusRulesGroupViewMap) {
        this.prescreenCusRulesGroupViewMap = prescreenCusRulesGroupViewMap;
    }

    public List<Map.Entry<Integer, PrescreenCusRulesGroupView>> getPrescreenCusRulesGroupViewMapList(){
        Set<Map.Entry<Integer, PrescreenCusRulesGroupView>> entry = prescreenCusRulesGroupViewMap.entrySet();
        return new ArrayList<Map.Entry<Integer, PrescreenCusRulesGroupView>>(entry);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("interfaceResult", interfaceResult)
                .append("interfaceReason", interfaceReason)
                .append("existingCreditFacilityView", existingCreditFacilityView)
                .append("bankStmtSummaryView", bankStmtSummaryView)
                .append("uwRuleResultSummaryView", uwRuleResultSummaryView)
                .append("groupRuleResults", groupRuleResults)
                .append("numberOfCusRelation", numberOfCusRelation)
                .append("customerInfoSimpleViewMap", customerInfoSimpleViewMap)
                .toString();
    }
}
