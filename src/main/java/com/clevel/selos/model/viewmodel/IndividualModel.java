package com.clevel.selos.model.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;


public class IndividualModel implements Serializable {
     private String reqId;
     private String resCode;
     private String resDesc;
     private String searchResult;
     private String lastPageFlag;

    //personal Detail Section

    private String title;
    private String custId;
    private String telephone1;

    //personal list Section
    private ArrayList<IndividualPersonalList>personalLists;


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

    public ArrayList<IndividualPersonalList> getPersonalLists() {
        return personalLists;
    }

    public void setPersonalLists(ArrayList<IndividualPersonalList> personalLists) {
        this.personalLists = personalLists;
    }

}
