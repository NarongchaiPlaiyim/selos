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

import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name="baInfo")
public class BAInfo implements Serializable {
	private static final long serialVersionUID = -6678163555513002049L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private User user;
	private BasicInfoView basicInfoView;
	
	//Property
	
	
	public BAInfo() {
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
	
	/*
	 * Private method
	 */
	private void _loadInitData() {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
	}
}
