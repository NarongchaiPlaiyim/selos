package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data2.ApplicationTypeLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class FullApplicationRequest implements Serializable {
//    public ApplicationLevel applicationLevel;
//    public List<BorrowerLevel> customerLevelList;
//    public String bizDescription;
//    //todo add more data level/group  (Acc/Requested, Fac, HeadColl)
//
//    public FullApplicationRequest() {
//    }
//
//    public FullApplicationRequest(ApplicationLevel applicationLevel, List<BorrowerLevel> customerLevelList, String bizDescription) {
//        this.applicationLevel = applicationLevel;
//        this.customerLevelList = customerLevelList;
//        this.bizDescription = bizDescription;
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
//
//    public String getBizDescription() {
//        return bizDescription;
//    }
//
//    public void setBizDescription(String bizDescription) {
//        this.bizDescription = bizDescription;
//    }
    private ApplicationTypeLevel applicationType;

    public FullApplicationRequest() {
    }

    public FullApplicationRequest(ApplicationTypeLevel applicationType) {
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
