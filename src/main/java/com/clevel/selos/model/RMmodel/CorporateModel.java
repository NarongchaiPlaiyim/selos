package com.clevel.selos.model.RMmodel;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sahawat
 * Date: 29/8/2556
 * Time: 20:20 น.
 * To change this template use File | Settings | File Templates.
 */
public class CorporateModel {

    private String reqId;
    private String resCode;
    private String resDesc;

    private String searchResult;

    //Personal Detail Section
    private String custNbr;
    private String title;
    private String thaiName1;
    private String cId;
    private String citizenId;
    private String estDate;

    private ArrayList<CorporatePersonalList> PersonalList;

    public ArrayList<CorporatePersonalList> getPersonalList() {
        return PersonalList;
    }

    public void setPersonalList(ArrayList<CorporatePersonalList> personalList) {
        PersonalList = personalList;
    }

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

    public String getCustNbr() {
        return custNbr;
    }

    public void setCustNbr(String custNbr) {
        this.custNbr = custNbr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThaiName1() {
        return thaiName1;
    }

    public void setThaiName1(String thaiName1) {
        this.thaiName1 = thaiName1;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getEstDate() {
        return estDate;
    }

    public void setEstDate(String estDate) {
        this.estDate = estDate;
    }
}
