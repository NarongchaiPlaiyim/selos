package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import com.clevel.selos.businesscontrol.PledgeConfirmControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.PledgeInfoView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name="pledgeConfirm")
public class PledgeConfirm implements Serializable {
	private static final long serialVersionUID = -4174470383570669219L;
	@Inject @SELOS
	private Logger log;
	@Inject
	private BasicInfoControl basicInfoControl;
	
	@Inject
	private PledgeConfirmControl pledgeConfirmControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	
	private BasicInfoView basicInfoView;
	private Date lastUpdateDate;
	private String lastUpdateBy;
	//Property
	private List<PledgeInfoView> pledges;
	
	public PledgeConfirm() {
		
	}
	public Date getLastUpdateDateTime() {
		return lastUpdateDate;
	}
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}
	public List<PledgeInfoView> getPledges() {
		return pledges;
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
	
	public void onSavePledgeConfirm() {
		pledgeConfirmControl.savePledgeConfirm(pledges);
		
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
		pledges = pledgeConfirmControl.getPledgeInfoList(workCaseId);
		if (pledges == null || pledges.isEmpty()) {
			lastUpdateBy = null;
			lastUpdateDate = null;
		} else {
			Date checkDate = new Date(0);
			for (PledgeInfoView view : pledges) {
				if (view.getModifyDate() == null) 
					continue;
				
				if (view.getModifyDate().after(checkDate)) {
					checkDate = view.getModifyDate();
					lastUpdateDate = view.getModifyDate();
					if (view.getModifyBy() != null)
						lastUpdateBy = view.getModifyBy().getDisplayName();
					else
						lastUpdateBy = null;
				}
				
			}
		}
	}
}

