package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.ApplicationLevel;
import com.clevel.selos.integration.brms.model.request.data.BorrowerLevel;

import java.util.List;

public class StandardPricingIntRequest {
    public ApplicationLevel applicationLevel;
    //todo add more data level/group (Acc/Requested, Fac, Head Coll)

    List<BorrowerLevel> customerLevelList;

    public StandardPricingIntRequest() {
    }

    public StandardPricingIntRequest(ApplicationLevel applicationLevel, List<BorrowerLevel> customerLevelList) {
        this.applicationLevel = applicationLevel;
        this.customerLevelList = customerLevelList;
    }

    public ApplicationLevel getApplicationLevel() {
        return applicationLevel;
    }

    public void setApplicationLevel(ApplicationLevel applicationLevel) {
        this.applicationLevel = applicationLevel;
    }

    public List<BorrowerLevel> getCustomerLevelList() {
        return customerLevelList;
    }

    public void setCustomerLevelList(List<BorrowerLevel> customerLevelList) {
        this.customerLevelList = customerLevelList;
    }
}
