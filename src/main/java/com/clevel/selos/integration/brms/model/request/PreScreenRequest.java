package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data2.ApplicationTypeLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class PreScreenRequest implements Serializable {

    private ApplicationTypeLevel applicationType;

    public PreScreenRequest() {
    }

    public PreScreenRequest(ApplicationTypeLevel applicationType) {
        this.applicationType = applicationType;
    }

    public ApplicationTypeLevel getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationTypeLevel applicationType) {
        this.applicationType = applicationType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("applicationType", applicationType)
                .toString();
    }
}
