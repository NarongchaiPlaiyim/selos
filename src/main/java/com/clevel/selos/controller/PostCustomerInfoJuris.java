package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.GeneralPeopleInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.PostCustomerInfoJurisControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
	private long stageId = -1;
	private BasicInfoView basicInfoView;
	
	private long customerId = -1;
	private boolean fromSubScreen = false;
	private long fromCustomerId = -1;
	private boolean fromJuristic = false;
	
	//Dropdown list
	private List<SelectItem> titles;
	private List<SelectItem> provinces;
	private List<SelectItem> countries;
	private List<SelectItem> addressTypes;
	
	//Property
	private CustomerInfoPostJurisView customer;
	private List<CustomerInfoView> committees;
	private CustomerInfoView selectedCustomer;
	private boolean canUpdateInfo;
	
	public PostCustomerInfoJuris() {
		
	}
	public Date getLastUpdateDateTime() {
		return customer.getModifyDate();
	}
	public String getLastUpdateBy() {
		return customer.getModifyUser();
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
	public List<CustomerInfoView> getCommittees() {
		return committees;
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
		
		customerId = Util.parseLong(FacesUtil.getFlash().get("customerId"),-1L);
		fromSubScreen = "true".equals(FacesUtil.getFlash().get("customer_fromsub"));
		fromCustomerId = Util.parseLong(FacesUtil.getFlash().get("customer_fromid"),-1L);
		fromJuristic = "true".equals(FacesUtil.getFlash().get("customer_fromjuris"));
		
		canUpdateInfo = true; //TODO
		_loadFieldControl();
		_loadDropdown();
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
			if (!ec.isResponseCommitted())
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
	public String onClickLink() {
		if (selectedCustomer == null)
			return "";
		FacesUtil.getFlash().put("customerId", selectedCustomer.getId());
		FacesUtil.getFlash().put("customer_fromsub", "true");
		FacesUtil.getFlash().put("customer_fromjuris", "true");
		FacesUtil.getFlash().put("customer_fromid",customer.getId());
		if (selectedCustomer.getCustomerEntity().getId() == 1) {
			// Individual
			return "postCustomerInfoIndv?faces-redirect=true";
		} else {
			return "postCustomerInfoJuris?faces-redirect=true";
		}
	}
	
	
	public void onSaveCustomerInfo() {
		postCustomerInfoJurisControl.saveCustomerInfoJuristic(customer);
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public String onCancelCustomerInfo() {
		if (fromSubScreen && fromCustomerId > 0) {
			FacesUtil.getFlash().put("customerId", fromCustomerId);
			FacesUtil.getFlash().put("customer_fromsub", "false");
			if (fromJuristic) {
				return "postCustomerInfoJuris?faces-redirect=true";
			} else {
				// Individual
				return "postCustomerInfoIndv?faces-redirect=true";
			}
		}
		return "postCustomerInfoSummary?faces-redirect=true";
	}
	
	/*
	 * Private method
	 */
	private void _loadInitData() {
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		customer = postCustomerInfoJurisControl.getCustomer(customerId);
		
		boolean back = false;
		if (customer.getJuristicId() <= 0) {
			back = true;
		} else {
			if (customer.getWorkCaseId() != workCaseId) {
				if (fromSubScreen) {
					log.info("Request from another post customer screen but workcase id this isn't match then mark as read only [CustomerId=" + customerId+", workcase="+customer.getWorkCaseId()+"]");
					canUpdateInfo = false;
				} else {
					back = true;
				}
			}
		}
			
		if (back) {
			String redirectPage = "/site/postCustomerInfoSummary.jsf";
			try {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				if (!ec.isResponseCommitted())
					ec.redirect(ec.getRequestContextPath()+redirectPage);
			} catch (IOException e) {
				log.error("Fail to redirect screen to "+redirectPage,e);
			}
		}
		
		committees = postCustomerInfoJurisControl.getCustomerCommittees(customerId);
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
	/*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.PostCustomerInfoJuris);
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
	
	public String mandateAddress(CustomerInfoPostAddressView address,String name) {
		int type = address.getAddressType();
		String prefix = "registration.";
		if (type == 5) {
			prefix = "bizlocation.";
		}
		return mandate(prefix+name);	
	}
	public boolean isAddressDisabled(CustomerInfoPostAddressView address,String name) {
		int type = address.getAddressType();
		String prefix = "registration.";
		if (type == 5) {
			prefix = "bizlocation.";
		}
		return isDisabled(prefix+name) || !address.canUpdate();	
	}
}
