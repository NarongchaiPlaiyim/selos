package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.ApplicationLevel;

public class StandardPricingIntRequest {
    public ApplicationLevel applicationLevel;
    //todo add more data level/group (Acc/Requested, Fac, Head Coll)

    public StandardPricingIntRequest() {
    }

    public StandardPricingIntRequest(ApplicationLevel applicationLevel) {
        this.applicationLevel = applicationLevel;
    }

    public ApplicationLevel getApplicationLevel() {
        return applicationLevel;
    }

    public void setApplicationLevel(ApplicationLevel applicationLevel) {
        this.applicationLevel = applicationLevel;
    }
}
