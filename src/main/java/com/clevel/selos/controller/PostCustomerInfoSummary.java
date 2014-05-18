package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.GeneralPeopleInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "postCustomerInfoSummary")
public class PostCustomerInfoSummary implements Serializable {
	private static final long serialVersionUID = -1822834368060726560L;
	@Inject @SELOS
	private Logger log;
	@Inject
	private BasicInfoControl basicInfoControl;
	@Inject
	private CustomerInfoControl customerInfoControl;
	@Inject
	private GeneralPeopleInfoControl generalPeopleInfoControl;
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private long stageId = -1;
	private BasicInfoView basicInfoView;
	private LastUpdateDataView lastUpdateView;
	
	
	//Property
	private List<PostCustomerInfoGroupView> groups;
	private CustomerInfoView selectedCustomer;
	
	public PostCustomerInfoSummary() {
	}
	public Date getLastUpdateDateTime() {
		return lastUpdateView.getModifyDate();
	}
	public String getLastUpdateBy() {
		return lastUpdateView.getModifyUser();
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
	public List<PostCustomerInfoGroupView> getGroups() {
		return groups;
	}
	public CustomerInfoView getSelectedCustomer() {
		return selectedCustomer;
	}
	public void setSelectedCustomer(CustomerInfoView selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
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
		_loadFieldControl();
		_loadInitData();
	}
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		if (workCaseId > 0) {
			if (stepId <= 0 || !(stageId >= 300 && stageId < 400)) {
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
	public String onClickLink() {
		if (selectedCustomer == null)
			return "";
		FacesUtil.getFlash().put("customerId", selectedCustomer.getId());
		if (selectedCustomer.getCustomerEntity().getId() == 1) {
			// Individual
			return "postCustomerInfoIndv?faces-redirect=true";
		} else {
			return "postCustomerInfoJuris?faces-redirect=true";
		}
	}
	
	/*
	 * Private method
	 */
	private void _loadInitData() {
		
		CustomerInfoSummaryView summary = null;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
			summary = customerInfoControl.getCustomerInfoSummary(workCaseId);
		}
		lastUpdateView = generalPeopleInfoControl.getWorkCaseLastUpdate(workCaseId);
		
		groups = new ArrayList<PostCustomerInfoGroupView>();
		PostCustomerInfoGroupView borrowerGroup = new PostCustomerInfoGroupView();
		borrowerGroup.setTitleKey("app.custInfoSummary.content.borrower");
		groups.add(borrowerGroup);
		
		PostCustomerInfoGroupView guarantorGroup = new PostCustomerInfoGroupView();
		guarantorGroup.setTitleKey("app.custInfoSummary.content.guarantor");
		groups.add(guarantorGroup);
		
		PostCustomerInfoGroupView relatedGroup = new PostCustomerInfoGroupView();
		relatedGroup.setTitleKey("app.custInfoSummary.content.related");
		groups.add(relatedGroup);
		
		
		if (summary != null) {
			borrowerGroup.setCustomers(summary.getBorrowerCustomerViewList());
			guarantorGroup.setCustomers(summary.getGuarantorCustomerViewList());
			relatedGroup.setCustomers(summary.getRelatedCustomerViewList());
		}
	}
	
	/*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.PostCustomerInfoSum);
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
	public boolean isDisabledGroup(PostCustomerInfoGroupView group,String name) {
		String prefix = "borrower.";
		if (group.getTitleKey().equals("app.custInfoSummary.content.related")) {
			prefix = "related.";
		} else if (group.getTitleKey().equals("app.custInfoSummary.content.guarantor")) {
			prefix = "guarantor.";
		} 
		return isDisabled(prefix+name);
	}
}
