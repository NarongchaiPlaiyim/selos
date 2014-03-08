package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.integration.brms.model.RuleColorResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PreScreenResponse {
    public String ruleName;
    public String ruleOrder;
    public String type;
    public String personalId;
    public RuleColorResult color;
    public String deviationFlag;
    public String rejectGroupCode;

    public PreScreenResponse() {
    }

    public PreScreenResponse(String ruleName, String ruleOrder, String type, String personalId, RuleColorResult color, String deviationFlag, String rejectGroupCode) {
        this.ruleName = ruleName;
        this.ruleOrder = ruleOrder;
        this.type = type;
        this.personalId = personalId;
        this.color = color;
        this.deviationFlag = deviationFlag;
        this.rejectGroupCode = rejectGroupCode;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public RuleColorResult getColor() {
        return color;
    }

    public void setColor(RuleColorResult color) {
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
                .append("personalId", personalId)
                .append("color", color)
                .append("deviationFlag", deviationFlag)
                .append("rejectGroupCode", rejectGroupCode)
                .toString();
    }
}
