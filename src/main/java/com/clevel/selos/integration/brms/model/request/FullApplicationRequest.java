package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.ApplicationLevel;
import com.clevel.selos.integration.brms.model.request.data.BorrowerLevel;

import java.util.List;

public class FullApplicationRequest {
    public ApplicationLevel applicationLevel;
    public List<BorrowerLevel> customerLevelList;
    public String bizDescription;
    //todo add more data level/group  (Acc/Requested, Fac, HeadColl)

    public FullApplicationRequest() {
    }

    public FullApplicationRequest(ApplicationLevel applicationLevel, List<BorrowerLevel> customerLevelList, String bizDescription) {
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

    public List<BorrowerLevel> getCustomerLevelList() {
        return customerLevelList;
    }

    public void setCustomerLevelList(List<BorrowerLevel> customerLevelList) {
        this.customerLevelList = customerLevelList;
    }

    public String getBizDescription() {
        return bizDescription;
    }

    public void setBizDescription(String bizDescription) {
        this.bizDescription = bizDescription;
    }
}
