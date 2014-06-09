package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.GuarantorDetailControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.model.view.GuarantorInfoFullView;
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
import java.util.*;

@ViewScoped
@ManagedBean(name = "guarantorDetail")
public class GuarantorDetail implements Serializable {
	private static final long serialVersionUID = -7897839845100860116L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	@Inject
	private GuarantorDetailControl guarantorDetailControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private long stageId = -1;
	private long guarantorId = -1;
	private BasicInfoView basicInfoView;
	
	//Property
	private GuarantorInfoFullView guarantorInfoView;

    public GuarantorDetail(){
    	
    }
    public Date getLastUpdateDateTime() {
    	return guarantorInfoView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (guarantorInfoView.getModifyBy() != null)
			return guarantorInfoView.getModifyBy().getDisplayName();
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
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy",new Locale("th", "TH"));
		return dFmt.format(new Date());
	}
	
	public GuarantorInfoFullView getGuarantorInfoView() {
		return guarantorInfoView;
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
			stageId = Util.parseLong(session.getAttribute("stageId"), -1);
		}
		Map<String,Object> params =  FacesUtil.getParamMapFromFlash("guarantorParams");
		guarantorId = Util.parseLong(params.get("guarantorId"),-1);
		_loadFieldControl();
		_loadInitData();
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		if (workCaseId > 0) {
			if (stepId <= 0 || stageId != 301) {
				redirectPage = "/site/inbox.jsf";
			} else {
				if (guarantorId <= 0) {
					redirectPage = "/site/mortgageSummary.jsf";
				} else {
					return;
				}
			}
		}
		
		try {
			if (redirectPage == null) {
				redirectPage = "/site/inbox.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			if (!ec.isResponseCommitted())
				ec.redirect(ec.getRequestContextPath()+redirectPage);
		} catch (IOException e) {
			log.error("Fail to redirect screen to "+redirectPage,e);
		}
	}
	
	
	public String clickCustomerInfo(long id,boolean juristic) {
		FacesUtil.getFlash().put("customerId", id);
		if (juristic) {
			return "postCustomerInfoJuris?faces-redirect=true";
		} else {
			// Individual
			return "postCustomerInfoIndv?faces-redirect=true";
		}
	}
	
	public void onSaveGuarantorDetail() {
		guarantorDetailControl.saveGuarantorDetail(guarantorInfoView);
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onCancelGuarantor() {
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
		
		guarantorInfoView = guarantorDetailControl.getGuarantorInfoFull(guarantorId);
		if (guarantorInfoView.getId() <= 0 || guarantorInfoView.getWorkCaseId() != workCaseId) {
			String redirectPage = "/site/mortgageSummary.jsf";
			try {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				if (!ec.isResponseCommitted())
					ec.redirect(ec.getRequestContextPath()+redirectPage);
			} catch (IOException e) {
				log.error("Fail to redirect screen to "+redirectPage,e);
			}
		}
	}
	
	/*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.GuarantorDetail);
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
