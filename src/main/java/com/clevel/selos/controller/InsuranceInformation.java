package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.InsuranceInfoControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ViewScoped
@ManagedBean(name = "insuranceInfo")
public class InsuranceInformation implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    //*** View ***//
    private List<InsuranceInfoView> insuranceInfoViewList;
    
    //New / New + Change
    private int approvedType;

    //Total Premium
    private BigDecimal total;

    //*** Mode for check Add or Edit ***//
    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private int rowIndex;

    //session
    private long workCaseId;
    
    @Inject
    private InsuranceInfoControl insuranceInfoControl;

    @Inject
    public InsuranceInformation() {
    }

    @PostConstruct
    public void onCreation(){
        init();
    }

    private void init(){
        /*insuranceInfoViewList = new ArrayList<InsuranceInfoView>();
        InsuranceInfoSectionView sectionModel = null;
        List<InsuranceInfoSectionView> sectionModelList = null;

        insuranceInfoView = new InsuranceInfoView();
        insuranceInfoView.setJobID("#001");
        insuranceInfoView.setPremium(new BigDecimal(9999999));

        sectionModelList = new ArrayList<InsuranceInfoSectionView>();

        sectionModel = new InsuranceInfoSectionView();
        sectionModel.getHeadColl().setTitleDeed("#0001");
        sectionModelList.add(sectionModel);
        sectionModel = new InsuranceInfoSectionView();
        sectionModel.getHeadColl().setTitleDeed("#0002");
        sectionModelList.add(sectionModel);

        insuranceInfoView.setSectionList(sectionModelList);
        insuranceInfoView.setSectionList(sectionModelList);

        insuranceInfoViewList.add(insuranceInfoView);

        insuranceInfoView = new InsuranceInfoView();
        insuranceInfoView.setJobID("#002");
        insuranceInfoView.setPremium(new BigDecimal(6666666));

        sectionModelList = new ArrayList<InsuranceInfoSectionView>();

        sectionModel = new InsuranceInfoSectionView();
        sectionModel.getHeadColl().setTitleDeed("#0001");
        sectionModelList.add(sectionModel);
        sectionModel = new InsuranceInfoSectionView();
        sectionModel.getHeadColl().setTitleDeed("#0002");
        sectionModelList.add(sectionModel);

        insuranceInfoView.setSectionList(sectionModelList);
        insuranceInfoView.setSectionList(sectionModelList);

        insuranceInfoViewList.add(insuranceInfoView);*/
    	 HttpSession session = FacesUtil.getSession(true);

         if(session.getAttribute("workCaseId") != null){
        	 
             workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
             log.info("init ::: workCaseId is " + workCaseId);
         }else{
             log.info("init ::: workCaseId is null.");
             try{
                 FacesUtil.redirect("/site/insuranceInfo.jsf");
                 return;
             }catch (Exception ex){
                 log.info("Exception :: {}",ex);
             }
         }
         
        this.insuranceInfoViewList = this.insuranceInfoControl.getInsuranceInfo(workCaseId);
         
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
    
    public void onSave(){
    	log.info("InsuranceInfo: onSave()");
    	insuranceInfoControl.saveInsuranceInfo(insuranceInfoViewList, this.total, workCaseId);
    	this.onCreation();
    }
}
