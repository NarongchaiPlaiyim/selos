package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.ApplicationLevel;
import com.clevel.selos.integration.brms.model.request.data.CustomerLevel;

import java.util.List;

public class StandardPricingIntRequest {
    public ApplicationLevel applicationLevel;
    //todo add more data level/group (Acc/Requested, Fac, Head Coll)

    List<CustomerLevel> customerLevelList;

    public StandardPricingIntRequest() {
    }

    public StandardPricingIntRequest(ApplicationLevel applicationLevel, List<CustomerLevel> customerLevelList) {
        this.applicationLevel = applicationLevel;
        this.customerLevelList = customerLevelList;
    }

    public ApplicationLevel getApplicationLevel() {
        return applicationLevel;
    }

    public void setApplicationLevel(ApplicationLevel applicationLevel) {
        this.applicationLevel = applicationLevel;
    }

    public List<CustomerLevel> getCustomerLevelList() {
        return customerLevelList;
    }

    public void setCustomerLevelList(List<CustomerLevel> customerLevelList) {
        this.customerLevelList = customerLevelList;
    }
}
