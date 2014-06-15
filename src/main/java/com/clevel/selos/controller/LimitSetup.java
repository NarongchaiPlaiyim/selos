package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

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
import com.clevel.selos.businesscontrol.LimitSetupControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.LimitSetupView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;


@ViewScoped
@ManagedBean(name="limitSetup")
public class LimitSetup implements Serializable {

	private static final long serialVersionUID = 2119248561303071111L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	LimitSetupControl limitSetupControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private LimitSetupView limitSetupView;
	private BasicInfoView basicInfoView;
	private User user;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	
	
	public LimitSetup() {
		
	}
	public Date getLastUpdateDateTime() {
		//TODO 
		return new Date();
	}
	public String getLastUpdateBy() {
		//TODO
		return user.getDisplayName();
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
		_loadInitData();
		if (workCaseId > 0){
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		this.setLimitSetupView(limitSetupControl.getLimitSetupView(workCaseId));
	}
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		log.info("preRender workCase Id = "+workCaseId);
		if (workCaseId > 0) {
			//TODO Validate step 
			if (stepId <= 0) {
				redirectPage = "/site/inbox.jsf";
			} else {
				return;
			}
		}
		try {
			log.info("preRender "+redirectPage);
			if (redirectPage == null) {
				redirectPage = "/site/inbox.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath()+redirectPage);
		} catch (IOException e) {
			log.error("Fail to redirect screen to "+redirectPage,e);
		}
	}
	
	public void onSaveLimitSetup() {
		this.limitSetupControl.saveLimitSetup(limitSetupView);
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	/*
	 * Private method
	 */
	private void _loadInitData() {
		preRenderCheck = false;
		
	}
	public LimitSetupView getLimitSetupView() {
		return limitSetupView;
	}
	public void setLimitSetupView(LimitSetupView limitSetupView) {
		this.limitSetupView = limitSetupView;
	}
	
	public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}
}
