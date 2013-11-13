package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.ApplicationLevel;
import com.clevel.selos.integration.brms.model.request.data.BorrowerLevel;
import com.clevel.selos.integration.brms.model.request.data.TmbAccountLevel;
import com.clevel.selos.integration.brms.model.request.data2.ApplicationTypeLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class PreScreenRequest implements Serializable {
//    private ApplicationLevel applicationLevel;
//    private List<BorrowerLevel> customerLevelList;
//    private String bizDescription;
//    private TmbAccountLevel tmbAccountLevel;
//
//    public PreScreenRequest() {
//    }
//
//    public PreScreenRequest(ApplicationLevel applicationLevel, List<BorrowerLevel> customerLevelList, String bizDescription, TmbAccountLevel tmbAccountLevel) {
//        this.applicationLevel = applicationLevel;
//        this.customerLevelList = customerLevelList;
//        this.bizDescription = bizDescription;
//        this.tmbAccountLevel = tmbAccountLevel;
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
//
//    public TmbAccountLevel getTmbAccountLevel() {
//        return tmbAccountLevel;
//    }
//
//    public void setTmbAccountLevel(TmbAccountLevel tmbAccountLevel) {
//        this.tmbAccountLevel = tmbAccountLevel;
//    }
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
