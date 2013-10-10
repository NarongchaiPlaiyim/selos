package com.clevel.selos.controller;

import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.model.db.master.ProductGroup;
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

    //*** Drop down List ***//
    private List<ProductGroup> productGroupList;

    //*** View ***//
    private BasicInfoView basicInfoView;

    public BasicInfo(){

    }

    @PostConstruct
    public void onCreation() {
        basicInfoView = new BasicInfoView();
        productGroupList = productGroupDAO.findAll();
    }

    public void onSave(){
        log.debug("basicInfoView : {}",basicInfoView);
    }

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroup> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public BasicInfoView getBasicInfoView() {
        return basicInfoView;
    }

    public void setBasicInfoView(BasicInfoView basicInfoView) {
        this.basicInfoView = basicInfoView;
    }
}
