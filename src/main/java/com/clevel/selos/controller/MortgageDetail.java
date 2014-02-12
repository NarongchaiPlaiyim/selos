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
import com.clevel.selos.businesscontrol.MortgageDetailControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.BasicInfoView;
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
	private User user;
	private BasicInfoView basicInfoView;
	
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
	private boolean appointeeOwnerSelectable;
	
	//for testing provicnes
	private int provinceId;
	private int districtId;
	private int subdistrictId;
	
    public MortgageDetail(){
    }
    
    public Date getLastUpdateDateTime() {
		//TODO 
		return new Date();
	}
	public String getLastUpdateBy() {
		//TODO
		return user.getDisplayName();
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
	
	
	public List<SelectItem> getOutsourceCompaines() {
		return outsourceCompaines;
	}

	public List<SelectItem> getLandOffices() {
		return landOffices;
	}

	public List<SelectItem> getDocumentTypes() {
		return documentTypes;
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

	public List<SelectItem> getDistricts() {
		return districts;
	}

	public List<SelectItem> getSubdistricts() {
		return subdistricts;
	}

	public List<SelectItem> getMaritalStatuses() {
		return maritalStatuses;
	}

	public List<SelectItem> getCountries() {
		return countries;
	}
	
	public boolean isAttorneyDetailEditable() {
		return attorneyDetailEditable;
	}
	public boolean isAppointeeOwnerSelectable() {
		return appointeeOwnerSelectable;
	}
	
	/* TEST */
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public int getDistrictId() {
		return districtId;
	}
	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}
	public int getSubdistrictId() {
		return subdistrictId;
	}
	public void setSubdistrictId(int subdistrictId) {
		this.subdistrictId = subdistrictId;
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
			user = (User) session.getAttribute("user");
		}
		Map<String,Object> params =  FacesUtil.getParamMapFromFlash("mortgageParams");
		fromPage = (String)params.get("fromPage");
		mortgageId = Util.parseLong(params.get("mortgageId"),-1);
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
		districtId = 0;
		subdistrictId = 0;
		subdistricts = Collections.emptyList();
		
		if (provinceId > 0)
			districts = generalPeopleInfoControl.listDistricts(provinceId);
		else
			districts = Collections.emptyList();
	}
	public void onSelectDistrict() {
		subdistrictId = 0;
		subdistricts = Collections.emptyList();
		
		if (districtId > 0)
			subdistricts = generalPeopleInfoControl.listSubDistricts(districtId);
		else
			subdistricts = Collections.emptyList();
	}
	public void onSelectAppointeeRelationship() {
		attorneyDetailEditable = true;
		appointeeOwnerSelectable = true;
	}
	public void onSelectAppotineeOwner() {
		
	}
	
	public void onSaveMortgageDetail() {
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	public String clickCustomerInfo(long id) {
		//TODO Clear what it is
		Map<String, Object> map = new HashMap<String, Object>();
		/*
        map.put("isFromSummaryParam",true);
        map.put("isFromJuristicParam",false);
        map.put("isFromIndividualParam",false);
        map.put("isEditFromJuristic", false);
        CustomerInfoView cusView = new CustomerInfoView();
        cusView.reset();
        map.put("customerInfoView", cusView);
        */
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
	}
	private void _loadInitData() {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		
		//TODO Load mortage info by using workcase and mortgageId
		
		if (provinceId > 0) {
			districts = generalPeopleInfoControl.listDistricts(provinceId);
			if (districtId > 0) {
				subdistricts = generalPeopleInfoControl.listSubDistricts(districtId);
			} else {
				subdistrictId = 0;
				subdistricts = Collections.emptyList();
			}
		} else {
			districtId = 0;
			subdistrictId = 0;
			subdistricts = Collections.emptyList();
			districts = Collections.emptyList();
		}
		
		attorneyDetailEditable = false;
		appointeeOwnerSelectable = false;
	}
}
