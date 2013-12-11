package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.model.view.insurance.model.SectionModel;
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
import java.math.BigDecimal;
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
    private List<InsuranceInfoView> insuranceInfoViewList;
    private InsuranceInfoView insuranceInfoView;


    //New / New + Change
    private int approvedType;

    //Total Premium
    private BigDecimal total;

    //*** Mode for check Add or Edit ***//
    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private int rowIndex;



    @Inject
    public InsuranceInfo() {
    }

    @PostConstruct
    public void onCreation(){
        init();
    }

    private void init(){
        insuranceInfoViewList = new ArrayList<InsuranceInfoView>();
        SectionModel sectionModel = null;
        List<SectionModel> sectionModelList = null;

        insuranceInfoView = new InsuranceInfoView();
        insuranceInfoView.setJobID("#001");
        insuranceInfoView.setPremium(new BigDecimal(9999999));

        sectionModelList = new ArrayList<SectionModel>();

        sectionModel = new SectionModel();
        sectionModel.getHeadColl().setTitleDeed("#0001");
        sectionModelList.add(sectionModel);
        sectionModel = new SectionModel();
        sectionModel.getHeadColl().setTitleDeed("#0002");
        sectionModelList.add(sectionModel);

        insuranceInfoView.setSectionList(sectionModelList);
        insuranceInfoView.setSectionList(sectionModelList);

        insuranceInfoViewList.add(insuranceInfoView);

        insuranceInfoView = new InsuranceInfoView();
        insuranceInfoView.setJobID("#002");
        insuranceInfoView.setPremium(new BigDecimal(6666666));

        sectionModelList = new ArrayList<SectionModel>();

        sectionModel = new SectionModel();
        sectionModel.getHeadColl().setTitleDeed("#0001");
        sectionModelList.add(sectionModel);
        sectionModel = new SectionModel();
        sectionModel.getHeadColl().setTitleDeed("#0002");
        sectionModelList.add(sectionModel);

        insuranceInfoView.setSectionList(sectionModelList);
        insuranceInfoView.setSectionList(sectionModelList);

        insuranceInfoViewList.add(insuranceInfoView);
        addition();
    }

    public void addition(){
        total = BigDecimal.ZERO;
        for(InsuranceInfoView view : insuranceInfoViewList){
            total = total.add(view.getPremium());
        }
    }

    public List<InsuranceInfoView> getInsuranceInfoViewList() {
        return insuranceInfoViewList;
    }

    public void setInsuranceInfoViewList(List<InsuranceInfoView> insuranceInfoViewList) {
        this.insuranceInfoViewList = insuranceInfoViewList;
    }

    public int getApprovedType() {
        return approvedType;
    }

    public void setApprovedType(int approvedType) {
        this.approvedType = approvedType;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
