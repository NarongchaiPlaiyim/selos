package com.clevel.selos.integration.brms.model.response.data;

import com.clevel.selos.integration.brms.model.RuleColorResult;

public class BorrowerResultData {
    public String personalId;
    public RuleColorResult color;
    public String deviationFlag;
    public String rejectGroupCode;

    public BorrowerResultData() {
    }

    public BorrowerResultData(String personalId, RuleColorResult color, String deviationFlag, String rejectGroupCode) {
        this.personalId = personalId;
        this.color = color;
        this.deviationFlag = deviationFlag;
        this.rejectGroupCode = rejectGroupCode;
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
}
