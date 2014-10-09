package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.MortgageSummaryControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.MortgageSignLocationType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@ViewScoped
@ManagedBean(name = "mortgageSummary")
public class MortgageSummary implements Serializable {
	private static final long serialVersionUID = 3803192687188180318L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	
	@Inject
	private MortgageSummaryControl mortgageSummaryControl;
	@Inject
	private UserAccessControl userAccessControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private BasicInfoView basicInfoView;
	private List<SelectItem> branches;
	private List<SelectItem> zones; 
	
	//Property
	private List<SelectItem> locations;
	private MortgageSummaryView mortgageSummaryView;
	private AgreementInfoView agreementInfoView;
	private List<MortgageInfoView> mortgageInfos;
	private List<PledgeInfoView> pledgeInfos;
	private List<GuarantorInfoView> guarantorInfos;
	
	public MortgageSummary() {
		
	}
	public Date getLastUpdateDateTime() {
		return mortgageSummaryView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (mortgageSummaryView.getModifyBy() != null ) 
			return mortgageSummaryView.getModifyBy().getDisplayName();
		else
			return "";
	}
	public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}
	public void setApproveType(ApproveType type) {
		//DO NOTHING
	}
	public String getMinDate() {
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy");
		return dFmt.format(new Date());
	}
	
	public List<SelectItem> getLocations() {
		return locations;
	}
	public MortgageSummaryView getMortgageSummaryView() {
		return mortgageSummaryView;
	}
	public AgreementInfoView getAgreementInfoView() {
		return agreementInfoView;
	}
	public boolean isEnableSignContractLocation() {
		return !MortgageSignLocationType.NA.equals(agreementInfoView.getSigningLocation()) && !isDisabled("signContract") && !isDisabled("signDetail");
	}
	public List<MortgageInfoView> getMortgageInfos() {
		return mortgageInfos;
	}
	public List<PledgeInfoView> getPledgeInfos() {
		return pledgeInfos;
	}
	public List<GuarantorInfoView> getGuarantorInfos() {
		return guarantorInfos;
	}
	
	
	/*
	 * Action
	 */
	@PostConstruct
	private void init() {
		log.info("Construct");
		HttpSession session = FacesUtil.getSession(false);
		if (session != null) {
			workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
			stepId = Util.parseLong(session.getAttribute("stepId"), -1);
		}
		
		branches = mortgageSummaryControl.listBankBranches();
		zones = mortgageSummaryControl.listUserZones();
		_loadFieldControl();
		_loadInitData(false);
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		if (workCaseId > 0) {
			if (!userAccessControl.canUserAccess(Screen.CollateralMortgageInfoSum, stepId)) {
				redirectPage = "/site/inbox.jsf";
			} else {
				return;
			}
		}
		try {
			if (redirectPage == null) {
				redirectPage = "/site/inbox.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath()+redirectPage);
		} catch (IOException e) {
			log.error("Fail to redirect screen to "+redirectPage,e);
		}
	}
	public void onSelectSignContract() {
		agreementInfoView.setUpdLocation(0);
		switch(agreementInfoView.getSigningLocation()) {
			case BRANCH :
				locations = branches;
				break;
			case ZONE :
				locations = zones;
				break;
			default:
				locations = Collections.emptyList();
				break;
		}
	}
	
	public void onSaveMortgageSummary() {
		mortgageSummaryControl.saveMortgageSummary(mortgageSummaryView, agreementInfoView, workCaseId);
		
		_loadInitData(true);
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void clickMorgageDetail(long id) {
        log.debug("clickMorgageDetail (id : {})",id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mortgageId", id);
		map.put("fromPage", "mortgageSummary");
		FacesUtil.getFlash().put("mortgageParams",map);
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("mortgageParams", map);
        String redirectPage = "/site/mortgageDetail.jsf";
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath()+redirectPage);
        } catch (IOException e) {
            log.error("Fail to redirect screen to "+redirectPage,e);
        }
		//return "mortgageDetail?faces-redirect=true";
	}
	public void clickPledgeDetail(long id) {
        log.debug("clickPledgeDetail (id : {})",id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pledgeId", id);
		map.put("fromPage", "mortgageSummary");
		FacesUtil.getFlash().put("pledgeParams",map);
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("pledgeParams", map);
        String redirectPage = "/site/pledgeDetail.jsf";
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath()+redirectPage);
        } catch (IOException e) {
            log.error("Fail to redirect screen to "+redirectPage,e);
        }
		//return "pledgeDetail?faces-redirect=true";
	}
	public void clickGuarantorDetail(long id) {
        log.debug("clickGuarantorDetail (id : {})",id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("guarantorId", id);
		map.put("fromPage", "mortgageSummary");
		FacesUtil.getFlash().put("guarantorParams",map);
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("guarantorParams", map);
        String redirectPage = "/site/guarantorDetail.jsf";
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath()+redirectPage);
        } catch (IOException e) {
            log.error("Fail to redirect screen to "+redirectPage,e);
        }
		//return "guarantorDetail?faces-redirect=true";
	}
	public void onCancelMortgageSummary() {
		_loadInitData(true);
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	/*
	 * Private method
	 */
	private void _loadInitData(boolean ignoreRecalculate) {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		mortgageSummaryView = mortgageSummaryControl.getMortgageSummaryView(workCaseId);
		if (!ignoreRecalculate) {
			if ("true".equals(FacesUtil.getParameter("force")) || mortgageSummaryView.getId() <= 0) {
				mortgageSummaryView = mortgageSummaryControl.calculateMortgageSummary(workCaseId);
			}
		}
		agreementInfoView = mortgageSummaryControl.getAgreementInfoView(workCaseId);
		
		mortgageInfos = mortgageSummaryControl.getMortgageInfoList(workCaseId);
		pledgeInfos = mortgageSummaryControl.getPledgeInfoList(workCaseId);
		guarantorInfos = mortgageSummaryControl.getGuarantorInfoList(workCaseId);
		
		//init agreement 
		switch(agreementInfoView.getSigningLocation()) {
			case BRANCH :
				locations = branches;
				break;
			case ZONE :
				locations = zones;
				break;
			default:
				locations = Collections.emptyList();
				break;
		}
		
	}
	
	/*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
        HttpSession session = FacesUtil.getSession(false);
        String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.CollateralMortgageInfoSum, ownerCaseUserId);
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
