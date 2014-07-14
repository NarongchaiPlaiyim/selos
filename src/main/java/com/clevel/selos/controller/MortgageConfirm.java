package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.MortgageConfirmControl;
import com.clevel.selos.businesscontrol.PerfectionReviewControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.model.view.MortgageInfoView;
import com.clevel.selos.model.view.MortgageSummaryView;
import com.clevel.selos.model.view.PerfectionReviewView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name="mortgageConfirm")
public class MortgageConfirm implements Serializable {
	private static final long serialVersionUID = -2455694528177787475L;
	@Inject @SELOS
	private Logger log;
	@Inject
	private BasicInfoControl basicInfoControl;
	
	@Inject
	private MortgageConfirmControl mortgageConfirmControl;
	@Inject
	private PerfectionReviewControl perfectionReviewControl;
	@Inject
	private UserAccessControl userAccessControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private BasicInfoView basicInfoView;
	
	//Property
	private MortgageSummaryView summary;
	private List<MortgageInfoView> mortgages;
	private List<PerfectionReviewView> perfectionReviews;
	
	public MortgageConfirm() {
		
	}
	public Date getLastUpdateDateTime() {
		return summary.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (summary.getModifyBy() != null)
			return summary.getModifyBy().getDisplayName();
		else
			return null;
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
	public List<MortgageInfoView> getMortgages() {
		return mortgages;
	}
	public MortgageSummaryView getSummary() {
		return summary;
	}
	public List<PerfectionReviewView> getPerfectionReviews() {
		return perfectionReviews;
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
		_loadFieldControl();
		_loadInitData();
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		if (workCaseId > 0) {
			if (!userAccessControl.canUserAccess(Screen.AgreementAndMortgageConfirm, stepId)) {
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
	
	public void onSaveMortgageConfirm() {
		mortgageConfirmControl.saveMortgageConfirm(summary, mortgages,workCaseId);
	
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onCancelMortgageConfirm() {
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
			perfectionReviews = perfectionReviewControl.getPerfectionReviews(workCaseId);
		}
		summary = mortgageConfirmControl.getMortgageSummaryView(workCaseId);
		mortgages = mortgageConfirmControl.getMortgageInfoList(workCaseId);
	}
	
	/*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.AgreementAndMortgageConfirm);
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
