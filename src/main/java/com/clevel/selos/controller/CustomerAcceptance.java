package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.CustomerAcceptanceControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveResult;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.*;
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
import java.text.SimpleDateFormat;
import java.util.*;

@ViewScoped
@ManagedBean(name="customerAcceptance")
public class CustomerAcceptance implements Serializable {
	private static final long serialVersionUID = 4912829545245905956L;
	@Inject @SELOS
	private Logger log;
	
	@Inject
	private CustomerAcceptanceControl customerAcceptanceControl;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	@Inject
	private UserAccessControl userAccessControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	
	private User user;
	private Status workCaseStatus;
	private List<ContactRecordDetailView> deleteList;
	private BasicInfoView basicInfoView;
	
	//Property
	private TCGInfoView tcgInfoView; 
	private CustomerAcceptanceView customerAcceptanceView;
	private List<ContactRecordDetailView> contactRecordDetailViews;
	private ContactRecordDetailView contactRecord;
	private int deletedRowId;
	private List<Reason> reasons;
	private boolean addDialog;
	
	public CustomerAcceptance() {
	}
	
	public CustomerAcceptanceView getCustomerAcceptanceView() {
		return customerAcceptanceView;
	}
	public Date getLastUpdateDateTime() {
		return customerAcceptanceView.getModifyDate();
	}
	public String getLastUpdateBy() {
		User modUser = customerAcceptanceView.getModifyBy();
		if (modUser == null)
			return "";
		return modUser.getDisplayName();
	}
	public String getMinTCGPayinSlipSendDate() {
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy");
		return dFmt.format(new Date());
	}
	public ApproveResult getApproveResult() {
		if (basicInfoView == null)
			return ApproveResult.NA;
		else
			return basicInfoView.getApproveResult();
	}
	public void setApproveResult(ApproveResult result) {
		//DO NOTHING
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
	public String getMinDate() {
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy");
		return dFmt.format(new Date());
	}
	public List<ContactRecordDetailView> getContactRecordDetailViews() {
		return contactRecordDetailViews;
	}
	public ContactRecordDetailView getContactRecord() {
		return contactRecord;
	}
	public void setContactRecord(ContactRecordDetailView contactRecord) {
		this.contactRecord = contactRecord;
	}
	public List<Reason> getReasons() {
		return reasons;
	}
	public int getDeletedRowId() {
		return deletedRowId;
	}
	public void setDeletedRowId(int deletedRowId) {
		this.deletedRowId = deletedRowId;
	}
	public TCGInfoView getTcgInfoView() {
		return tcgInfoView;
	}
	public boolean isAddDialog() {
		return addDialog;
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
		_loadFieldControl();
		_loadInitData();
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		log.info("preRender workCase Id = "+workCaseId);
		if (workCaseId > 0) {
			if (!userAccessControl.canUserAccess(Screen.ContactRecord, stepId)) {
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
	
	public void onOpenAddContactRecordDialog() {
		contactRecord = new ContactRecordDetailView();
		contactRecord.setId(0);
		contactRecord.setCallingDate(new Date());
		contactRecord.setCreateBy(user);
		contactRecord.setStatus(workCaseStatus);
		if (reasons == null) {
			reasons = customerAcceptanceControl.getContactRecordReasons();
		}
		addDialog = true;
	}
	public void onOpenUpdateContactRecordDialog() {
		if (reasons == null) {
			reasons = customerAcceptanceControl.getContactRecordReasons();
		}
		addDialog = false;
	}
	public void onAddContactRecord() {
		Reason reason = _retrieveReasonFromId(contactRecord.getUpdReasonId());
		contactRecord.setReason(reason);
		contactRecord.updateNextCallingDate();
		contactRecordDetailViews.add(contactRecord);
		contactRecord = null;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onUpdateContactRecord() {
		Reason reason = _retrieveReasonFromId(contactRecord.getUpdReasonId());
		contactRecord.setReason(reason);
		contactRecord.updateNextCallingDate();
		contactRecord.setNeedUpdate(true);
		contactRecord = null;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onDeleteContactRecord() {
		if (deletedRowId < 0 || deletedRowId > contactRecordDetailViews.size())
			return;
		ContactRecordDetailView delete = contactRecordDetailViews.remove(deletedRowId);
		if (delete != null && delete.getId() > 0) {
			deleteList.add(delete);
		}
		deletedRowId = -1;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	public boolean isContactUpdatable(ContactRecordDetailView detail) {
		if (detail == null)
			return false;
		return Util.equals(user.getId(),detail.getCreateBy().getId()) && workCaseStatus.getId() == detail.getStatus().getId();
	}
	
	public void onSaveCustomerAcceptance() {
		customerAcceptanceControl.saveCustomerContactRecords(workCaseId,customerAcceptanceView, tcgInfoView, contactRecordDetailViews, deleteList);
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onCancelCustomerAcceptance() {
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	/*
	 * Private method
	 */
	private Reason _retrieveReasonFromId(int id) {
		for (Reason reason : reasons) {
			if (reason.getId() == id)
				return reason;
		}
		return null;
	}
	private void _loadInitData() {
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		
		tcgInfoView = customerAcceptanceControl.getTCGInfoView(workCaseId);
		workCaseStatus = customerAcceptanceControl.getWorkCaseStatus(workCaseId);
		customerAcceptanceView = customerAcceptanceControl.getCustomerAcceptanceView(workCaseId);
		contactRecordDetailViews = new ArrayList<ContactRecordDetailView>(customerAcceptanceControl.getContactRecordDetails(customerAcceptanceView.getId()));
		contactRecord = null;
		deleteList = new ArrayList<ContactRecordDetailView>();
		deletedRowId = -1;
		preRenderCheck = false;
	}
	
	/*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private final HashMap<String, FieldsControlView> dialogFieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
        HttpSession session = FacesUtil.getSession(false);
        String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.ContactRecord, ownerCaseUserId);
		List<FieldsControlView> dialogFields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.CallingRecordDialog, ownerCaseUserId);
		fieldMap.clear();
		dialogFieldMap.clear();
		for (FieldsControlView field : fields) {
			fieldMap.put(field.getFieldName(), field);
		}
		for (FieldsControlView field : dialogFields) {
			dialogFieldMap.put(field.getFieldName(), field);
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
	public boolean isDialogMandate(String name) {
		FieldsControlView field = dialogFieldMap.get(name);
		if (field == null)
			return FieldsControlView.DEFAULT_MANDATE;
		return field.isMandate();
	}
	public boolean isDialogDisable(String name) {
		FieldsControlView field = dialogFieldMap.get(name);
		if (field == null)
			return FieldsControlView.DEFAULT_READONLY;
		return field.isReadOnly();
	}
}
