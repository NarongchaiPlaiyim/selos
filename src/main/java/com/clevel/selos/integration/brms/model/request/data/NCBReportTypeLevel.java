package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class NCBReportTypeLevel implements Serializable {
    //Attribute
    private boolean ncbNPLFlag;

    private List<AccountTypeLevelNCBReport> ncbAccountType;
    private List<NCBEnquiryTypeLevel> ncbEnquiryTypes;
    public NCBReportTypeLevel() {
    }

    public NCBReportTypeLevel(boolean ncbNPLFlag, List<AccountTypeLevelNCBReport> ncbAccountType, List<NCBEnquiryTypeLevel> ncbEnquiryTypes) {
        this.ncbNPLFlag = ncbNPLFlag;
        this.ncbAccountType = ncbAccountType;
        this.ncbEnquiryTypes = ncbEnquiryTypes;
    }

    public boolean isNcbNPLFlag() {
        return ncbNPLFlag;
    }

    public void setNcbNPLFlag(boolean ncbNPLFlag) {
        this.ncbNPLFlag = ncbNPLFlag;
    }

    public List<AccountTypeLevelNCBReport> getNcbAccountType() {
        return ncbAccountType;
    }

    public void setNcbAccountType(List<AccountTypeLevelNCBReport> ncbAccountType) {
        this.ncbAccountType = ncbAccountType;
    }

    public List<NCBEnquiryTypeLevel> getNcbEnquiryTypes() {
        return ncbEnquiryTypes;
    }

    public void setNcbEnquiryTypes(List<NCBEnquiryTypeLevel> ncbEnquiryTypes) {
        this.ncbEnquiryTypes = ncbEnquiryTypes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ncbNPLFlag", ncbNPLFlag)
                .append("ncbAccountType", ncbAccountType)
                .append("ncbEnquiryTypes", ncbEnquiryTypes)
                .toString();
    }
}
