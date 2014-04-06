package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.FeeCalculationControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.FeeCollectionAccountView;
import com.clevel.selos.model.view.FeeCollectionDetailView;
import com.clevel.selos.model.view.FeeSummaryView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name="feeCalculation")
public class FeeCalculation implements Serializable {

	private static final long serialVersionUID = -8238319076227615414L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private FeeCalculationControl feeCalculationControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private long stageId = -1;
	
	private List<BigDecimal> totalAgreementList;
	private List<BigDecimal> totalNonAgreementList;
	//Property
	private FeeSummaryView summary;
	private List<FeeCollectionAccountView> accounts;
	private List<List<FeeCollectionDetailView>> detailsAgreement;
	
	private BigDecimal grandTotalAgreement;
	private List<List<FeeCollectionDetailView>> detailsNonAgreement;
	
	public FeeCalculation() {
	}
	public Date getLastUpdateDateTime() {
		return summary.getModifyDate();
	}
	public String getLastUpdateBy() {
		return summary.getModifyUser();
	}
	public FeeSummaryView getSummary() {
		return summary;
	}
	public List<FeeCollectionAccountView> getAccounts() {
		return accounts;
	}
	public List<List<FeeCollectionDetailView>> getDetailsAgreement() {
		return detailsAgreement;
	}
	public List<List<FeeCollectionDetailView>> getDetailsNonAgreement() {
		return detailsNonAgreement;
	}
	public BigDecimal getGrandTotalAgreement() {
		return grandTotalAgreement;
	}
	public BigDecimal getTotalAgreement(int index) {
		if (index >= 0 && index <= totalAgreementList.size()) 
			return totalAgreementList.get(index);
		else
			return BigDecimal.ZERO;
	}
	public BigDecimal getTotalNonAgreement(int index) {
		if (index >= 0 && index <= totalNonAgreementList.size()) 
			return totalNonAgreementList.get(index);
		else
			return BigDecimal.ZERO;
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
		_loadFieldControl();
		_loadInitData(false);
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		System.err.println("STEP = "+stepId+" < STAGE "+stageId);
		String redirectPage = null;
		if (workCaseId > 0) {
			if (stepId <= 0 || stageId != 301) {
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
	public void onCancelFeeCalculation() {
		_loadInitData(true);
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onSaveFeeCalculation() {
		feeCalculationControl.saveFeeConfirm(summary);
		
		_loadInitData(true);
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	/*
	 * Private method
	 */
	private void _loadInitData(boolean ignoreRecalculate) {
		preRenderCheck = false;
		
		if (workCaseId <= 0) {
			summary = new FeeSummaryView();
			accounts = Collections.emptyList();
			detailsAgreement = Collections.emptyList();
			totalAgreementList = Collections.emptyList();
			grandTotalAgreement = BigDecimal.ZERO;
			detailsNonAgreement = Collections.emptyList();
			totalNonAgreementList = Collections.emptyList();
			return;
		}
		
		summary = feeCalculationControl.getFeeSummary(workCaseId);
		if (!ignoreRecalculate) {
			if ("true".equals(FacesUtil.getParameter("force")) || summary.getId() <= 0) {
				summary = feeCalculationControl.calculateFeeCollection( workCaseId);
			}
		}
		accounts = feeCalculationControl.getFeeCollectionAccounts(workCaseId);
		
		List<List<FeeCollectionDetailView>> details = feeCalculationControl.getFeeCollectionDetails(workCaseId);
		
		detailsAgreement = new ArrayList<List<FeeCollectionDetailView>>();
		totalAgreementList = new ArrayList<BigDecimal>();
		grandTotalAgreement = BigDecimal.ZERO;
		
		detailsNonAgreement = new ArrayList<List<FeeCollectionDetailView>>();
		totalNonAgreementList = new ArrayList<BigDecimal>();
		
		for (List<FeeCollectionDetailView> detailList : details) {
			if (detailList.isEmpty())
				continue;
			FeeCollectionDetailView firstView = detailList.get(0);
			BigDecimal total = (firstView.getAmount() != null) ? firstView.getAmount() : BigDecimal.ZERO;
			for (int i=1;i<detailList.size();i++) {
				FeeCollectionDetailView view = detailList.get(i);
				view.setPaymentMethod(""); //set to blank
				if (view.getAmount() != null)
					total = total.add(view.getAmount());
			}
			if (firstView.isAgreementSign()) {
				detailsAgreement.add(detailList);
				totalAgreementList.add(total);
				grandTotalAgreement = grandTotalAgreement.add(total);
			} else {
				detailsNonAgreement.add(detailList);
				totalNonAgreementList.add(total);
			}
		}
	}
	
	/*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.FeeCalculation);
		fieldMap.clear();
		for (FieldsControlView field : fields) {
			fieldMap.put(field.getFieldName(), field);
		}
	}
	public String mandate(String name) {
		FieldsControlView field = fieldMap.get(name);
		if (field == null)
			return "";
		return field.isMandate() ? " *" : "";
	}
	public boolean isDisabled(String name) {
		FieldsControlView field = fieldMap.get(name);
		if (field == null)
			return false;
		return field.isReadOnly();
	}
}
