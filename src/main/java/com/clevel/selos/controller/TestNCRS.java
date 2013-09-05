package com.clevel.selos.controller;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncrs.service.NCRSModel;
import com.clevel.selos.integration.ncrs.service.NCRSService;
import org.slf4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

@ViewScoped
@ManagedBean(name="TestNCRS")
public class TestNCRS implements Serializable {

    @Inject
    @NCB
    Logger log;

    @Inject
    NCRSService service;

    private String result;

    //Config
    private String url = "http://10.175.230.112/ncrs/servlet/xmladapter";
    private String id = "SLOSTEST";
    private String pass = "SLOSTEST12";



    private String memberref = "123456789";
    private String enqpurpose = "01";
    private String enqamount;
    private String consent = "Y";
    private String disputeenquiry;

    @Inject
    public TestNCRS() {
    }

    public void onClick(){
        //System.out.println("========================================= Onclick");
        log.info("========================================= Onclick");

        NCRSModel ncrsModel = new NCRSModel();

        TUEFEnquiryNameModel nameModel = new TUEFEnquiryNameModel("aa", "bb", "19000101");
        ArrayList<TUEFEnquiryNameModel> name = new ArrayList<TUEFEnquiryNameModel>();
        name.add(nameModel);

        TUEFEnquiryIdModel idModel = new TUEFEnquiryIdModel("01", "3111111111115", null);
        ArrayList<TUEFEnquiryIdModel> id = new ArrayList<TUEFEnquiryIdModel>();
        id.add(idModel);

        ncrsModel.setEnqpurpose("01");
        ncrsModel.setEnqamount("0");
        ncrsModel.setConsent("Y");
        ncrsModel.setIdList(id);
        ncrsModel.setNameList(name);

        service.process(ncrsModel);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMemberref() {
        return memberref;
    }

    public void setMemberref(String memberref) {
        this.memberref = memberref;
    }

    public String getEnqpurpose() {
        return enqpurpose;
    }

    public void setEnqpurpose(String enqpurpose) {
        this.enqpurpose = enqpurpose;
    }

    public String getEnqamount() {
        return enqamount;
    }

    public void setEnqamount(String enqamount) {
        this.enqamount = enqamount;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public String getDisputeenquiry() {
        return disputeenquiry;
    }

    public void setDisputeenquiry(String disputeenquiry) {
        this.disputeenquiry = disputeenquiry;
    }
}
