package com.clevel.selos.integration.corebanking.model.customeraccount;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;


public class SearchCustomerAccountModel implements Serializable {

    private String reqId;
    private String acronym;
    private String productCode;
    private String serverURL;
    private String sessionId;
    private String custNbr;
    private String radSelectSearch;


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getCustNbr() {
        return custNbr;
    }

    public void setCustNbr(String custNbr) {
        this.custNbr = custNbr;
    }

    public String getRadSelectSearch() {
        return radSelectSearch;
    }

    public void setRadSelectSearch(String radSelectSearch) {
        this.radSelectSearch = radSelectSearch;
    }


    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("reqId", reqId)
                .append("custNbr", custNbr)
                .append("radSelectSearch", radSelectSearch)
                .append("acronym", acronym)
                .append("productCode", productCode)
                .append("serverURL", serverURL)
                .append("sessionId", sessionId)
                .toString();
    }
}
