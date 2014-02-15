package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BAPAInfoControl;
import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.db.master.InsuranceCompany;
import com.clevel.selos.model.view.BAPAInfoView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name="baInfo")
public class BAInfo implements Serializable {
	private static final long serialVersionUID = -6678163555513002049L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	
	@Inject
	private BAPAInfoControl bapaInfoControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private BasicInfoView basicInfoView;
	
	//Property
	private BAPAInfoView bapaInfoView;
	private List<InsuranceCompany> insuranceCompanies;
	
	
	public BAInfo() {
	}
	
	public Date getLastUpdateDateTime() {
		return bapaInfoView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (bapaInfoView.getModifyBy() == null)
			return "-";
		else
			return bapaInfoView.getModifyBy().getDisplayName();
	}
	public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}
	public String getMinCheckDate() {
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy",new Locale("th", "TH"));
		return dFmt.format(new Date());
	}
	public String getMaxCheckDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 90);
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy",new Locale("th", "TH"));
		return dFmt.format(calendar.getTime());
	}
	
	public BAPAInfoView getBapaInfoView() {
		return bapaInfoView;
	}
	public List<InsuranceCompany> getInsuranceCompanies() {
		return insuranceCompanies;
	}
	public String getInsuranceAccount() {
		if (bapaInfoView.getUpdInsuranceCompany() > 0) {
			for (InsuranceCompany company : insuranceCompanies) {
				if (company.getId() == bapaInfoView.getUpdInsuranceCompany())
					return company.getAccountNumber();
			}
		}
		return null;
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
		insuranceCompanies = bapaInfoControl.getInsuranceCompanies();
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
	
	public void onOpenApplyInformationDialog() {
		
	}
	public void onApplyBAInformation() {
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onOpenAddBAPAInformationDialog() {
		
	}
	public void onAddBAPAInformation() {
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onOpenUpdateBAPAInformaionDialog() {
		
	}
	public void onUpdateBAPAInformation() {
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onDeleteBAPAInformation() {
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onSaveBAInformation() {
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	/*
	 * Private method
	 */
	private void _loadInitData() {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		bapaInfoView = bapaInfoControl.getBAPAInfoView(workCaseId);
	}
}
