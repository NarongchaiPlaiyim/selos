package com.clevel.selos.controller;

import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.master.RequestTypeDAO;
import com.clevel.selos.dao.master.RiskTypeDAO;
import com.clevel.selos.dao.master.SpecialProgramDAO;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.RequestType;
import com.clevel.selos.model.db.master.RiskType;
import com.clevel.selos.model.db.master.SpecialProgram;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "basicInfo")
public class BasicInfo implements Serializable {
    @Inject
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    private ProductGroupDAO productGroupDAO;
    @Inject
    private SpecialProgramDAO specialProgramDAO;
    @Inject
    private RequestTypeDAO requestTypeDAO;
    @Inject
    private RiskTypeDAO riskTypeDAO;

    //*** Drop down List ***//
    private List<ProductGroup> productGroupList;
    private List<SpecialProgram> specialProgramList;
    private List<RequestType> requestTypeList;
    private List<RiskType> riskTypeList;

    //*** View ***//
    private BasicInfoView basicInfoView;

    //Dialog
    private BasicInfoAccountView basicInfoAccountView;

    public BasicInfo(){

    }

    @PostConstruct
    public void onCreation() {
        basicInfoView = new BasicInfoView();
        basicInfoView.setQualitative("A");
        basicInfoView.setBaPayment("TOPUP");

        basicInfoAccountView = new BasicInfoAccountView();

        productGroupList = productGroupDAO.findAll();
        specialProgramList = specialProgramDAO.findAll();
        requestTypeList = requestTypeDAO.findAll();
        riskTypeList = riskTypeDAO.findAll();
    }

    public void onSave(){
        log.debug("basicInfoView : {}",basicInfoView);
    }

    public void onAddAccount() {
        log.debug("onAddAccount");
        basicInfoAccountView = new BasicInfoAccountView();
    }

    // Get Set
    public BasicInfoView getBasicInfoView() {
        return basicInfoView;
    }

    public void setBasicInfoView(BasicInfoView basicInfoView) {
        this.basicInfoView = basicInfoView;
    }

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroup> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public List<RequestType> getRequestTypeList() {
        return requestTypeList;
    }

    public void setRequestTypeList(List<RequestType> requestTypeList) {
        this.requestTypeList = requestTypeList;
    }

    public List<SpecialProgram> getSpecialProgramList() {
        return specialProgramList;
    }

    public void setSpecialProgramList(List<SpecialProgram> specialProgramList) {
        this.specialProgramList = specialProgramList;
    }

    public List<RiskType> getRiskTypeList() {
        return riskTypeList;
    }

    public void setRiskTypeList(List<RiskType> riskTypeList) {
        this.riskTypeList = riskTypeList;
    }
}
