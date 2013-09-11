package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.*;

import java.util.List;

public class PreScreenRequest {
    public ApplicationLevel applicationLevel;
    public List<CustomerLevel> customerLevelList;
    public String bizDescription;

    public PreScreenRequest() {
    }

    public PreScreenRequest(ApplicationLevel applicationLevel, List<CustomerLevel> customerLevelList, String bizDescription) {
        this.applicationLevel = applicationLevel;
        this.customerLevelList = customerLevelList;
        this.bizDescription = bizDescription;
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

    public String getBizDescription() {
        return bizDescription;
    }

    public void setBizDescription(String bizDescription) {
        this.bizDescription = bizDescription;
    }
}