package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class CollateralInfoModel implements Serializable {
    private String type;
    private String description;
    private String appraisal;
    private String collateral;
    private String submittedby;

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getAppraisal() {
        return appraisal;
    }

    public String getCollateral() {
        return collateral;
    }

    public String getSubmittedby() {
        return submittedby;
    }
}
