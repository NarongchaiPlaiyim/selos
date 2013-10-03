package com.clevel.selos.integration.corebanking.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class SearchIndividual implements Serializable {
    private String reqId;
    private String custType;
    private String type;
    private String custId;
    private String custNbr;
    private String custName;
    private String custSurname;
    private String radSelectSearch;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustNbr() {
        return custNbr;
    }

    public void setCustNbr(String custNbr) {
        this.custNbr = custNbr;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustSurname() {
        return custSurname;
    }

    public void setCustSurname(String custSurname) {
        this.custSurname = custSurname;
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
                .append("custType", custType)
                .append("type", type)
                .append("custId", custId)
                .append("custNbr", custNbr)
                .append("custName", custName)
                .append("custSurname", custSurname)
                .append("radSelectSearch", radSelectSearch)
                .toString();
    }
}
