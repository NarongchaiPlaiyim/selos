package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import org.primefaces.model.SelectableDataModel;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.GeneralPeopleInfoControl;
import com.clevel.selos.businesscontrol.MortgageDetailControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.CustomerAttorneyView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.MortgageInfoAttorneySelectView;
import com.clevel.selos.model.view.MortgageInfoCollOwnerView;
import com.clevel.selos.model.view.MortgageInfoCollSubView;
import com.clevel.selos.model.view.MortgageInfoView;
import com.clevel.selos.model.view.NewCreditDetailSimpleView;
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
	private String fromPage;
	private long mortgageId = -1;
	private BasicInfoView basicInfoView;
	private List<MortgageInfoAttorneySelectView> attorneySelectViews;
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
	private List<NewCreditDetailSimpleView> credits;
	
	private AttorneySelectDataModel attorneySelectDataModel;
	private MortgageInfoAttorneySelectView selectedAttorney;
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
	public String getMinDate() {
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
	public MortgageInfoAttorneySelectView getSelectedAttorney() {
		return selectedAttorney;
	}
	public void setSelectedAttorney(MortgageInfoAttorneySelectView selectedAttorney) {
		this.selectedAttorney = selectedAttorney;
	}
	public CustomerAttorneyView getAttorneyView() {
		return attorneyView;
	}
	public boolean canUpdateSpouse() {
		if (!attorneyDetailEditable)
			return false;
		
		if (attorneyView.isCanUpdateRelationInfo() && attorneyView.getMaritalStatusId() >= 0)
			return spouseMaritialSet.contains(attorneyView.getMaritalStatusId());
		else
			return false;
	}
	public List<MortgageInfoCollSubView> getCollSubs() {
		return collSubs;
	}
	public List<NewCreditDetailSimpleView> getCredits() {
		return credits;
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
		}
		Map<String,Object> params =  FacesUtil.getParamMapFromFlash("mortgageParams");
		fromPage = (String)params.get("fromPage");
		mortgageId = Util.parseLong(params.get("mortgageId"),-1);
		if (mortgageId <= 0)
			mortgageId = Util.parseLong(FacesUtil.getParameter("mortgageId"),-1);
		attorneySelectViews = mortgageDetailControl.getAttorneySelectList(workCaseId);
		attorneySelectDataModel = new AttorneySelectDataModel();
		attorneySelectDataModel.setWrappedData(attorneySelectViews);
		
		_loadDropdownList();
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
		_calculateAttorneyDetail();
	}
	public void onSelectAppointeeRelationship() {
		_calculateAttorneyDetail();
	}
	public void onSelectAppotineeOwner() {
		_calculateAttorneyDetail();
	}
	
	public void onSaveMortgageDetail() {
		
		mortgageId = mortgageDetailControl.saveMortgageDetail(workCaseId, mortgageInfoView, collOwners, attorneyView);
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	public String clickCustomerInfo(long id) {
		//TODO Clear what it is
		Map<String, Object> map = new HashMap<String, Object>();
		
        map.put("isFromSummaryParam",false);
        map.put("isFromJuristicParam",false);
        map.put("isFromIndividualParam",false);
        map.put("isEditFromJuristic", false);
        CustomerInfoView cusView = new CustomerInfoView();
        cusView.reset();
        map.put("customerInfoView", cusView);
        map.put("customerId", id);
		return "customerInfoIndividual?faces-redirect=true";
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
		maritalStatuses = generalPeopleInfoControl.listMaritialStatuses();
		countries = generalPeopleInfoControl.listCountries();
		
		spouseMaritialSet = generalPeopleInfoControl.listSpouseReqMaritialStatues();
	}
	private void _loadInitData() {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		
		//TODO Load mortgage info by using workcase and mortgageId
		mortgageInfoView = mortgageDetailControl.getMortgageInfo(mortgageId);
		collOwners = mortgageDetailControl.getCollOwners(mortgageInfoView.getId());
		collSubs = mortgageDetailControl.getMortgageInfoCollSubList(mortgageInfoView.getId());
		credits = mortgageDetailControl.getMortgageInfoCreditList(mortgageInfoView.getId());
		
		currentAttorneyView = mortgageDetailControl.getCustomerAttorneyView(mortgageInfoView.getCustomerAttorneyId());
		if (currentAttorneyView.getCustomerId() > 0) {
			for (MortgageInfoAttorneySelectView view : attorneySelectViews) {
				if (view.getCustomerId() == currentAttorneyView.getCustomerId()) {
					view.setAttorneyDetail(currentAttorneyView);
					selectedAttorney = view;
				}
			}
		}
		if (selectedAttorney != null)
			currentAttorneyView = new CustomerAttorneyView();
		
		_calculateAttorneyDetail();
	}
	private void _calculateAttorneyDetail() {
		RadioValue poa = mortgageInfoView.getPoa();
		AttorneyRelationType type = mortgageInfoView.getAttorneyRelation();
		log.info("POA "+poa);
		log.info("Type "+type);
		
		if (!RadioValue.YES.equals(poa)) {
			//disable all about attorney
			mortgageInfoView.setAttorneyRelation(AttorneyRelationType.NA);
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
					attorneyView = selectedAttorney.getAttorneyDetail();
				} else {
					attorneyDetailEditable = false;
					attorneyView = currentAttorneyView;
				}
				break;
			case OTHERS :
				attorneyDetailEditable = true;
				attorneyView = currentAttorneyView;
				_preCalculateDropdown();
				selectedAttorney = null;
				break;
			default :
				attorneyDetailEditable = false;
				selectedAttorney = null;
				attorneyView = currentAttorneyView;
				break;
		}
		log.info("attorneyDetailEditable "+attorneyDetailEditable);
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
	
	
	public class AttorneySelectDataModel extends ListDataModel<MortgageInfoAttorneySelectView> implements SelectableDataModel<MortgageInfoAttorneySelectView> {
		@SuppressWarnings("unchecked")
		@Override
		public MortgageInfoAttorneySelectView getRowData(String key) {
			long id = Util.parseLong(key, -1);
			if (id <= 0)
				return null;
			List<MortgageInfoAttorneySelectView> datas = (List<MortgageInfoAttorneySelectView>) getWrappedData();
			if (datas == null || datas.isEmpty())
				return null;
			for (MortgageInfoAttorneySelectView data : datas) {
				if (data.getCustomerId() == id)
					return data;
			}
			return null;
		}
		@Override
		public Object getRowKey(MortgageInfoAttorneySelectView data) {
			return data.getCustomerId();
		}
	}
}
