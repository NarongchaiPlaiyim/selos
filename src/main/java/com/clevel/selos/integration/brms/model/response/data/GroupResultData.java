package com.clevel.selos.integration.brms.model.response.data;

import com.clevel.selos.integration.brms.model.RuleColorResult;

public class GroupResultData {
    public RuleColorResult color;
    public String deviationFlag;
    public String rejectGroupCode;

    public GroupResultData() {
    }

    public GroupResultData(RuleColorResult color, String deviationFlag, String rejectGroupCode) {
        this.color = color;
        this.deviationFlag = deviationFlag;
        this.rejectGroupCode = rejectGroupCode;
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
}
