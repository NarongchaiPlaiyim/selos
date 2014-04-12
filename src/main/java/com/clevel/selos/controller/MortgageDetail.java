package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SelectableDataModel;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.GeneralPeopleInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.MortgageDetailControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.CreditDetailSimpleView;
import com.clevel.selos.model.view.CustomerAttorneyView;
import com.clevel.selos.model.view.CustomerAttorneySelectView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.model.view.MortgageInfoCollOwnerView;
import com.clevel.selos.model.view.MortgageInfoCollSubView;
import com.clevel.selos.model.view.MortgageInfoView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name = "mortgageDetail")
public class MortgageDetail implements Serializable {
	private static final long serialVersionUID = -1567031252193766024L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	
	@Inject
	private GeneralPeopleInfoControl generalPeopleInfoControl;
	
	@Inject
	private MortgageDetailControl mortgageDetailControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private long stageId = -1;
	private long mortgageId = -1;
	private BasicInfoView basicInfoView;
	private List<CustomerAttorneySelectView> attorneySelectViews;
	private CustomerAttorneyView currentAttorneyView;
	private Set<Integer> spouseMaritialSet;
	
	//Dropdown list
	private List<SelectItem> outsourceCompaines;
	private List<SelectItem> landOffices;
	private List<SelectItem> documentTypes;
	private List<SelectItem> titles;
	private List<SelectItem> races;
	private List<SelectItem> nationalities;
	private List<SelectItem> provinces;
	private List<SelectItem> districts;
	private List<SelectItem> subdistricts;
	private List<SelectItem> maritalStatuses;
	private List<SelectItem> countries;
	
	//Property
	private boolean attorneyDetailEditable;
	
	private MortgageInfoView mortgageInfoView;
	private List<MortgageInfoCollOwnerView> collOwners;
	private List<MortgageInfoCollSubView> collSubs;
	private List<CreditDetailSimpleView> credits;
	
	private AttorneySelectDataModel attorneySelectDataModel;
	private CustomerAttorneySelectView selectedAttorney;
	private CustomerAttorneyView attorneyView;
	
	
    public MortgageDetail(){
    }
    
    public Date getLastUpdateDateTime() {
		return mortgageInfoView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (mortgageInfoView.getModifyBy() != null)
			return mortgageInfoView.getModifyBy().getDisplayName();
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
	
	public int[] getMortgageOrderList() {
		int[] rtn = new int[10];
		for (int i=1;i<=10;i++)
			rtn[i-1] = i;
		return rtn;
	}
	
	public List<SelectItem> getOutsourceCompaines() {
		return outsourceCompaines;
	}

	public List<SelectItem> getLandOffices() {
		return landOffices;
	}

	public List<SelectItem> getDocumentTypes() {
		if (attorneyDetailEditable)
			return documentTypes;
		else 
			return null;
	}

	public List<SelectItem> getTitles() {
		if (attorneyDetailEditable)
			return titles;
		else
			return null;
	}

	public List<SelectItem> getRaces() {
		if (attorneyDetailEditable)
			return races;
		else
			return null;
	}

	public List<SelectItem> getNationalities() {
		if (attorneyDetailEditable)
			return nationalities;
		else
			return null;
	}

	public List<SelectItem> getProvinces() {
		if (attorneyDetailEditable)
			return provinces;
		else
			return null;
	}

	public List<SelectItem> getDistricts() {
		if (attorneyDetailEditable)
			return districts;
		else
			return null;
	}

	public List<SelectItem> getSubdistricts() {
		if (attorneyDetailEditable)
			return subdistricts;
		else
			return null;
	}

	public List<SelectItem> getMaritalStatuses() {
		if (attorneyDetailEditable)
			return maritalStatuses;
		else
			return null;
	}

	public List<SelectItem> getCountries() {
		if (attorneyDetailEditable)
			return countries;
		else
			return null;
	}
	
	public boolean isAttorneyDetailEditable() {
		return attorneyDetailEditable;
	}
	public boolean isAppointeeOwnerSelectable() {
		return isEnablePOA() && AttorneyRelationType.BORROWER.equals(mortgageInfoView.getAttorneyRelation());
	}
	public MortgageInfoView getMortgageInfoView() {
		return mortgageInfoView;
	}
	public List<MortgageInfoCollOwnerView> getCollOwners() {
		return collOwners;
	}
	public boolean isEnablePOA() {
		return RadioValue.YES.equals(mortgageInfoView.getPoa());
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
	public boolean canUpdateSpouse() {
		if (!attorneyDetailEditable)
			return false;
		
		if (attorneyView.getMaritalStatusId() >= 0)
			return spouseMaritialSet.contains(attorneyView.getMaritalStatusId());
		else
			return false;
	}
	public List<MortgageInfoCollSubView> getCollSubs() {
		return collSubs;
	}
	public List<CreditDetailSimpleView> getCredits() {
		return credits;
	}
	public String getAgeYearRange() {
		Calendar calendar = Calendar.getInstance(new Locale("th","TH"));
		return "2450:"+calendar.get(Calendar.YEAR);
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
		Map<String,Object> params =  FacesUtil.getParamMapFromFlash("mortgageParams");
		mortgageId = Util.parseLong(params.get("mortgageId"),-1);
		if (mortgageId <= 0)
			mortgageId = Util.parseLong(FacesUtil.getParameter("mortgageId"),-1);
		attorneySelectViews = mortgageDetailControl.getAttorneySelectList(workCaseId);
		attorneySelectDataModel = new AttorneySelectDataModel();
		attorneySelectDataModel.setWrappedData(attorneySelectViews);
		
		_loadDropdownList();
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
				if (mortgageId <= 0) {
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
			ec.redirect(ec.getRequestContextPath()+redirectPage);
		} catch (IOException e) {
			log.error("Fail to redirect screen to "+redirectPage,e);
		}
	}
	public void onSelectProvince() {
		attorneyView.setDistrictId(0);
		attorneyView.setSubDistrictId(0);
		subdistricts = Collections.emptyList();
		
		if (attorneyView.getProvinceId() > 0)
			districts = generalPeopleInfoControl.listDistricts(attorneyView.getProvinceId());
		else
			districts = Collections.emptyList();
	}
	public void onSelectDistrict() {
		attorneyView.setSubDistrictId(0);
		subdistricts = Collections.emptyList();
		
		if (attorneyView.getDistrictId() > 0)
			subdistricts = generalPeopleInfoControl.listSubDistricts(attorneyView.getDistrictId());
		else
			subdistricts = Collections.emptyList();
	}
	public void onSelectPOA() {
		if (!RadioValue.YES.equals(mortgageInfoView.getPoa())) {
			//mark all POA in coll owners to false
			for (MortgageInfoCollOwnerView owner : collOwners) {
				owner.setPOA(false);
			}
		}
		_calculateAttorneyDetail(false);
	}
	public void onSelectAppointeeRelationship() {
		_calculateAttorneyDetail(false);
	}
	public void onSelectAppotineeOwner() {
		_calculateAttorneyDetail(false);
	}
	
	public void onSaveMortgageDetail() {
		
		mortgageId = mortgageDetailControl.saveMortgageDetail(workCaseId, mortgageInfoView, collOwners, attorneyView);
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
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
	public void handleBirthDay(SelectEvent event) {
		if (attorneyView == null)
			return;
		Date newDate = (Date) event.getObject();
		attorneyView.setAge(Util.calAge(newDate));
	}
	public void onCancelMortgage() {
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	/*
	 * Private method
	 */
	private void _loadDropdownList() {
		outsourceCompaines = mortgageDetailControl.listMortgageOSCompanies();
		landOffices = mortgageDetailControl.listMortgageLandOffices();
		
		documentTypes = generalPeopleInfoControl.listIndividualDocumentTypes();
		titles = generalPeopleInfoControl.listIndividualTitles(FacesUtil.getLanguage());
		races = generalPeopleInfoControl.listRaces();
		nationalities = generalPeopleInfoControl.listNationalities();
		provinces = generalPeopleInfoControl.listProvinces();
		districts = Collections.emptyList(); // Need to process
		subdistricts = Collections.emptyList(); //Need to process
		maritalStatuses = generalPeopleInfoControl.listMaritalStatuses();
		countries = generalPeopleInfoControl.listCountries();
		
		spouseMaritialSet = generalPeopleInfoControl.listSpouseReqMaritalStatues();
	}
	private void _loadInitData() {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		
		mortgageInfoView = mortgageDetailControl.getMortgageInfo(mortgageId);
		
		if (mortgageInfoView.getId() <= 0 || mortgageInfoView.getWorkCaseId() != workCaseId) {
			String redirectPage = "/site/mortgageSummary.jsf";
			try {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				ec.redirect(ec.getRequestContextPath()+redirectPage);
			} catch (IOException e) {
				log.error("Fail to redirect screen to "+redirectPage,e);
			}
		}
		
		collOwners = mortgageDetailControl.getCollOwners(mortgageInfoView.getId());
		collSubs = mortgageDetailControl.getMortgageInfoCollSubList(mortgageInfoView.getId());
		credits = mortgageDetailControl.getMortgageInfoCreditList(mortgageInfoView.getId());
		
		currentAttorneyView = mortgageDetailControl.getCustomerAttorneyView(mortgageInfoView.getCustomerAttorneyId());
		if (currentAttorneyView.getCustomerId() > 0) {
			for (CustomerAttorneySelectView view : attorneySelectViews) {
				if (view.getCustomerId() == currentAttorneyView.getCustomerId()) {
					selectedAttorney = view;
					break;
				}
			}
		}
		
		_calculateAttorneyDetail(true);
	}
	private void _calculateAttorneyDetail(boolean init) {
		RadioValue poa = mortgageInfoView.getPoa();
		AttorneyRelationType type = mortgageInfoView.getAttorneyRelation();
		
		if (!RadioValue.YES.equals(poa)) {
			//disable all about attorney
			mortgageInfoView.setAttorneyRelation(AttorneyRelationType.OTHERS);
			attorneyDetailEditable = false;
			attorneyView = new CustomerAttorneyView();
			selectedAttorney = null;
			_preCalculateDropdown();
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
						attorneyView = mortgageDetailControl.getCustomerAttorneyViewFromCustomer(selectedAttorney.getCustomerId());
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
		_preCalculateDropdown();
	}
	
	private void _preCalculateDropdown() {
		if (attorneyView == null)
			return;
		if (attorneyView.getProvinceId() > 0) {
			districts = generalPeopleInfoControl.listDistricts(attorneyView.getProvinceId());
			if (attorneyView.getDistrictId() > 0) {
				subdistricts = generalPeopleInfoControl.listSubDistricts(attorneyView.getDistrictId());
			} else {
				attorneyView.setSubDistrictId(0);
				subdistricts = Collections.emptyList();
			}
		} else {
			attorneyView.setDistrictId(0);
			attorneyView.setSubDistrictId(0);
			subdistricts = Collections.emptyList();
			districts = Collections.emptyList();
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
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.MortgageInfoDetail);
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
