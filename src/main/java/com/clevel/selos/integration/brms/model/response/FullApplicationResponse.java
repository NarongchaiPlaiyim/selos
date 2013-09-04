package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.integration.brms.model.response.data.BorrowerResultData;
import com.clevel.selos.integration.brms.model.response.data.GroupResultData;

import java.util.List;

public class FullApplicationResponse extends PreScreenResponse{
    public FullApplicationResponse() {
        super();
    }

    public FullApplicationResponse(String ruleName, String ruleOrder, boolean groupResult, boolean borrowerResult, GroupResultData groupResultData, List<BorrowerResultData> borrowerResultDataList) {
        super(ruleName, ruleOrder, groupResult, borrowerResult, groupResultData, borrowerResultDataList);
    }
}
