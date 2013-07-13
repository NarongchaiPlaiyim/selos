package com.clevel.selos.controller;

import com.clevel.selos.model.db.Facility;
import com.clevel.selos.system.MessageProvider;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name="prescreenMaker")
public class PrescreenMaker implements Serializable {
    @Inject
    Logger log;
    @Inject
    MessageProvider msg;

    private List<Facility> facilityList;
    private String frmFacilityName;
    private String frmRequestAmount;


    public PrescreenMaker() {
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation.");
        if(facilityList == null){
            facilityList = new ArrayList<Facility>();
        }
        Facility facility = new Facility();
        facility.setFacilityName("Loan");
        facility.setRequestAmount(new BigDecimal("2000000"));
        facilityList.add(facility);
    }

    public void addFacility(){

    }

    public List<Facility> getFacilityList() {
        return facilityList;
    }

    public void setFacilityList(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }

    public String getFrmRequestAmount() {
        return frmRequestAmount;
    }

    public void setFrmRequestAmount(String frmRequestAmount) {
        this.frmRequestAmount = frmRequestAmount;
    }

    public String getFrmFacilityName() {
        return frmFacilityName;
    }

    public void setFrmFacilityName(String frmFacilityName) {
        this.frmFacilityName = frmFacilityName;
    }
}
