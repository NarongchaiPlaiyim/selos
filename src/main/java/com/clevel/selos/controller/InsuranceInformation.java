package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.InsuranceInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.model.view.insurance.InsuranceInfoSummaryView;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "insuranceInfo")
public class InsuranceInformation implements Serializable {
	private static final long serialVersionUID = 6866647827035876947L;

	@Inject
	@SELOS
	Logger log;

	@Inject
	@NormalMessage
	Message msg;

	// *** View ***//
	private InsuranceInfoSummaryView infoSummaryView;
	private List<InsuranceInfoView> insuranceInfoViewList;

	private BasicInfoView basicInfoView;

	// New / New + Change
	private int approvedType;

	// Total Premium
	private BigDecimal total;

	// *** Mode for check Add or Edit ***//
	enum ModeForButton {
		ADD, EDIT
	}

	private ModeForButton modeForButton;
	private int rowIndex;

	// session
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private User user;

	@Inject
	private InsuranceInfoControl insuranceInfoControl;

	@Inject
	private BasicInfoControl basicInfoControl;

	@Inject
	private UserAccessControl userAccessControl;

	@Inject
	public InsuranceInformation() {
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
			user = (User) session.getAttribute("user");
			log.info("init ::: workCaseId is " + workCaseId);
		} else {
			log.info("init ::: workCaseId is null.");
			try {
				FacesUtil.redirect("/site/inbox.jsf");
				return;
			} catch (Exception ex) {
				log.info("Exception :: {}", ex);
			}
		}
		_loadInitData();
		this.infoSummaryView = this.insuranceInfoControl.getInsuranceInforSummaryView(workCaseId);
		this.insuranceInfoViewList = this.insuranceInfoControl.getInsuranceInfo(workCaseId);
		_loadFieldControl();
		addition();
	}

	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;

		String redirectPage = null;
		log.info("preRender workCase Id = " + workCaseId);
		if (workCaseId > 0) {
			if (!userAccessControl.canUserAccess(Screen.InsuranceInfo, stepId)) {
				redirectPage = "/site/inbox.jsf";
			} else {
				return;
			}
		}
		try {
			log.info("preRender " + redirectPage);
			if (redirectPage == null) {
				redirectPage = "/site/inbox.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + redirectPage);
		} catch (IOException e) {
			log.error("Fail to redirect screen to " + redirectPage, e);
		}
	}

	public void addition() {
		total = BigDecimal.ZERO;
		for (InsuranceInfoView view : insuranceInfoViewList) {
			total = total.add(view.getPremium());
		}
	}

	public List<InsuranceInfoView> getInsuranceInfoViewList() {
		return insuranceInfoViewList;
	}

	public void setInsuranceInfoViewList(List<InsuranceInfoView> insuranceInfoViewList) {
		this.insuranceInfoViewList = insuranceInfoViewList;
	}

	public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}

	public Date getLastUpdateDateTime() {
		return infoSummaryView.getModifyDate();
	}

	public String getLastUpdateBy() {
		if (infoSummaryView.getModifyBy() != null)
			return infoSummaryView.getModifyBy().getDisplayName();
		else
			return null;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void onSave() {
		log.info("InsuranceInfo: onSave()");
		insuranceInfoControl.saveInsuranceInfo(insuranceInfoViewList, this.total, workCaseId);
		init();
	}

	private void _loadInitData() {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}

	}

	/*
	 * Mandate and read-only
	 */
	@Inject
	MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();

	private void _loadFieldControl() {
		if (workCaseId > 0) {
			List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.InsuranceInfo);
			fieldMap.clear();

			for (FieldsControlView field : fields) {
				fieldMap.put(field.getFieldName(), field);
			}
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
