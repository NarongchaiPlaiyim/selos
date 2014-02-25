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
import com.clevel.selos.model.view.CustomerInfoPostAddressView;
import com.clevel.selos.model.view.CustomerInfoPostIndvView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name = "postCustomerInfoIndv")
public class PostCustomerInfoIndv implements Serializable {
	private static final long serialVersionUID = 7472195208984075946L;
	@Inject @SELOS
	private Logger log;
	@Inject @NormalMessage
	private Message message;
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
	private List<SelectItem> addressTypes;
		
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
	public String getAddressFlagLabel(int index) {
		if (index >= addressTypes.size())
			return message.get("app.custInfoIndi.content.button.other");
		SelectItem type = addressTypes.get(index);
		return type.getLabel();
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
	public List<SelectItem> getAddressTypes() {
		return addressTypes;
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
	
	public void handleBirthDay(SelectEvent event) {
		Date newDate = (Date) event.getObject();
		customer.setAge(Util.calAge(newDate));
	}
	public void onSelectAddressFlag(int index) {
		if (index == 0)
			return;
		CustomerInfoPostAddressView addressView = customer.getAddresses().get(index);
		if (addressView.getAddressFlag() != 3) {
			CustomerInfoPostAddressView toCopy = customer.getAddresses().get(addressView.getAddressFlag());
			addressView.duplicateData(toCopy);
		}
		_calculateDropdown(addressView);
	}
	public void onSelectProvince(int index) {
		CustomerInfoPostAddressView addressView = customer.getAddresses().get(index);
		addressView.setDistrictId(0);
		addressView.setSubDistrictId(0);
		addressView.setSubDistrictList(null);
		if (addressView.getProvinceId() > 0) {
			List<SelectItem> districts = generalPeopleInfoControl.listDistricts(addressView.getProvinceId());
			addressView.setDistrictList(districts);
		} else {
			addressView.setDistrictList(null);
		}
	}
	public void onSelectDistrict(int index) {
		CustomerInfoPostAddressView addressView = customer.getAddresses().get(index);
		addressView.setSubDistrictId(0);
		addressView.setSubDistrictList(null);
		if (addressView.getDistrictId() > 0) {
			List<SelectItem> subDistricts = generalPeopleInfoControl.listSubDistricts(addressView.getDistrictId());
			addressView.setSubDistrictList(subDistricts);
		} else {
			addressView.setSubDistrictList(null);
		}
	}
	
	public void onSaveCustomerInfo() {
		postCustomerInfoIndvControl.saveCustomerInfoIndividual(customer);
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	/*
	 * Private method
	 */
	private void _loadInitData() {
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		customer = postCustomerInfoIndvControl.getCustomer(customerId);
		
		if (canUpdateInfo && customer.getMaritalStatusId() >= 0)
			canUpdateSpouse = spouseMaritalSet.contains(customer.getMaritalStatusId());
		else
			canUpdateSpouse = false;
		_preCalculateDropdown();
	}
	private void _loadDropdown() {
		titles = generalPeopleInfoControl.listIndividualTitles(FacesUtil.getLanguage());
		races = generalPeopleInfoControl.listRaces();
		nationalities = generalPeopleInfoControl.listNationalities();
		provinces = generalPeopleInfoControl.listProvinces();
		maritalStatuses = generalPeopleInfoControl.listMaritalStatuses();
		countries = generalPeopleInfoControl.listCountries();
		businessTypes = generalPeopleInfoControl.listBusinessTypes();
		addressTypes = generalPeopleInfoControl.listIndividualAddressTypes();
		
		spouseMaritalSet = generalPeopleInfoControl.listSpouseReqMaritalStatues();
	}
	private void _preCalculateDropdown() {
		for (CustomerInfoPostAddressView address : customer.getAddresses()) {
			_calculateDropdown(address);
		}
	}
	private void _calculateDropdown(CustomerInfoPostAddressView address) {
		if (!(address.canUpdate() && canUpdateInfo)) {
			return;
		}
		if (address.getProvinceId() > 0) {
			address.setDistrictList(generalPeopleInfoControl.listDistricts(address.getProvinceId()));
			if (address.getDistrictId() > 0) {
				address.setSubDistrictList(generalPeopleInfoControl.listSubDistricts(address.getDistrictId()));
			} else {
				address.setSubDistrictId(0);
				address.setSubDistrictList(null);
			}
		} else {
			address.setDistrictId(0);
			address.setSubDistrictId(0);
			address.setDistrictList(null);
			address.setSubDistrictList(null);
		}
	}
}
