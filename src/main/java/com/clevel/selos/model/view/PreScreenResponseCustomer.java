package com.clevel.selos.model.view;

import com.clevel.selos.integration.brms.model.RuleColorResult;

import java.io.Serializable;

public class PreScreenResponseCustomer implements Serializable {
    private long id;
    private String ruleName;
    private String ruleOrder;
    private String ruleType;
    private RuleColorResult ruleColor;
    private String personalId;
    private String deviationFlag;
    private String rejectGroupCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public RuleColorResult getRuleColor() {
        return ruleColor;
    }

    public void setRuleColor(RuleColorResult ruleColor) {
        this.ruleColor = ruleColor;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getDeviationFlag() {
        return deviationFlag;
    }

    public void setDeviationFlag(String deviationFlag) {
        this.deviationFlag = deviationFlag;
    }

    public String getRejectGroupCode() {
        return rejectGroupCode;
    }

    public void setRejectGroupCode(String rejectGroupCode) {
        this.rejectGroupCode = rejectGroupCode;
    }
}
