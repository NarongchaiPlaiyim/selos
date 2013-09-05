package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.view.Stakeholder;
import com.clevel.selos.system.message.NormalMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 5/9/2556
 * Time: 16:26 น.
 * To change this template use File | Settings | File Templates.
 */
@ViewScoped
@ManagedBean(name = "businessInfoDetail")
public class BusinessInfoDetailMaker implements Serializable {
    @NormalMessage
    private Stakeholder stakeholder;
    private List<Stakeholder> supplierList;
    private List<Stakeholder> buyerList;
    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;
    private String stakeType;
    @Inject
    Logger log;
    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;

    public BusinessInfoDetailMaker(){

    }

    @PostConstruct
    public void onCreation(){
        stakeholder = new Stakeholder();
        supplierList = new ArrayList<Stakeholder>();
        buyerList = new ArrayList<Stakeholder>();
        businessGroupList = businessGroupDAO.findAll();
    }

    public void onChangeBusinessGroup(){
        businessDescriptionList = businessDescriptionDAO.findAll();
    }

    public void onAddStakeholder(String uiStakeholder){
        stakeholder = new Stakeholder();
        stakeType = uiStakeholder;
        log.info("stakeType >>> {}",stakeType);
    }

    public void onSaveStakeholder(){
        log.info("stakeholder {}",stakeholder);
         if(stakeType.equals("supplier")){
            supplierList.add(stakeholder);
         }else if(stakeType.equals("buyer")){
            buyerList.add(stakeholder);
         }

        stakeholder = new Stakeholder();
    }


    public Stakeholder getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(Stakeholder stakeholder) {
        this.stakeholder = stakeholder;
    }

    public List<Stakeholder> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<Stakeholder> supplierList) {
        this.supplierList = supplierList;
    }

    public List<Stakeholder> getBuyerList() {
        return buyerList;
    }

    public void setBuyerList(List<Stakeholder> buyerList) {
        this.buyerList = buyerList;
    }

    public List<BusinessGroup> getBusinessGroupList() {
        return businessGroupList;
    }

    public void setBusinessGroupList(List<BusinessGroup> businessGroupList) {
        this.businessGroupList = businessGroupList;
    }

    public List<BusinessDescription> getBusinessDescriptionList() {
        return businessDescriptionList;
    }

    public void setBusinessDescriptionList(List<BusinessDescription> businessDescriptionList) {
        this.businessDescriptionList = businessDescriptionList;
    }
}
