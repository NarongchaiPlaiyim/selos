package com.clevel.selos.integration.brms.model.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class UWRulesResult implements Serializable{
    private String ruleName;
    private String ruleOrder;
    private String type;
    private String personalID;
    private String color;
    private String deviationFlag;
    private String rejectGroupCode;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ruleName", ruleName)
                .append("ruleOrder", ruleOrder)
                .append("type", type)
                .append("personalID", personalID)
                .append("color", color)
                .append("deviationFlag", deviationFlag)
                .append("rejectGroupCode", rejectGroupCode)
                .toString();
    }
}
