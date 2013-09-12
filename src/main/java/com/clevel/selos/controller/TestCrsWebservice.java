package com.clevel.selos.controller;

import com.clevel.selos.integration.crs.CrsWebservice;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.util.Date;

@ManagedBean(name = "crs")
public class TestCrsWebservice {

    @Inject
    CrsWebservice crsWebservice;


    public TestCrsWebservice(){

    }


    @PostConstruct
    public void onCreate(){

    }

    private String test;

    public void callservice(){
                 test=crsWebservice.csrService("",getTest(),"","","","","",0,0,"","","","","","","","","","","","","","","","","","","","",new Date());
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
