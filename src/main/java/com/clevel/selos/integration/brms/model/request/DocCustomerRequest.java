package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data2.ApplicationTypeLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class DocCustomerRequest implements Serializable {
//    public ApplicationLevel applicationLevel;
//    public List<BorrowerLevel> customerLevelList;
//    //todo add more data level/group (Acc/Requested)
//
//    public DocCustomerRequest() {
//    }
//
//    public DocCustomerRequest(ApplicationLevel applicationLevel, List<BorrowerLevel> customerLevelList) {
//        this.applicationLevel = applicationLevel;
//        this.customerLevelList = customerLevelList;
//    }
//
//    public ApplicationLevel getApplicationLevel() {
//        return applicationLevel;
//    }
//
//    public void setApplicationLevel(ApplicationLevel applicationLevel) {
//        this.applicationLevel = applicationLevel;
//    }
//
//    public List<BorrowerLevel> getCustomerLevelList() {
//        return customerLevelList;
//    }
//
//    public void setCustomerLevelList(List<BorrowerLevel> customerLevelList) {
//        this.customerLevelList = customerLevelList;
//    }
    private ApplicationTypeLevel applicationType;

    public DocCustomerRequest() {
    }

    public DocCustomerRequest(ApplicationTypeLevel applicationType) {
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
