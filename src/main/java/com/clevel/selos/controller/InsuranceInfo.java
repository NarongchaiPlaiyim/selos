package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.insurance.InsuranceInfoDetailView;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.model.view.insurance.model.InsurerNameModel;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.DateTimeUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "insuranceInfo")
public class InsuranceInfo implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    //*** View ***//

    private InsuranceInfoView insuranceInfoView;
    private List<InsuranceInfoDetailView> insuranceInfoDetailViewList;
    private InsuranceInfoDetailView insuranceInfoDetailViewSelected;



    //*** Mode for check Add or Edit ***//
    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private int rowIndex;

    //*** Drop down List ***//
    private List<InsurerNameModel> insurerNameList;





    @Inject
    public InsuranceInfo() {
    }

    @PostConstruct
    public void onCreation(){
        init();
        insuranceInfoDetailViewList = new ArrayList<InsuranceInfoDetailView>();
        insuranceInfoDetailViewList.add(insuranceInfoDetailViewSelected);
        insuranceInfoDetailViewList.add(insuranceInfoDetailViewSelected);

    }

    public void onAddDetail(){
        modeForButton = ModeForButton.ADD;
        init();
    }

    public void addDetail(){
        Date date = null;
        int id = 0;
        String value = null;

        //Insurer's Name
        id = insuranceInfoView.getInsuranceInfoDetailView().getInsurerName().getId();
        if(id > 0){
            for(InsurerNameModel model : insurerNameList){
                if(model.getId() == id){
                    insuranceInfoView.getInsuranceInfoDetailView().getInsurerName().setName(model.getName());
                    break;
                } else {
                    continue;
                }
            }
        }

        //วัตถุประสงค์การใช้อาคาร
        value = insuranceInfoView.getInsuranceInfoDetailView().getWaitForName();
        if(value == null || "null".equalsIgnoreCase(value) || "".equals(value)){
            insuranceInfoView.getInsuranceInfoDetailView().setWaitForName(" - ");
        }

        //Insurance Policy Number
        value = insuranceInfoView.getInsuranceInfoDetailView().getInsurancePolicyNumber();
        if(value == null || "null".equalsIgnoreCase(value) || "".equals(value)){
            insuranceInfoView.getInsuranceInfoDetailView().setInsurancePolicyNumber(" - ");
        }

        //Risk Code
        value = insuranceInfoView.getInsuranceInfoDetailView().getRiskCode();
        if(value == null || "null".equalsIgnoreCase(value) || "".equals(value)){
            insuranceInfoView.getInsuranceInfoDetailView().setRiskCode(" - ");
        }

        //EffectiveDateOfInsurance
        date = insuranceInfoView.getInsuranceInfoDetailView().getEffectiveDateOfInsurance();
        insuranceInfoView.getInsuranceInfoDetailView().setEffectiveDateOfInsuranceShow(DateTimeUtil.getDateStr(date));

        //ExpiryDateOfInsurance
        date = insuranceInfoView.getInsuranceInfoDetailView().getExpiryDateOfInsurance();
        insuranceInfoView.getInsuranceInfoDetailView().setExpiryDateOfInsuranceShow(DateTimeUtil.getDateStr(date));

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            insuranceInfoDetailViewSelected = insuranceInfoView.getInsuranceInfoDetailView();
            insuranceInfoDetailViewList.add(insuranceInfoDetailViewSelected);
        } else {
            insuranceInfoDetailViewSelected = insuranceInfoView.getInsuranceInfoDetailView();
            insuranceInfoDetailViewList.set(rowIndex, insuranceInfoDetailViewSelected);
        }

        boolean complete = true;
        RequestContext context = RequestContext.getCurrentInstance();
        log.debug("Complete : {}", complete);
        context.addCallbackParam("functionComplete", complete);
    }

    public void removeDetail(){
        insuranceInfoDetailViewList.remove(insuranceInfoDetailViewSelected);
    }

    public void editDetail(){
        modeForButton = ModeForButton.EDIT;
        insuranceInfoView.setInsuranceInfoDetailView(insuranceInfoDetailViewSelected);
    }

    private void init(){
        insuranceInfoView = new InsuranceInfoView();

        //Insurer's Name
        InsurerNameModel insurerName = null;
        insurerNameList = new ArrayList<InsurerNameModel>();

        insurerName = new InsurerNameModel();
        insurerName.setId(01);
        insurerName.setName("test");
        insurerNameList.add(insurerName);

        insurerName = new InsurerNameModel();
        insurerName.setId(02);
        insurerName.setName("test2");
        insurerNameList.add(insurerName);

        insurerName = new InsurerNameModel();
        insurerName.setId(03);
        insurerName.setName("test3");
        insurerNameList.add(insurerName);

    }

    public InsuranceInfoView getInsuranceInfoView() {
        return insuranceInfoView;
    }

    public void setInsuranceInfoView(InsuranceInfoView insuranceInfoView) {
        this.insuranceInfoView = insuranceInfoView;
    }

    public List<InsuranceInfoDetailView> getInsuranceInfoDetailViewList() {
        return insuranceInfoDetailViewList;
    }

    public void setInsuranceInfoDetailViewList(List<InsuranceInfoDetailView> insuranceInfoDetailViewList) {
        this.insuranceInfoDetailViewList = insuranceInfoDetailViewList;
    }

    public InsuranceInfoDetailView getInsuranceInfoDetailViewSelected() {
        return insuranceInfoDetailViewSelected;
    }

    public void setInsuranceInfoDetailViewSelected(InsuranceInfoDetailView insuranceInfoDetailViewSelected) {
        this.insuranceInfoDetailViewSelected = insuranceInfoDetailViewSelected;
    }

    public ModeForButton getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(ModeForButton modeForButton) {
        this.modeForButton = modeForButton;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<InsurerNameModel> getInsurerNameList() {
        return insurerNameList;
    }

    public void setInsurerNameList(List<InsurerNameModel> insurerNameList) {
        this.insurerNameList = insurerNameList;
    }
}
