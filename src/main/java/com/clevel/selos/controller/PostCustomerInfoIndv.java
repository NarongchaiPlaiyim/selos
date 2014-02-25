package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.GeneralPeopleInfoControl;
import com.clevel.selos.businesscontrol.PostCustomerInfoIndvControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.CustomerInfoPostIndvView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name = "postCustomerInfoIndv")
public class PostCustomerInfoIndv implements Serializable {
	private static final long serialVersionUID = 7472195208984075946L;
	@Inject @SELOS
	private Logger log;
	@Inject
	private BasicInfoControl basicInfoControl;
	@Inject
	private PostCustomerInfoIndvControl postCustomerInfoIndvControl;
	@Inject
	private GeneralPeopleInfoControl generalPeopleInfoControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private BasicInfoView basicInfoView;
	private long customerId = -1;
	private Set<Integer> spouseMaritalSet;
	
	//Dropdown list
	private List<SelectItem> titles;
	private List<SelectItem> races;
	private List<SelectItem> nationalities;
	private List<SelectItem> provinces;
	private List<SelectItem> maritalStatuses;
	private List<SelectItem> countries;
	private List<SelectItem> businessTypes;
	
	
	
	private List<SelectItem> districts;
	private List<SelectItem> subdistricts;
		
	//Property
	private CustomerInfoPostIndvView customer;
	private boolean canUpdateInfo;
	private boolean canUpdateSpouse;
	
	public PostCustomerInfoIndv() {
	}
	public Date getLastUpdateDateTime() {
		if (basicInfoView == null)
			return null;
		else
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
	public String getCurrentDate() {
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy",new Locale("th", "TH"));
		return dFmt.format(new Date());
	}
	public String getAgeYearRange() {
		Calendar calendar = Calendar.getInstance(new Locale("th","TH"));
		return "2450:"+calendar.get(Calendar.YEAR);
	}
	public CustomerInfoPostIndvView getCustomer() {
		return customer;
	}
	public boolean isCanUpdateInfo() {
		return canUpdateInfo;
	}
	public boolean isCanUpdateSpouse() {
		return canUpdateSpouse;
	}
	public boolean isRequiredBusinessType() {
		return customer.getRelationId() == 1 || customer.getRelationId() == 2; 
	}
	
	public List<SelectItem> getTitles() {
		return titles;
	}
	public List<SelectItem> getRaces() {
		return races;
	}
	public List<SelectItem> getNationalities() {
		return nationalities;
	}
	public List<SelectItem> getProvinces() {
		return provinces;
	}
	public List<SelectItem> getMaritalStatuses() {
		return maritalStatuses;
	}
	public List<SelectItem> getCountries() {
		return countries;
	}
	
	public List<SelectItem> getBusinessTypes() {
		return businessTypes;
	}
	
	
	/*
	 * Action
	 */
	@PostConstruct
	private void init() {
		
		log.info("POSSSSS");
		HttpSession session = FacesUtil.getSession(false);
		if (session != null) {
			workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
			stepId = Util.parseLong(session.getAttribute("stepId"), -1);
		}
		
		customerId = Util.parseLong(FacesUtil.getFlash().get("customerId"),-1L);
		
		canUpdateInfo = true; //TODO
		
		_loadDropdown();
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
				if (customerId <= 0) {
					redirectPage = "/site/postCustomerInfoSummary.jsf";
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
			ec.redirect(ec.getRequestContextPath()+redirectPage);
		} catch (IOException e) {
			log.error("Fail to redirect screen to "+redirectPage,e);
		}
	}
	public void onChangeMaritalStatus() {
		if (canUpdateInfo && customer.getMaritalStatusId() >= 0)
			canUpdateSpouse = spouseMaritalSet.contains(customer.getMaritalStatusId());
		else
			canUpdateSpouse = false;
		log.info("onChangeMaritalStatus DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD  "+canUpdateSpouse);
	}
	public void handleBirthDay(SelectEvent event) {
		Date newDate = (Date) event.getObject();
		customer.setAge(Util.calAge(newDate));
	}
	public void onSaveCustomerInfo() {
		log.info("XXXXXXXX Calling on Save Customer Info");
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	/*
	 * Private method
	 */
	private void _loadInitData() {
		log.info("Initial DATA DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		customer = postCustomerInfoIndvControl.getCustomer(customerId);
		onChangeMaritalStatus();
	}
	private void _loadDropdown() {
		titles = generalPeopleInfoControl.listIndividualTitles(FacesUtil.getLanguage());
		races = generalPeopleInfoControl.listRaces();
		nationalities = generalPeopleInfoControl.listNationalities();
		provinces = generalPeopleInfoControl.listProvinces();
		maritalStatuses = generalPeopleInfoControl.listMaritalStatuses();
		countries = generalPeopleInfoControl.listCountries();
		businessTypes = generalPeopleInfoControl.listBusinessTypes();
		
		spouseMaritalSet = generalPeopleInfoControl.listSpouseReqMaritalStatues();
	}
}
