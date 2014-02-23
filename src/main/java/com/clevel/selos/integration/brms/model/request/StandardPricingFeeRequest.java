package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.ApplicationTypeLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class StandardPricingFeeRequest implements Serializable {

    private ApplicationTypeLevel applicationType;

    public StandardPricingFeeRequest() {
    }

    public StandardPricingFeeRequest(ApplicationTypeLevel applicationType) {
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
