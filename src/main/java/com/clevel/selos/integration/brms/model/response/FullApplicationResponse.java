package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.integration.brms.model.RuleColorResult;

public class FullApplicationResponse extends PreScreenResponse{
    public FullApplicationResponse() {
        super();
    }

    public FullApplicationResponse(String ruleName, String ruleOrder, String type, String personalId, RuleColorResult color, String deviationFlag, String rejectGroupCode) {
        super(ruleName, ruleOrder, type, personalId, color, deviationFlag, rejectGroupCode);
    }
}
