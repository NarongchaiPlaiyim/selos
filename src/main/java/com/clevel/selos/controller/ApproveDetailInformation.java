package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.ApproveDetailInformationControl;
import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.ApproveDetailInformationView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ViewScoped
@ManagedBean(name = "approveDetailInformation")
public class ApproveDetailInformation implements Serializable {
	private static final long serialVersionUID = 5055575396070904261L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	
	@Inject
	private ApproveDetailInformationControl approveDetailInformationControl;
	@Inject
	private UserAccessControl userAccessControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private User user;
	private BasicInfoView basicInfoView;
	private ApproveDetailInformationView approveDetailInformationView;
	//Property
	
	public ApproveDetailInformation() {
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
	public void onCreation() {
		HttpSession session = FacesUtil.getSession(false);
		if (session != null) {
			log.info("workCaseId: " + workCaseId + ", stepId: " + stepId);
			workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
			stepId = Util.parseLong(session.getAttribute("stepId"), -1);
			user = (User) session.getAttribute("user");
			log.info("workCaseId: " + workCaseId + ", stepId: " + stepId);
			//workCaseId = 281;
			this.setApproveDetailInformationView(approveDetailInformationControl.getApproveDetailInformationView(workCaseId));
			log.info("init ::: workCaseId is " + workCaseId);
		} else {
			log.info("init ::: workCaseId is null.");
			try {
				FacesUtil.redirect("/site/inbox.jsf");
				return;
			} catch (Exception ex) {
				log.info("Exception :: {}", ex);
			}
		}
		_loadInitData();
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		log.info("preRender workCase Id = "+workCaseId);
		if (workCaseId > 0) {
			if (!userAccessControl.canUserAccess(Screen.ApproveDetailInfo, stepId)) {
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
	
	public void onSaveApproveDetail() {
		log.info("onSaveApproveDetail: " + this.approveDetailInformationView.getPayDate() + "," + this.approveDetailInformationView.getFirstPaymentDate());
		this.approveDetailInformationControl.saveApproveDetailInformationView(approveDetailInformationView, workCaseId);
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
		
	}

	public ApproveDetailInformationView getApproveDetailInformationView() {
		return approveDetailInformationView;
	}

	public void setApproveDetailInformationView(ApproveDetailInformationView approveDetailInformationView) {
		this.approveDetailInformationView = approveDetailInformationView;
	}
}
