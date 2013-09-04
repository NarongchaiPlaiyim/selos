package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.ApplicationLevel;

public class DocAppraisalRequest {
    public ApplicationLevel applicationLevel;

    public DocAppraisalRequest() {
    }

    public DocAppraisalRequest(ApplicationLevel applicationLevel) {
        this.applicationLevel = applicationLevel;
    }

    public ApplicationLevel getApplicationLevel() {
        return applicationLevel;
    }

    public void setApplicationLevel(ApplicationLevel applicationLevel) {
        this.applicationLevel = applicationLevel;
    }
}
