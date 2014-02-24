package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.PostCustomerInfoGroupView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

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
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private BasicInfoView basicInfoView;
	
	//Property
	private List<PostCustomerInfoGroupView> groups;
	private CustomerInfoView selectedCustomer;
	
	public PostCustomerInfoSummary() {
	}
	public Date getLastUpdateDateTime() {
		return basicInfoView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (basicInfoView != null && basicInfoView.getModifyBy() != null)
			return basicInfoView.getModifyBy().getDisplayName();
		else
			return null;
	}
	public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
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
	public String onClickLink() {
		if (selectedCustomer == null)
			return "";
		FacesUtil.getFlash().put("customerId", selectedCustomer.getId());
		if (selectedCustomer.getCustomerEntity().getId() == 1) {
			// Individual
			return "postCustomerInfoIndividual?faces-redirect=true";
		} else {
			return "postCustomerInfoJuristic?faces-redirect=true";
		}
	}
	
	/*
	 * Private method
	 */
	private void _loadInitData() {
		preRenderCheck = false;
		CustomerInfoSummaryView summary = null;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
			summary = customerInfoControl.getCustomerInfoSummary(workCaseId);
		}
		
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
}
