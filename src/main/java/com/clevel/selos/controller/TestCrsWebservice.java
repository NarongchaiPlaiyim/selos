package com.clevel.selos.controller;

import com.clevel.selos.ws.CaseCreation;
import com.clevel.selos.ws.CaseCreationResponse;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.util.Date;

@ManagedBean(name = "crs")
public class TestCrsWebservice {

    @Inject
    CaseCreation crsWebservice;


    public TestCrsWebservice(){

    }

    private String testValue;

    @PostConstruct
    public void onCreate(){
    }

    private CaseCreationResponse caseCreationResponse;

    public void callservice(){

        caseCreationResponse=crsWebservice.newCase("KK", new Date()+"", "2323", "", "", "", getTestValue(), 0, 0, "", "", "", "", "", "0", "0", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    public CaseCreation getCrsWebservice() {
        return crsWebservice;
    }

    public void setCrsWebservice(CaseCreation crsWebservice) {
        this.crsWebservice = crsWebservice;
    }

    public CaseCreationResponse getCaseCreationResponse() {
        return caseCreationResponse;
    }

    public void setCaseCreationResponse(CaseCreationResponse caseCreationResponse) {
        this.caseCreationResponse = caseCreationResponse;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }
}
