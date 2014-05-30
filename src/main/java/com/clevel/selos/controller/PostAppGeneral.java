package com.clevel.selos.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.GeneralPeopleInfoControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name = "postAppGeneral")
public class PostAppGeneral implements Serializable  {
	private static final long serialVersionUID = 8040267511396703345L;
	
	@Inject private GeneralPeopleInfoControl generalPeopleInfoControl;
	@Inject @NormalMessage
	private Message message;
	@Inject @SELOS
	private Logger log;
	private long workCaseId = -1;
	private long stepId = -1;
	private long stageId = -1;
	
	//Submit02 Dialog value
	private String submit02_Remark;
	
	//Return 01 Dialog
	private String return01_Remark;
	private int return01_SelectedReasonId;
	
	//Cancel01 Dialog
	private String cancel01_Remark;
	private int cancel01_SelectedReasonId;
	
	private List<SelectItem> returnReasonList;
	private List<SelectItem> cancelReasonList;
	
	public String getCancel01_Remark() {
		return cancel01_Remark;
	}
	public void setCancel01_Remark(String cancel01_Remark) {
		this.cancel01_Remark = cancel01_Remark;
	}
	public String getReturn01_Remark() {
		return return01_Remark;
	}
	public void setReturn01_Remark(String return01_Remark) {
		this.return01_Remark = return01_Remark;
	}
	public void setReturn01_SelectedReasonId(int return01_SelectedReasonId) {
		this.return01_SelectedReasonId = return01_SelectedReasonId;
	}
	public int getReturn01_SelectedReasonId() {
		return return01_SelectedReasonId;
	}
	public String getSubmit02_Remark() {
		return submit02_Remark;
	}
	public void setSubmit02_Remark(String submit02_Remark) {
		this.submit02_Remark = submit02_Remark;
	}
	public int getCancel01_SelectedReasonId() {
		return cancel01_SelectedReasonId;
	}
	public void setCancel01_SelectedReasonId(int cancel01_SelectedReasonId) {
		this.cancel01_SelectedReasonId = cancel01_SelectedReasonId;
	}
	
	public List<SelectItem> getCancelReasonList() {
		return cancelReasonList;
	}
	public List<SelectItem> getReturnReasonList() {
		return returnReasonList;
	}
	/*
	 * Action
	 */
	@PostConstruct
	private void init() {
		HttpSession session = FacesUtil.getSession(false);
		if (session != null) {
			workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
			stepId = Util.parseLong(session.getAttribute("stepId"), -1);
			stageId = Util.parseLong(session.getAttribute("stageId"), -1);
		}
		returnReasonList = generalPeopleInfoControl.listReturnReasons();
		cancelReasonList = generalPeopleInfoControl.listCancelReasons();
	}
	/*
	 * 
	 */
	public boolean isPostAppStage() {
		return stepId / 1000 == 3; // 3xxx
	}
	
	public void onSubmitCase() {
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
		FacesUtil.redirect("/site/inbox_dev.jsf");
	}
	public void onSubmitCaseFail() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"TEST Error : Error","TEST Error : Error");
		FacesContext.getCurrentInstance().addMessage(null,msg); 
	}
	
	
	
}
