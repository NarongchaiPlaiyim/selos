package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data2.ApplicationTypeLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class StandardPricingFeeRequest implements Serializable {
//    public ApplicationLevel applicationLevel;
//    //todo add more data level/group (Fac)
//
//    List<BorrowerLevel> customerLevelList;
//
//    public StandardPricingFeeRequest() {
//    }
//
//    public StandardPricingFeeRequest(ApplicationLevel applicationLevel, List<BorrowerLevel> customerLevelList) {
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
//
//    @Override
//    public String toString() {
//        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
//                .append("applicationLevel", applicationLevel)
//                .append("customerLevelList", customerLevelList)
//                .toString();
//    }
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
