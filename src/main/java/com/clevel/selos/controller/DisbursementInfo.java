package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.DisbursementControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "disbursementInfo")
public class DisbursementInfo implements Serializable {

	@Inject @SELOS
	private Logger log;
	@Inject @NormalMessage
	private Message message;
	@Inject
	private BasicInfoControl basicInfoControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private BasicInfoView basicInfoView;
   
    enum ModeForButton {ADD, EDIT}

    private ModeForButton modeForButton;
    private int selectRowNumber;

    private DisbursementInfoView disbursementInfoView;
    
    private DisbursementMcDetailView disbursementMcDetailView = new DisbursementMcDetailView();
    
    private DisbursementDepositBaDetailView disbursementDepositDetailView = new DisbursementDepositBaDetailView();
    
    private DisbursementBahtnetDetailView disbursementBahtnetDetailView = new DisbursementBahtnetDetailView();
    
    
    private List<SelectItem> bankList;
    
    private List<SelectItem> crossTypeList;    
    
    private List<SelectItem> bankBranchList;
    
    private HashMap<Integer, String> bankMap;
    private HashMap<Integer, String> crossTypeMap;
    private HashMap<Integer, String> bankBranchMap;
    
    private List<Long> disbursementMcDeleteList = new ArrayList<Long>();
    
    private List<Long> disbursementDepositDeleteList = new ArrayList<Long>();
    
    private List<Long> disbursementBahtnetDeleteList = new ArrayList<Long>();
    
    private HashMap<Long, BigDecimal> totalDisburseMCAmount = new HashMap<Long, BigDecimal>();
    private HashMap<Long, BigDecimal> totalDisburseTRAmount  = new HashMap<Long, BigDecimal>();
    private HashMap<Long, BigDecimal> totalDisburseBAAmount  = new HashMap<Long, BigDecimal>();
    
    @Inject
    DisbursementControl disbursementControl;
    
    @PostConstruct
    public void onCreate() {
    	_loadDropdown();
    	_loadData();
    	HttpSession session = FacesUtil.getSession(false);
		if (session != null) {
			workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
			stepId = Util.parseLong(session.getAttribute("stepId"), -1);
		}
		this.disbursementInfoView = disbursementControl.getDisbursementInfoView(workCaseId);
		calculationSummaryTotalMc();
		calculationSummaryTotalDeposit();
		calculationSummaryTotalBahtnet();
		calculationSummary();
		calculationSummaryTotalBa();
    }
    
    private void _loadDropdown() {
    	bankList = disbursementControl.getBanks();
    	crossTypeList = disbursementControl.getCrossTypes();
    	bankBranchList = disbursementControl.getBankBranches();
    }
    
    private void _loadData() {
    	bankMap = disbursementControl.getBankMap();
    	crossTypeMap = disbursementControl.getCrossTypeMap();
    	bankBranchMap = disbursementControl.getBankBranchMap();
    }
    
    public Date getLastUpdateDateTime() {
		return disbursementInfoView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (disbursementInfoView.getModifyBy() != null)
			return disbursementInfoView.getModifyBy().getDisplayName();
		else
			return null;
	}
	public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}

    public void onOpenAddMcDialog() {
        log.debug("onOpenAddMcDialog()");

        modeForButton = ModeForButton.ADD;
        disbursementMcDetailView = new DisbursementMcDetailView();
        List<DisbursementCreditTypeView> creditTypeViewList = new ArrayList<DisbursementCreditTypeView>();
        for (DisbursementSummaryView disbursementSummaryView : this.disbursementInfoView.getDisburse()){
        	DisbursementCreditTypeView disbursementCreditTypeView = new DisbursementCreditTypeView();
        	disbursementCreditTypeView.setNewCreditDetailId(disbursementSummaryView.getNewCreditDetailID());
        	disbursementCreditTypeView.setProductProgram(disbursementSummaryView.getProductProgram());
        	disbursementCreditTypeView.setCreditFacility(disbursementSummaryView.getCreditFacility());
        	disbursementCreditTypeView.setLimitAmount(disbursementSummaryView.getApprovedLimit());
        	creditTypeViewList.add(disbursementCreditTypeView);
        }
        disbursementMcDetailView.setDisbursementCreditTypeView(creditTypeViewList);

    }

    public void onOpenEditMcDialog() {
        log.debug("onOpenEditMcDialog()");
        modeForButton = ModeForButton.EDIT;

        //disbursementMcDetailView = new DisbursementMcDetailView();
        //disbursementMcDetailView.setIssuedBy(newDisbursementMcDetailView.getIssuedBy());
        //disbursementMcDetailView.setIssuedDate(newDisbursementMcDetailView.getIssuedDate());
        //disbursementMcDetailView.setPayeeName(newDisbursementMcDetailView.getPayeeName());
        //disbursementMcDetailView.setPayeeSubname(newDisbursementMcDetailView.getPayeeSubname());
        //disbursementMcDetailView.setCrossType(newDisbursementMcDetailView.getCrossType());
        //disbursementMcDetailView.setCreditType(cloneCreditTypeList(newDisbursementMcDetailView.getCreditType()));
    }

    public void onOpenAddDepositDialog() {
        log.debug("onOpenAddDepositDialog()");

        modeForButton = ModeForButton.ADD;
        disbursementDepositDetailView = new DisbursementDepositBaDetailView();
        List<DisbursementCreditTypeView> creditTypeViewList = new ArrayList<DisbursementCreditTypeView>();
        for (DisbursementSummaryView disbursementSummaryView : this.disbursementInfoView.getDisburse()){
        	DisbursementCreditTypeView disbursementCreditTypeView = new DisbursementCreditTypeView();
        	disbursementCreditTypeView.setNewCreditDetailId(disbursementSummaryView.getNewCreditDetailID());
        	disbursementCreditTypeView.setProductProgram(disbursementSummaryView.getProductProgram());
        	disbursementCreditTypeView.setCreditFacility(disbursementSummaryView.getCreditFacility());
        	disbursementCreditTypeView.setLimitAmount(disbursementSummaryView.getApprovedLimit());
        	creditTypeViewList.add(disbursementCreditTypeView);
        }
        disbursementDepositDetailView.setDisbursementCreditTypeView(creditTypeViewList);
        //disbursementDepositDetailView.setCreditType(initCreditTypeList());
    }

    public void onOpenEditDepositDialog() {
        log.debug("onOpenEditDepositDialog()");

        modeForButton = ModeForButton.EDIT;
        //disbursementDepositDetailView = new DisbursementDepositBaDetailView();

        //disbursementDepositDetailView.setAccountNumber(newDisbursementDepositDetailView.getAccountNumber());
        //disbursementDepositDetailView.setAccountName(newDisbursementDepositDetailView.getAccountName());
        //disbursementDepositDetailView.setCreditType(cloneCreditTypeList(newDisbursementDepositDetailView.getCreditType()));

    }

    public void onOpenAddBahtnetDialog() {
        log.debug("onOpenAddBahtnetDialog()");
        //bankList = bankDAO.findAll();
        modeForButton = ModeForButton.ADD;
        disbursementBahtnetDetailView = new DisbursementBahtnetDetailView();
        List<DisbursementCreditTypeView> creditTypeViewList = new ArrayList<DisbursementCreditTypeView>();
        for (DisbursementSummaryView disbursementSummaryView : this.disbursementInfoView.getDisburse()){
        	DisbursementCreditTypeView disbursementCreditTypeView = new DisbursementCreditTypeView();
        	disbursementCreditTypeView.setNewCreditDetailId(disbursementSummaryView.getNewCreditDetailID());
        	disbursementCreditTypeView.setProductProgram(disbursementSummaryView.getProductProgram());
        	disbursementCreditTypeView.setCreditFacility(disbursementSummaryView.getCreditFacility());
        	disbursementCreditTypeView.setLimitAmount(disbursementSummaryView.getApprovedLimit());
        	creditTypeViewList.add(disbursementCreditTypeView);
        }
        disbursementBahtnetDetailView.setDisbursementCreditTypeView(creditTypeViewList);
    }

    public void onOpenEditBahtnetDialog() {
        log.debug("onOpenEditBahtnetDialog()");
        //bankList = bankDAO.findAll();
        modeForButton = ModeForButton.EDIT;
        //disbursementBahtnetDetailView = new DisbursementBahtnetDetailView();
        //disbursementBahtnetDetailView.setCreditType(cloneCreditTypeList(newDisbursementBahtnetDetailView.getCreditType()));
        //disbursementBahtnetDetailView.setBankCode(newDisbursementBahtnetDetailView.getBankCode());
        //disbursementBahtnetDetailView.setAccountNumber(newDisbursementBahtnetDetailView.getAccountNumber());
        //disbursementBahtnetDetailView.setBenefitName(newDisbursementBahtnetDetailView.getBenefitName());


    }

    private List<CreditTypeDetailView> cloneCreditTypeList(List<CreditTypeDetailView> list){
        List<CreditTypeDetailView> creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
        for(CreditTypeDetailView creditTypes : list){
            CreditTypeDetailView creditTypeDetailView = new CreditTypeDetailView();
            creditTypeDetailView.setProductProgram(creditTypes.getProductProgram());
            creditTypeDetailView.setCreditFacility(creditTypes.getCreditFacility());
            creditTypeDetailView.setLimit(creditTypes.getLimit());
            creditTypeDetailView.setDisburseAmount(creditTypes.getDisburseAmount());
            creditTypeDetailView.setComponentFlag(true);
            creditTypeDetailViewList.add(creditTypeDetailView);
        }
        return creditTypeDetailViewList;
    }


    public void onSubmitDisbursementMc() {
        log.debug("onSubmitDisbursementMc()");
        log.info("modeForButton ::: {}", modeForButton);

        boolean complete = false;
        BigDecimal totalAmount = BigDecimal.ZERO;
        RequestContext context = RequestContext.getCurrentInstance();
        int falseCount = 0;
        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

            if (disbursementMcDetailView != null || disbursementMcDetailView.getDisbursementCreditTypeView() != null) {

                for (int i = 0; i < disbursementMcDetailView.getDisbursementCreditTypeView().size(); i++) {
                    if (disbursementMcDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount().compareTo(disbursementMcDetailView.getDisbursementCreditTypeView().get(i).getLimitAmount()) > 0) {
                        disbursementMcDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementMcDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementMcDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount());
                }

                if (falseCount == 0) {
                    disbursementMcDetailView.setTotalAmount(totalAmount);
                    disbursementMcDetailView.setPayeeName(disbursementMcDetailView.getPayeeName());
                    this.disbursementInfoView.getDisburseMcList().add(disbursementMcDetailView);
                    //disbursementInfoView.setDisburseMcList(disbursementMcList);
                    complete = true;
                }
            }
        }
        if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {

            if (disbursementMcDetailView != null || disbursementMcDetailView.getDisbursementCreditTypeView() != null) {

                for (int i = 0; i < disbursementMcDetailView.getDisbursementCreditTypeView().size(); i++) {
                    if (disbursementMcDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount().compareTo(disbursementMcDetailView.getDisbursementCreditTypeView().get(i).getLimitAmount()) > 0) {
                        disbursementMcDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementMcDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementMcDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount());
                }

                if (falseCount == 0) {
                	this.disbursementInfoView.getDisburseMcList().get(selectRowNumber).setIssuedBy(disbursementMcDetailView.getIssuedBy());
                	this.disbursementInfoView.getDisburseMcList().get(selectRowNumber).setIssuedDate(disbursementMcDetailView.getIssuedDate());
                	this.disbursementInfoView.getDisburseMcList().get(selectRowNumber).setPayeeName(disbursementMcDetailView.getPayeeName());
                	this.disbursementInfoView.getDisburseMcList().get(selectRowNumber).setPayeeSubname(disbursementMcDetailView.getPayeeSubname());
                	this.disbursementInfoView.getDisburseMcList().get(selectRowNumber).setCrossType(disbursementMcDetailView.getCrossType());
                	this.disbursementInfoView.getDisburseMcList().get(selectRowNumber).setDisbursementCreditTypeView(disbursementMcDetailView.getDisbursementCreditTypeView());
                	this.disbursementInfoView.getDisburseMcList().get(selectRowNumber).setTotalAmount(totalAmount);
                    complete = true;
                }
            }
        }

        calculationSummaryTotalMc();
        calculationSummary();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onSubmitDisbursementDeposit() {
        log.debug("onSubmitDisbursementDeposit()");
        log.info("modeForButton ::: {}", modeForButton);

        boolean complete = false;
        BigDecimal totalAmount = BigDecimal.ZERO;
        RequestContext context = RequestContext.getCurrentInstance();
        int falseCount = 0;

        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

            if (disbursementDepositDetailView != null || disbursementDepositDetailView.getDisbursementCreditTypeView() != null) {

                for (int i = 0; i < disbursementDepositDetailView.getDisbursementCreditTypeView().size(); i++) {
                    if (disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount().compareTo(disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).getLimitAmount()) > 0) {

                        disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount());
                }
                if (falseCount == 0) {
                    disbursementDepositDetailView.setTotalAmount(totalAmount);
                    this.disbursementInfoView.getDisburseDepositList().add(disbursementDepositDetailView);
                    complete = true;
                }
            }
        }
        if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {

            if (disbursementDepositDetailView != null || disbursementDepositDetailView.getDisbursementCreditTypeView() != null) {

                for (int i = 0; i < disbursementDepositDetailView.getDisbursementCreditTypeView().size(); i++) {
                    if (disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount().compareTo(disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).getLimitAmount()) > 0) {

                        disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementDepositDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount());
                }

                if (falseCount == 0) {
                	this.disbursementInfoView.getDisburseDepositList().get(selectRowNumber).setAccountNumber(disbursementDepositDetailView.getAccountNumber());
                	this.disbursementInfoView.getDisburseDepositList().get(selectRowNumber).setAccountName(disbursementDepositDetailView.getAccountName());
                	this.disbursementInfoView.getDisburseDepositList().get(selectRowNumber).setDisbursementCreditTypeView(disbursementDepositDetailView.getDisbursementCreditTypeView());
                	this.disbursementInfoView.getDisburseDepositList().get(selectRowNumber).setTotalAmount(totalAmount);
                    complete = true;
                }
            }
        }

        calculationSummaryTotalDeposit();
        calculationSummary();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onSubmitDisbursementBahtnet() {
        log.debug("onSubmitDisbursementBahtnet()");
        log.info("modeForButton ::: {}", modeForButton);


        boolean complete = false;
        BigDecimal totalAmount = BigDecimal.ZERO;
        RequestContext context = RequestContext.getCurrentInstance();
        int falseCount = 0;
        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

            if (disbursementBahtnetDetailView != null || disbursementBahtnetDetailView.getDisbursementCreditTypeView() != null) {

                for (int i = 0; i < disbursementBahtnetDetailView.getDisbursementCreditTypeView().size(); i++) {
                    if (disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount().compareTo(disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).getLimitAmount()) > 0) {
                        disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount());
                }
                if (falseCount == 0) {
                    disbursementBahtnetDetailView.setBankCode(disbursementBahtnetDetailView.getBankCode());
                    disbursementBahtnetDetailView.setTotalAmount(totalAmount);
                    this.disbursementInfoView.getDisbursementBahtnetList().add(disbursementBahtnetDetailView);
                    //disbursementInfoView.setDisbursementBahtnetList(disbursementBahtnetList);
                    complete=true;
                }
            }
        }
        if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {

            if (disbursementBahtnetDetailView != null || disbursementBahtnetDetailView.getDisbursementCreditTypeView() != null) {

                for (int i = 0; i < disbursementBahtnetDetailView.getDisbursementCreditTypeView().size(); i++) {
                    if (disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount().compareTo(disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).getLimitAmount()) > 0) {

                        disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).setComponentFlag(true);

                    }
                    totalAmount = totalAmount.add(disbursementBahtnetDetailView.getDisbursementCreditTypeView().get(i).getDisburseAmount());
                }

                if (falseCount == 0) {
                	this.disbursementInfoView.getDisbursementBahtnetList().get(selectRowNumber).setBankCode(disbursementBahtnetDetailView.getBankCode());
                	this.disbursementInfoView.getDisbursementBahtnetList().get(selectRowNumber).setBenefitName(disbursementBahtnetDetailView.getBenefitName());
                	this.disbursementInfoView.getDisbursementBahtnetList().get(selectRowNumber).setAccountNumber(disbursementBahtnetDetailView.getAccountNumber());
                	this.disbursementInfoView.getDisbursementBahtnetList().get(selectRowNumber).setBranchName(disbursementBahtnetDetailView.getBranchName());
                	this.disbursementInfoView.getDisbursementBahtnetList().get(selectRowNumber).setDisbursementCreditTypeView(disbursementBahtnetDetailView.getDisbursementCreditTypeView());
                	this.disbursementInfoView.getDisbursementBahtnetList().get(selectRowNumber).setTotalAmount(totalAmount);
                    complete = true;
                }
            }
        }
        calculationSummaryTotalBahtnet();
        calculationSummary();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteDisbursementMcList() {
        log.debug("onDeleteDisbursementMcList()");
        if ( this.disbursementInfoView.getDisburseMcList().get(selectRowNumber).getId() > 0)
        	this.disbursementMcDeleteList.add(this.disbursementInfoView.getDisburseMcList().get(selectRowNumber).getId());
        this.disbursementInfoView.getDisburseMcList().remove(selectRowNumber);
        calculationSummaryTotalMc();
        calculationSummary();
    }

    public void onDeleteDisbursementDepositList() {
        log.debug("onDeleteDisbursementDepositList()");
        if ( this.disbursementInfoView.getDisburseDepositList().get(selectRowNumber).getId() > 0)
        	this.disbursementDepositDeleteList.add(this.disbursementInfoView.getDisburseDepositList().get(selectRowNumber).getId());
        this.disbursementInfoView.getDisburseDepositList().remove(selectRowNumber);
        calculationSummaryTotalDeposit();
        calculationSummary();
    }

    public void onDeleteDisbursementBahtnetList() {
        log.debug("onDeleteDisbursementBahtnetList()");
        if ( this.disbursementInfoView.getDisbursementBahtnetList().get(selectRowNumber).getId() > 0)
        	this.disbursementBahtnetDeleteList.add(this.disbursementInfoView.getDisbursementBahtnetList().get(selectRowNumber).getId());
        this.disbursementInfoView.getDisbursementBahtnetList().remove(selectRowNumber);
        calculationSummaryTotalBahtnet();
        calculationSummary();
    }
    
    private void calculationSummary(){
    	BigDecimal totalDisburse = BigDecimal.ZERO;
    	for (DisbursementSummaryView disbursementSummaryView : this.disbursementInfoView.getDisburse()){
    		long newCreditDetailId = disbursementSummaryView.getNewCreditDetailID();
    		BigDecimal disburseMCAmount = this.totalDisburseMCAmount.get(newCreditDetailId);
    		if (disburseMCAmount != null)
    			totalDisburse = totalDisburse.add(disburseMCAmount);
    		BigDecimal disburseTRAmount = this.totalDisburseTRAmount.get(newCreditDetailId);
    		if (disburseTRAmount != null)
    			totalDisburse = totalDisburse.add(disburseTRAmount);
    		BigDecimal disburseBAAmount = this.totalDisburseBAAmount.get(newCreditDetailId);
    		if (disburseBAAmount != null)
    			totalDisburse = totalDisburse.add(disburseBAAmount);	
    		
    		disbursementSummaryView.setDisburseAmount(totalDisburse);
    		disbursementSummaryView.setDiffAmount(disbursementSummaryView.getApprovedLimit().subtract(disbursementSummaryView.getHoldAmount()).subtract(totalDisburse));
    	}
    }

    private void calculationSummaryTotalMc() {
    	this.totalDisburseMCAmount.clear();
        log.debug("calculationSummaryTotalMc()");
        BigDecimal totalMcDisbursement = BigDecimal.ZERO;
        int listSize = getDisbursementInfoView().getDisburseMcList().size();
        if (getDisbursementInfoView().getDisburseMcList() != null) {
            for (int i = 0; i < listSize; i++) {
            	for ( DisbursementCreditTypeView disbursementCreditTypeView : getDisbursementInfoView().getDisburseMcList().get(i).getDisbursementCreditTypeView()){
	            	long newCreditDetailId = disbursementCreditTypeView.getNewCreditDetailId();
	            	BigDecimal disburseAmount = disbursementCreditTypeView.getDisburseAmount();
	            	if (this.totalDisburseMCAmount.get(newCreditDetailId) != null)
	            		this.totalDisburseMCAmount.put(newCreditDetailId,disburseAmount.add(this.totalDisburseMCAmount.get(newCreditDetailId)));
	            	else
	            		this.totalDisburseMCAmount.put(newCreditDetailId,disburseAmount);
            	}    	
            	totalMcDisbursement = totalMcDisbursement.add(getDisbursementInfoView().getDisburseMcList().get(i).getTotalAmount());
            }
        }
        getDisbursementInfoView().setTotalMCDisburse(totalMcDisbursement);
        getDisbursementInfoView().setNumberOfCheque(listSize);
    }

    private void calculationSummaryTotalDeposit() {
        log.debug("calculationSummaryTotalDeposit()");
        this.totalDisburseTRAmount.clear();
        BigDecimal totalDepositDisbursement = BigDecimal.ZERO;
        int listSize = getDisbursementInfoView().getDisburseDepositList().size();
        if (getDisbursementInfoView().getDisburseDepositList() != null) {
            for (int i = 0; i < listSize; i++) {
            	for ( DisbursementCreditTypeView disbursementCreditTypeView : getDisbursementInfoView().getDisburseDepositList().get(i).getDisbursementCreditTypeView()){
	            	long newCreditDetailId = disbursementCreditTypeView.getNewCreditDetailId();
	            	BigDecimal disburseAmount = disbursementCreditTypeView.getDisburseAmount();
	            	if (this.totalDisburseTRAmount.get(newCreditDetailId) != null)
	            		this.totalDisburseTRAmount.put(newCreditDetailId,disburseAmount.add(this.totalDisburseTRAmount.get(newCreditDetailId)));
	            	else
	            		this.totalDisburseTRAmount.put(newCreditDetailId,disburseAmount);
            	}
                totalDepositDisbursement = totalDepositDisbursement.add(getDisbursementInfoView().getDisburseDepositList().get(i).getTotalAmount());
            }
        }
        getDisbursementInfoView().setTotalDepositDisburse(totalDepositDisbursement);
        getDisbursementInfoView().setNumberOfDeposit(listSize);
    }

    private void calculationSummaryTotalBahtnet() {
        log.debug("calculationSummaryTotalBahtnet()");
        this.totalDisburseBAAmount.clear();
        BigDecimal totalBahtnetDisbursement = BigDecimal.ZERO;
        int listSize = Util.safetyList(getDisbursementInfoView().getDisbursementBahtnetList()).size();
        if (getDisbursementInfoView().getDisbursementBahtnetList() != null) {
            for (int i = 0; i < listSize; i++) {
            	for ( DisbursementCreditTypeView disbursementCreditTypeView : getDisbursementInfoView().getDisbursementBahtnetList().get(i).getDisbursementCreditTypeView()){
	            	long newCreditDetailId = disbursementCreditTypeView.getNewCreditDetailId();
	            	BigDecimal disburseAmount = disbursementCreditTypeView.getDisburseAmount();
	            	if (this.totalDisburseBAAmount.get(newCreditDetailId) != null)
	            		this.totalDisburseBAAmount.put(newCreditDetailId,disburseAmount.add(this.totalDisburseBAAmount.get(newCreditDetailId)));
	            	else
	            		this.totalDisburseBAAmount.put(newCreditDetailId,disburseAmount);
            	}
                totalBahtnetDisbursement = totalBahtnetDisbursement.add(getDisbursementInfoView().getDisbursementBahtnetList().get(i).getTotalAmount());
            }
        }
        getDisbursementInfoView().setTotalBahtnetDisburse(totalBahtnetDisbursement);
        getDisbursementInfoView().setNumberOfBahtnet(listSize);
    }

    private void calculationSummaryTotalBa() {
        log.debug("calculationSummaryTotalBa()");
        BigDecimal totalBaDisbursement = BigDecimal.ZERO;
        int listSize = getDisbursementInfoView().getDisbursementBaList().size();
        if (getDisbursementInfoView().getDisbursementBaList() != null) {
            for (int i = 0; i < listSize; i++) {
                totalBaDisbursement = totalBaDisbursement.add(getDisbursementInfoView().getDisbursementBaList().get(i).getTotalAmount());
            }
        }
        getDisbursementInfoView().setTotalBADisburse(totalBaDisbursement);
        getDisbursementInfoView().setNumberOfBA(listSize);
    }
    
    public void save(){
    	log.debug("save()");
    	disbursementControl.saveDisbursement(workCaseId, disbursementInfoView);
    	disbursementControl.deleteDisbursementDetail(disbursementMcDeleteList, disbursementDepositDeleteList, disbursementBahtnetDeleteList);
    	onCreate();
    }

    public int getSelectRowNumber() {
        return selectRowNumber;
    }

    public void setSelectRowNumber(int selectRowNumber) {
        this.selectRowNumber = selectRowNumber;
    }

	public DisbursementInfoView getDisbursementInfoView() {
		return disbursementInfoView;
	}

	public void setDisbursementInfoView(DisbursementInfoView disbursementInfoView) {
		this.disbursementInfoView = disbursementInfoView;
	}

	public List<SelectItem> getBankBranchList() {
		return bankBranchList;
	}

	public void setBankBranchList(List<SelectItem> bankBranchList) {
		this.bankBranchList = bankBranchList;
	}

	public List<SelectItem> getBankList() {
		return bankList;
	}

	public void setBankList(List<SelectItem> bankList) {
		this.bankList = bankList;
	}

	public List<SelectItem> getCrossTypeList() {
		return crossTypeList;
	}

	public void setCrossTypeList(List<SelectItem> crossTypeList) {
		this.crossTypeList = crossTypeList;
	}

	public DisbursementMcDetailView getDisbursementMcDetailView() {
		return disbursementMcDetailView;
	}

	public void setDisbursementMcDetailView(DisbursementMcDetailView disbursementMcDetailView) {
		this.disbursementMcDetailView = disbursementMcDetailView;
	}

	public DisbursementDepositBaDetailView getDisbursementDepositDetailView() {
		return disbursementDepositDetailView;
	}

	public void setDisbursementDepositDetailView(DisbursementDepositBaDetailView disbursementDepositDetailView) {
		this.disbursementDepositDetailView = disbursementDepositDetailView;
	}

	public DisbursementBahtnetDetailView getDisbursementBahtnetDetailView() {
		return disbursementBahtnetDetailView;
	}

	public void setDisbursementBahtnetDetailView(DisbursementBahtnetDetailView disbursementBahtnetDetailView) {
		this.disbursementBahtnetDetailView = disbursementBahtnetDetailView;
	}

	public HashMap<Integer, String> getBankMap() {
		return bankMap;
	}

	public void setBankMap(HashMap<Integer, String> bankMap) {
		this.bankMap = bankMap;
	}

	public HashMap<Integer, String> getCrossTypeMap() {
		return crossTypeMap;
	}

	public void setCrossTypeMap(HashMap<Integer, String> crossTypeMap) {
		this.crossTypeMap = crossTypeMap;
	}

	public HashMap<Integer, String> getBankBranchMap() {
		return bankBranchMap;
	}

	public void setBankBranchMap(HashMap<Integer, String> bankBranchMap) {
		this.bankBranchMap = bankBranchMap;
	}

}
