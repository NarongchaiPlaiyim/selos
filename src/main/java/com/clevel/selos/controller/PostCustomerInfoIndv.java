package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.GeneralPeopleInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.PostCustomerInfoIndvControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SelectableDataModel;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

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
	@Inject
	private UserAccessControl userAccessControl;
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	
	private BasicInfoView basicInfoView;
	
	private long customerId = -1;
	private boolean fromSubScreen = false;
	private long fromCustomerId = -1;
	private boolean fromJuristic = false;
	
	private Set<Integer> spouseMaritalSet;
	private List<CustomerAttorneySelectView> attorneySelectViews;
	private CustomerAttorneyView currentAttorneyView;
	
	//Dropdown list
	private List<SelectItem> titles;
	private List<SelectItem> races;
	private List<SelectItem> nationalities;
	private List<SelectItem> provinces;
	private List<SelectItem> maritalStatuses;
	private List<SelectItem> countries;
	private List<SelectItem> businessTypes;
	private List<SelectItem> addressTypes;
	
	private List<SelectItem> attorneyDistricts;
	private List<SelectItem> attorneySubdistricts;
	
	//Property
	private CustomerInfoPostIndvView customer;
	private boolean canUpdateInfo;
	private boolean canUpdateSpouse;
	private boolean attorneyDetailEditable;
	private AttorneySelectDataModel attorneySelectDataModel;
	private CustomerAttorneySelectView selectedAttorney;
	private CustomerAttorneyView attorneyView;
	
	public PostCustomerInfoIndv() {
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
	public String getCurrentDate() {
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy");
		return dFmt.format(new Date());
	}
	public String getAgeYearRange() {
		Calendar calendar = Calendar.getInstance();
		return "1950:"+calendar.get(Calendar.YEAR);
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
	
	public String getAddressFlagLabel(int index) {
        log.debug("getAddressFlagLabel (index : {})",index);
		if (index >= addressTypes.size()-1)
			return message.get("app.custInfoIndi.content.button.other");
		SelectItem type = addressTypes.get(index);
        log.debug("getAddressFlagLabel (type.getLabel() : {}, type.getValue() : {})",type.getLabel(),type.getValue());
		return type.getLabel();
	}
	public boolean isAttorneyDetailEditable() {
		return attorneyDetailEditable;
	}
	public boolean isAppointeeOwnerSelectable() {
		return isEnableAttorneyRight() && AttorneyRelationType.BORROWER.equals(customer.getAttorneyRelationType());
	}
	public boolean isEnableAttorneyRight() {
		return RadioValue.YES.equals(customer.getAttorneyRequired());
	}
	public AttorneySelectDataModel getAttorneySelectDataModel() {
		return attorneySelectDataModel;
	}
	public CustomerAttorneySelectView getSelectedAttorney() {
		return selectedAttorney;
	}
	public void setSelectedAttorney(CustomerAttorneySelectView selectedAttorney) {
		this.selectedAttorney = selectedAttorney;
	}
	public CustomerAttorneyView getAttorneyView() {
		return attorneyView;
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
	public List<SelectItem> getAttorneyDistricts() {
		return attorneyDistricts;
	}
	public List<SelectItem> getAttorneySubdistricts() {
		return attorneySubdistricts;
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
		fromSubScreen = "true".equals(FacesUtil.getFlash().get("customer_fromsub"));
		fromCustomerId = Util.parseLong(FacesUtil.getFlash().get("customer_fromid"),-1L);
		fromJuristic = "true".equals(FacesUtil.getFlash().get("customer_fromjuris"));
		
		canUpdateInfo = true; 
		
		attorneySelectViews = postCustomerInfoIndvControl.getAttorneySelectList(workCaseId);
		attorneySelectDataModel = new AttorneySelectDataModel();
		attorneySelectDataModel.setWrappedData(attorneySelectViews);
		
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
			if (!userAccessControl.canUserAccess(Screen.PostCustomerInfoIndv, stepId)) {
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
	
	public void onSelectAttorneyProvince() {
		attorneyView.setDistrictId(0);
		attorneyView.setSubDistrictId(0);
		attorneySubdistricts = Collections.emptyList();
		
		if (attorneyView.getProvinceId() > 0)
			attorneyDistricts = generalPeopleInfoControl.listDistricts(attorneyView.getProvinceId());
		else
			attorneyDistricts = Collections.emptyList();
	}
	public void onSelectAttorneyDistrict() {
		attorneyView.setSubDistrictId(0);
		attorneySubdistricts = Collections.emptyList();
		
		if (attorneyView.getDistrictId() > 0)
			attorneySubdistricts = generalPeopleInfoControl.listSubDistricts(attorneyView.getDistrictId());
		else
			attorneySubdistricts = Collections.emptyList();
	}
	public void onSelectAttorneyRight() {
		_calculateAttorneyDetail(false);
	}
	public void onSelectAppointeeRelationship() {
		_calculateAttorneyDetail(false);
	}
	public void onSelectAppotineeOwner() {
		_calculateAttorneyDetail(false);
	}
	
	
	public void onSaveCustomerInfo() {
        log.debug("onSaveCustomerInfo (customer: {}, attorneyView: {})",customer,attorneyView);
		postCustomerInfoIndvControl.saveCustomerInfoIndividual(customer,attorneyView);
		
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
		customer = postCustomerInfoIndvControl.getCustomer(customerId);
        log.debug("_loadInitData (customer address list: {})",customer.getAddresses());
		
		boolean back = false;
		if (customer.getIndividualId() <= 0) {
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
		
		if (canUpdateInfo && customer.getMaritalStatusId() >= 0)
			canUpdateSpouse = spouseMaritalSet.contains(customer.getMaritalStatusId()) && customer.isHasSpouseData();
		else
			canUpdateSpouse = false;
		
		currentAttorneyView = postCustomerInfoIndvControl.getCustomerAttorneyView(customer.getCustomerAttorneyId());
		if (currentAttorneyView.getCustomerId() > 0) {
			for (CustomerAttorneySelectView view : attorneySelectViews) {
				if (view.getCustomerId() == currentAttorneyView.getCustomerId()) {
					selectedAttorney = view;
					break;
				}
			}
		}
		_calculateAttorneyDetail(true);
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
	
	private void _calculateAttorneyDetail(boolean init) {
		RadioValue attorneyRequired = customer.getAttorneyRequired();
		AttorneyRelationType type = customer.getAttorneyRelationType();
		
		if (!RadioValue.YES.equals(attorneyRequired)) {
			//disable all about attorney
			customer.setAttorneyRelationType(AttorneyRelationType.OTHERS);
			attorneyDetailEditable = false;
			attorneyView = new CustomerAttorneyView();
			selectedAttorney = null;
			_preCalculateAttorneyDropdown();
			return;
		}
		if (type == null)
			type = AttorneyRelationType.NA;
		switch (type) {
			case BORROWER :
				if (selectedAttorney != null) {
					attorneyDetailEditable = true;
					if (init)
						attorneyView = currentAttorneyView;
					else
						attorneyView = postCustomerInfoIndvControl.getCustomerAttorneyViewFromCustomer(selectedAttorney.getCustomerId());
				} else {
					attorneyDetailEditable = false;
					attorneyView = currentAttorneyView;
				}
				_preCalculateDropdown();
				return;
			case OTHERS :
				attorneyDetailEditable = true;
				break;
			default :
				attorneyDetailEditable = false;
				break;
		}
		selectedAttorney = null;
		attorneyView = currentAttorneyView;
		if (!canUpdateInfo) {
			attorneyDetailEditable = false;
		} else {
			_preCalculateAttorneyDropdown();
		}
	}
	
	
	private void _preCalculateAttorneyDropdown() {
		if (attorneyView == null)
			return;
		if (attorneyView.getProvinceId() > 0) {
			attorneyDistricts = generalPeopleInfoControl.listDistricts(attorneyView.getProvinceId());
			if (attorneyView.getDistrictId() > 0) {
				attorneySubdistricts = generalPeopleInfoControl.listSubDistricts(attorneyView.getDistrictId());
			} else {
				attorneyView.setSubDistrictId(0);
				attorneySubdistricts = Collections.emptyList();
			}
		} else {
			attorneyView.setDistrictId(0);
			attorneyView.setSubDistrictId(0);
			attorneySubdistricts = Collections.emptyList();
			attorneyDistricts = Collections.emptyList();
		}
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
	
	
	public class AttorneySelectDataModel extends ListDataModel<CustomerAttorneySelectView> implements SelectableDataModel<CustomerAttorneySelectView> {
		@SuppressWarnings("unchecked")
		@Override
		public CustomerAttorneySelectView getRowData(String key) {
			long id = Util.parseLong(key, -1);
			if (id <= 0)
				return null;
			List<CustomerAttorneySelectView> datas = (List<CustomerAttorneySelectView>) getWrappedData();
			if (datas == null || datas.isEmpty())
				return null;
			for (CustomerAttorneySelectView data : datas) {
				if (data.getCustomerId() == id)
					return data;
			}
			return null;
		}
		@Override
		public Object getRowKey(CustomerAttorneySelectView data) {
			return data.getCustomerId();
		}
	}
	
	/*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
        HttpSession session = FacesUtil.getSession(false);
        String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.PostCustomerInfoIndv, ownerCaseUserId);
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
	
	public String mandateBusinessType() {
		boolean isMandate = FieldsControlView.DEFAULT_MANDATE;
		FieldsControlView field = fieldMap.get("buzType");
		if (field != null)
			isMandate = field.isMandate();
		isMandate &= (customer.getRelationId() == 1 || customer.getRelationId() == 2); 
		return isMandate ? " *" : "";
	}
	public String mandateAddress(CustomerInfoPostAddressView address,String name) {
		int type = address.getAddressType();
		String prefix = "current.";
		if (type == 2) {
			prefix = "registration.";
		} else if (type==3) {
			prefix ="work.";
		}
		return mandate(prefix+name);	
	}
	public boolean isAddressDisabled(CustomerInfoPostAddressView address,String name) {
		int type = address.getAddressType();
		String prefix = "current.";
		if (type == 2) {
			prefix = "registration.";
		} else if (type==3) {
			prefix ="work.";
		}
		return isDisabled(prefix+name) || !address.canUpdate();	
	}
}

