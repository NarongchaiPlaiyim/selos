package com.clevel.selos.integration.brms.model.request;

import com.clevel.selos.integration.brms.model.request.data.ApplicationLevel;
import com.clevel.selos.integration.brms.model.request.data.BorrowerLevel;
import com.clevel.selos.integration.brms.model.request.data.TmbAccountLevel;

import java.util.List;

public class PreScreenRequest {
    private ApplicationLevel applicationLevel;
    private List<BorrowerLevel> customerLevelList;
    private String bizDescription;
    private TmbAccountLevel tmbAccountLevel;

    public PreScreenRequest() {
    }

    public PreScreenRequest(ApplicationLevel applicationLevel, List<BorrowerLevel> customerLevelList, String bizDescription, TmbAccountLevel tmbAccountLevel) {
        this.applicationLevel = applicationLevel;
        this.customerLevelList = customerLevelList;
        this.bizDescription = bizDescription;
        this.tmbAccountLevel = tmbAccountLevel;
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

    public TmbAccountLevel getTmbAccountLevel() {
        return tmbAccountLevel;
    }

    public void setTmbAccountLevel(TmbAccountLevel tmbAccountLevel) {
        this.tmbAccountLevel = tmbAccountLevel;
    }
}
