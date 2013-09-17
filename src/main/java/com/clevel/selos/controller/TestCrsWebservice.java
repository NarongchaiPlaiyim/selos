package com.clevel.selos.controller;

import com.clevel.selos.ws.CaseCreation;
import com.clevel.selos.ws.CaseCreationResponse;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

@ManagedBean(name = "crs")
public class TestCrsWebservice {

    @Inject
    CaseCreation crsWebservice;


    public TestCrsWebservice(){

    }


    @PostConstruct
    public void onCreate(){
    }

    private CaseCreationResponse caseCreationResponse;

    public void callservice(){
        caseCreationResponse=crsWebservice.newCase("", "12345", "", "", "", "", "", 0, 0, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
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
}
