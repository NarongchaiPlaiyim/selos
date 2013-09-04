package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.integration.brms.model.response.data.BorrowerResultData;
import com.clevel.selos.integration.brms.model.response.data.GroupResultData;

import java.util.List;

public class PreScreenResponse {
    public String ruleName;
    public String ruleOrder;
    public boolean groupResult;
    public boolean borrowerResult;
    public GroupResultData groupResultData; //Null if groupResult = false
    public List<BorrowerResultData> borrowerResultDataList; //Null if borrowerResult = false;

    public PreScreenResponse() {
    }

    public PreScreenResponse(String ruleName, String ruleOrder, boolean groupResult, boolean borrowerResult, GroupResultData groupResultData, List<BorrowerResultData> borrowerResultDataList) {
        this.ruleName = ruleName;
        this.ruleOrder = ruleOrder;
        this.groupResult = groupResult;
        this.borrowerResult = borrowerResult;
        this.groupResultData = groupResultData;
        this.borrowerResultDataList = borrowerResultDataList;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleOrder() {
        return ruleOrder;
    }

    public void setRuleOrder(String ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    public boolean isGroupResult() {
        return groupResult;
    }

    public void setGroupResult(boolean groupResult) {
        this.groupResult = groupResult;
    }

    public boolean isBorrowerResult() {
        return borrowerResult;
    }

    public void setBorrowerResult(boolean borrowerResult) {
        this.borrowerResult = borrowerResult;
    }

    public GroupResultData getGroupResultData() {
        return groupResultData;
    }

    public void setGroupResultData(GroupResultData groupResultData) {
        this.groupResultData = groupResultData;
    }

    public List<BorrowerResultData> getBorrowerResultDataList() {
        return borrowerResultDataList;
    }

    public void setBorrowerResultDataList(List<BorrowerResultData> borrowerResultDataList) {
        this.borrowerResultDataList = borrowerResultDataList;
    }
}
