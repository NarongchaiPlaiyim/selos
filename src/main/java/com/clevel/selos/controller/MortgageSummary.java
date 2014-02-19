package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.MortgageSummaryControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.MortgageSignLocationType;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.GuarantorInfoView;
import com.clevel.selos.model.view.MortgageInfoView;
import com.clevel.selos.model.view.MortgageSummaryView;
import com.clevel.selos.model.view.PledgeInfoView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

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
	public String getMinDate() {
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy",new Locale("th", "TH"));
		return dFmt.format(new Date());
	}
	
	public List<SelectItem> getLocations() {
		return locations;
	}
	public MortgageSummaryView getMortgageSummaryView() {
		return mortgageSummaryView;
	}
	public boolean isEnableSignContractLocation() {
		return ! MortgageSignLocationType.NA.equals(mortgageSummaryView.getSigningLocation());
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
		
		_loadInitData();
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		if (workCaseId > 0) {
			//TODO Validate step 
			if (stepId <= 0) {
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
		mortgageSummaryView.setUpdLocation(0);
		switch(mortgageSummaryView.getSigningLocation()) {
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
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public String clickMorgageDetail(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mortgageId", id);
		map.put("fromPage", "mortgageSummary");
		FacesUtil.getFlash().put("mortgageParams",map);
		return "mortgageDetail?faces-redirect=true";
	}
	public String clickPledgeDetail(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pledgeId", id);
		map.put("fromPage", "mortgageSummary");
		FacesUtil.getFlash().put("pledgeParams",map);
		return "pledgeDetail?faces-redirect=true";
	}
	public String clickGuarantorDetail(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("guarantorId", id);
		map.put("fromPage", "mortgageSummary");
		FacesUtil.getFlash().put("guarantorParams",map);
		return "guarantorDetail?faces-redirect=true";
	}
	/*
	 * Private method
	 */
	private void _loadInitData() {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		
		mortgageSummaryControl.calculateMortgageSummary(workCaseId);
		
		mortgageSummaryView = mortgageSummaryControl.getMortgageSummaryView(workCaseId);
		mortgageInfos = mortgageSummaryControl.getMortgageInfoList(workCaseId);
		pledgeInfos = mortgageSummaryControl.getPledgeInfoList(workCaseId);
		guarantorInfos = mortgageSummaryControl.getGuarantorInfoList(workCaseId);
	}
}
