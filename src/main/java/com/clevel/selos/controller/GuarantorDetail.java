package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name = "guarantorDetail")
public class GuarantorDetail implements Serializable {
	private static final long serialVersionUID = -7897839845100860116L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;

	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private long guarantorId = -1;
	private String fromPage;
	private User user;
	private BasicInfoView basicInfoView;
	
	//Property

    public GuarantorDetail(){
    	
    }
    public Date getLastUpdateDateTime() {
		//TODO 
		return new Date();
	}
	public String getLastUpdateBy() {
		//TODO
		return user.getDisplayName();
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
			user = (User) session.getAttribute("user");
		}
		Map<String,Object> params =  FacesUtil.getParamMapFromFlash("guarantorParams");
		fromPage = (String)params.get("fromPage");
		guarantorId = Util.parseLong(params.get("guarantorId"),-1);
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
	
	
	public String clickCustomerInfo(long id) {
		//TODO Clear what it is
		Map<String, Object> map = new HashMap<String, Object>();
		/*
        map.put("isFromSummaryParam",true);
        map.put("isFromJuristicParam",false);
        map.put("isFromIndividualParam",false);
        map.put("isEditFromJuristic", false);
        CustomerInfoView cusView = new CustomerInfoView();
        cusView.reset();
        map.put("customerInfoView", cusView);
        */
        map.put("customerId", id);
		return "customerInfoIndividual?faces-redirect=true";
	}
	public void onSaveGuarantorDetail() {
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	/*
	 * Private method
	 */
	private void _loadInitData() {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		//TODO Load guarantor info by using workcase and guarantorId
		
	}
}
