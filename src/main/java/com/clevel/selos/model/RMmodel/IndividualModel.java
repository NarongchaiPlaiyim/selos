package com.clevel.selos.model.RMmodel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class IndividualModel implements Serializable {
     private String reqId;
     private String resCode;
     private String resDesc;
     private String searchResult;
     private String lastPageFlag;

    //personal Detail Section

    private String title;
    private String custId;
    private String name;
    private String telephone1;

    //personal list Section
    private List<IndividualPersonalList> personalLists;


    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    public String getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }

    public String getLastPageFlag() {
        return lastPageFlag;
    }

    public void setLastPageFlag(String lastPageFlag) {
        this.lastPageFlag = lastPageFlag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public List<IndividualPersonalList> getPersonalLists() {
        return personalLists;
    }

    public void setPersonalLists(List<IndividualPersonalList> personalLists) {
        this.personalLists = personalLists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("reqId", reqId)
                .append("resCode", resCode)
                .append("resDesc", resDesc)
                .append("searchResult", searchResult)
                .append("lastPageFlag", lastPageFlag)
                .append("title", title)
                .append("custId", custId)
                .append("name", name)
                .append("telephone1", telephone1)
                .append("personalLists", personalLists)
                .toString();
    }
}
