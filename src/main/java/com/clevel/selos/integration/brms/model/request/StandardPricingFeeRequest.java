package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.ApplicationLevel;

public class StandardPricingFeeRequest {
    public ApplicationLevel applicationLevel;
    //todo add more data level/group (Fac)

    public StandardPricingFeeRequest() {
    }

    public StandardPricingFeeRequest(ApplicationLevel applicationLevel) {
        this.applicationLevel = applicationLevel;
    }

    public ApplicationLevel getApplicationLevel() {
        return applicationLevel;
    }

    public void setApplicationLevel(ApplicationLevel applicationLevel) {
        this.applicationLevel = applicationLevel;
    }
}
