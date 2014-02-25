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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.GeneralPeopleInfoControl;
import com.clevel.selos.businesscontrol.PostCustomerInfoJurisControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.CustomerInfoPostAddressView;
import com.clevel.selos.model.view.CustomerInfoPostJurisView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name = "postCustomerInfoJuris")
public class PostCustomerInfoJuris  implements Serializable {
	private static final long serialVersionUID = -3141808170140622081L;
	@Inject @SELOS
	private Logger log;
	@Inject @NormalMessage
	private Message message;
	@Inject
	private BasicInfoControl basicInfoControl;
	@Inject
	private PostCustomerInfoJurisControl postCustomerInfoJurisControl;
	@Inject
	private GeneralPeopleInfoControl generalPeopleInfoControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private BasicInfoView basicInfoView;
	private long customerId = -1;
	
	//Dropdown list
	private List<SelectItem> titles;
	private List<SelectItem> provinces;
	private List<SelectItem> countries;
	private List<SelectItem> addressTypes;
	
	//Property
	private CustomerInfoPostJurisView customer;
	private boolean canUpdateInfo;
	
	public PostCustomerInfoJuris() {
		
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
	public CustomerInfoPostJurisView getCustomer() {
		return customer;
	}
	public boolean isCanUpdateInfo() {
		return canUpdateInfo;
	}
	public String getAddressFlagLabel(int index) {
		if (index >= addressTypes.size())
			return message.get("app.custInfoJuri.content.button.other");
		SelectItem type = addressTypes.get(index);
		return type.getLabel();
	}
	public List<SelectItem> getTitles() {
		return titles;
	}
	public List<SelectItem> getProvinces() {
		return provinces;
	}
	public List<SelectItem> getCountries() {
		return countries;
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
		postCustomerInfoJurisControl.saveCustomerInfoJuristic(customer);
		
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
		customer = postCustomerInfoJurisControl.getCustomer(customerId);
		
		_preCalculateDropdown();
	}
	private void _loadDropdown() {
		titles = generalPeopleInfoControl.listJuristicTitles(FacesUtil.getLanguage());
		provinces = generalPeopleInfoControl.listProvinces();
		countries = generalPeopleInfoControl.listCountries();
		addressTypes = generalPeopleInfoControl.listJuristicAddressTypes();
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
