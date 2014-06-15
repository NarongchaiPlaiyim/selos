package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.InsuranceInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.model.view.insurance.InsuranceInfoSummaryView;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.model.view.insurance.InsuranceInfoSectionView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private InsuranceInfoSummaryView infoSummaryView;
    private List<InsuranceInfoView> insuranceInfoViewList;
    
    private BasicInfoView basicInfoView;
    
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
	private BasicInfoControl basicInfoControl;

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
             basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
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
        this.infoSummaryView = this.insuranceInfoControl.getInsuranceInforSummaryView(workCaseId);
        this.insuranceInfoViewList = this.insuranceInfoControl.getInsuranceInfo(workCaseId);
        _loadFieldControl(); 
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

    public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}
    
    public Date getLastUpdateDateTime() {
		return infoSummaryView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (infoSummaryView.getModifyBy() != null)
			return infoSummaryView.getModifyBy().getDisplayName();
		else
			return null;
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
    
    
    /*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.InsuranceInfo);
		fieldMap.clear();
		
		for (FieldsControlView field : fields) {
			fieldMap.put(field.getFieldName(), field);
		}
		
		
	}
	public String mandate(String name) {
		boolean isMandate = FieldsControlView.DEFAULT_MANDATE;
		FieldsControlView field = fieldMap.get(name);
		if (field != null)
			isMandate = field.isMandate();
		return isMandate ? " *" : "";
	}
	
	public boolean isDisabled(String name) {
		FieldsControlView field = fieldMap.get(name);
		if (field == null)
			return FieldsControlView.DEFAULT_READONLY;
		return field.isReadOnly();
	}	
}
