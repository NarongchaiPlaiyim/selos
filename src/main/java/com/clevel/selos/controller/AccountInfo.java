package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.AccountInfoControl;
import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.RequestAccountType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

import org.primefaces.component.selectonemenu.SelectOneMenu;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "accountInfo")
public class AccountInfo implements Serializable {
	private static final long serialVersionUID = -2790471412596111994L;

	@Inject @SELOS
	private Logger log;
	@Inject @NormalMessage
	private Message message;
	@Inject
	private BasicInfoControl basicInfoControl;
	@Inject
	private AccountInfoControl accountInfoControl;
	@Inject
	private UserAccessControl userAccessControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	
	private BasicInfoView basicInfoView;
	private AccountInfoSummaryView summaryView;
	private boolean addDialog = false;
	private List<OpenAccountFullView> deleteList;
	private List<OpenAccountNameView> allNameList;
	private List<OpenAccountCreditView> allCreditList;
	private List<OpenAccountPurposeView> allPurposeList;
	
	//Property
	private List<OpenAccountFullView> openAccounts;
	private int selectedRowId;
	private OpenAccountFullView openAccount;
	private List<SelectItem> toAddAccountNameList;
	private long selectedAddAccountNameId;
	//Drop down
	private List<SelectItem> branches;
	private List<SelectItem> accountTypes;
	private List<SelectItem> productTypes;
	
	private SelectOneMenu accountTypeBind;
	private SelectOneMenu productTypeBind;
	
	public AccountInfo() {
	}
	
	public Date getLastUpdateDateTime() {
		return summaryView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (summaryView.getModifyBy() != null)
			return summaryView.getModifyBy().getDisplayName();
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
	public List<SelectItem> getBranches() {
		return branches;
	}
	public List<SelectItem> getAccountTypes() {
		return accountTypes;
	}
	public List<SelectItem> getProductTypes() {
		return productTypes;
	}
	public List<OpenAccountFullView> getOpenAccounts() {
		return openAccounts;
	}
	public OpenAccountFullView getOpenAccount() {
		return openAccount;
	}
	public boolean isAddDialog() {
		return addDialog;
	}
	public int getSelectedRowId() {
		
		return selectedRowId;
	}
	public void setSelectedRowId(int selectedRowId) {
		//log.info("Set Selected Row ID "+this.selectedRowId +" << "+selectedRowId);
		this.selectedRowId = selectedRowId;
	}
	public long getSelectedAddAccountNameId() {
		return selectedAddAccountNameId;
	}
	public void setSelectedAddAccountNameId(long selectedAddAccountNameId) {
		this.selectedAddAccountNameId = selectedAddAccountNameId;
	}
	public List<SelectItem> getToAddAccountNameList() {
		return toAddAccountNameList;
	}
	public SelectOneMenu getAccountTypeBind() {
		return accountTypeBind;
	}
	public void setAccountTypeBind(SelectOneMenu accountTypeBind) {
		this.accountTypeBind = accountTypeBind;
	}
	public SelectOneMenu getProductTypeBind() {
		return productTypeBind;
	}
	public void setProductTypeBind(SelectOneMenu productTypeBind) {
		this.productTypeBind = productTypeBind;
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
		accountInfoControl.initialOpenAccount(workCaseId);
		
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
			if (!userAccessControl.canUserAccess(Screen.AccountInfo, stepId)) {
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
	
	public void onSelectAccountType() {
		if (openAccount == null)
			return;
		openAccount.setProductTypeId(0);
		if (openAccount.getAccountTypeId() <= 0) {
			productTypes = null;
		} else {
			productTypes = accountInfoControl.getProductTypes(openAccount.getAccountTypeId());
		}
	}
	public void onOpenAddOpenAccountDialog() {
		openAccount = new OpenAccountFullView();
		ArrayList<OpenAccountPurposeView> purposes = new ArrayList<OpenAccountPurposeView>();
		for (OpenAccountPurposeView purpose : allPurposeList) {
			purposes.add(purpose.clone());
		}
		openAccount.setPurposes(purposes);
		ArrayList<OpenAccountCreditView> credits = new ArrayList<OpenAccountCreditView>();
		for (OpenAccountCreditView credit : allCreditList) {
			credits.add(credit.clone());
		}
		openAccount.setCredits(credits);
		_recalculateAccountNameList();
		addDialog = true;
	}
	public void onOpenUpdateOpenAccountDialog() {
		OpenAccountFullView toUpd = null;
		if (selectedRowId >= 0 && selectedRowId < openAccounts.size()) {
			toUpd = openAccounts.get(selectedRowId);
		}
		if (toUpd == null) {
			onOpenAddOpenAccountDialog();
			return;
		}
		openAccount = new OpenAccountFullView();
		openAccount.updateValues(toUpd);
		_prepareDropdown();
		_recalculateAccountNameList();
		
		addDialog = false;
	}
	public void onAddOpenAccountName() {
		if (openAccount == null || selectedAddAccountNameId <= 0)
			return;
		for (OpenAccountNameView name : allNameList) {
			if (name.getCustomerId() != selectedAddAccountNameId)
				continue;
			openAccount.getNames().add(name.clone());
			_recalculateAccountNameList();
			break;
		}
	}
	public void onDeleteOpenAccountName(long customerId) {
		if (openAccount == null || customerId <= 0)
			return;
		OpenAccountNameView toDelete = null;
		for (OpenAccountNameView name : openAccount.getNames()) {
			if (name.getCustomerId() == customerId) {
				toDelete = name;
				break;
			}
		}
		if (toDelete != null) {
			openAccount.deleteOpenAccountName(toDelete);
			_recalculateAccountNameList();
		}
	}
	public void onAddOpenAccount() {
		if (!_validateOpenAccount()) {
			RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
			return;
		}
		_calculateDisplayField();
		openAccounts.add(openAccount);
		openAccount = null;
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onUpdateOpenAccount() {
		if (!_validateOpenAccount()) {
			RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
			return;
		}
		_calculateDisplayField();
		openAccount.setNeedUpdate(true);
		OpenAccountFullView upd = openAccounts.get(selectedRowId);
		upd.updateValues(openAccount);
		openAccount = null;
		selectedRowId = -1;
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onDeleteOpenAccount() {
		if (selectedRowId < 0 || selectedRowId > openAccounts.size())
			return;
		OpenAccountFullView delete = openAccounts.remove(selectedRowId);
		if (delete != null && delete.getId() > 0)
			deleteList.add(delete);
		selectedRowId = -1;
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	public void onSaveAccountInformation() {
		accountInfoControl.saveAccountInfo(workCaseId, summaryView, openAccounts, deleteList);
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onCancelAccountInformation() {
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
		
		summaryView = accountInfoControl.getAccountInfoSummary(workCaseId);
		openAccounts = new ArrayList<OpenAccountFullView>(accountInfoControl.getAccountInfoList(workCaseId));
		
		deleteList = new ArrayList<OpenAccountFullView>();
		selectedRowId = -1;
	}
	
	private void _loadDropdown() {
		allNameList = accountInfoControl.getAllAccountNames(workCaseId);
		allCreditList = accountInfoControl.getAllAccountCredits(workCaseId);
		allPurposeList = accountInfoControl.getAllAccountPurposes();
		branches = accountInfoControl.getBankBranches();
		accountTypes = accountInfoControl.getAccountTypes();
		productTypes = null;
	}
	private boolean _validateOpenAccount() {
		if (openAccount == null)
			return false;
		return true;
		/*
		 * Disable validation due to requirement to validate before submit only
		boolean isError = false;
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (openAccount.getAccountTypeId() <= 0) {
			String msg = message.get("app.accountInfo.account.accountType.validate");
			context.addMessage("accountType", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,null));
			accountTypeBind.setValid(false);
			isError = true;
		}
		if (openAccount.getProductTypeId() <= 0) {
			String msg = message.get("app.accountInfo.account.productType.validate");
			context.addMessage("productType", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,null));
			productTypeBind.setValid(false);
			isError = true;
		}
		
		if (openAccount.getNames().isEmpty()) {
			String msg = message.get("app.accountInfo.account.accountName.validate.required");
			context.addMessage("dep", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,null));
			isError = true;
		}
		boolean purposeError = true;
		for (OpenAccountPurposeView purpose : openAccount.getPurposes()) {
			if (purpose.isChecked()) {
				purposeError = false;
				break;
			}
		}
		if (purposeError) {
			String msg = message.get("app.accountInfo.account.purpose.validate.required");
			context.addMessage("dep", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,null));
			isError = true;
		}
		
		boolean creditError = true;
		for (OpenAccountCreditView credit : openAccount.getCredits()) {
			if (credit.isChecked()) {
				creditError = false;
				break;
			}
		}
		if (creditError) {
			String msg = message.get("app.accountInfo.account.creditType.validate.required");
			context.addMessage("dep", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,null));
			isError = true;
		}
		return !isError;
		*/
	}
	private void _calculateDisplayField() {
		if (openAccount == null)
			return;
		if (openAccount.getAccountTypeId() > 0) {
			openAccount.setDisplayAccountType(_getDisplayValue(accountTypes, openAccount.getAccountTypeId()));
		} else {
			openAccount.setDisplayAccountType(null);
		}
		if (openAccount.getProductTypeId() > 0) {
			openAccount.setDisplayProductType(_getDisplayValue(productTypes, openAccount.getProductTypeId()));
		} else {
			openAccount.setDisplayProductType(null);
		}
		if (openAccount.getBranchId() > 0) {
			openAccount.setDisplayBranch(_getDisplayValue(branches, openAccount.getBranchId()));
		} else {
			openAccount.setDisplayBranch(null);
		}
		if (openAccount.getRequestAccountType() == null)
			openAccount.setRequestAccountType(RequestAccountType.NA);
	}
	private String _getDisplayValue(List<SelectItem> list,Object value) {
		String check = value.toString();
		for (SelectItem item : list) {
			if (check.equals(item.getValue().toString())){
				return item.getLabel();
			}
		}
		return null;
	}
	
	private void _recalculateAccountNameList() {
		selectedAddAccountNameId = -1;
		if (openAccount == null) {
			toAddAccountNameList = null;
			return;
		}
		toAddAccountNameList = new ArrayList<SelectItem>();
		List<OpenAccountNameView> currNames = openAccount.getNames();
		for (OpenAccountNameView name : allNameList) {
			boolean isFound = false;
			for (OpenAccountNameView currName : currNames) {
				if (currName.getCustomerId() == name.getCustomerId()) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				SelectItem item = new SelectItem();
				item.setLabel(name.getName());
				item.setDescription(name.getName());
				item.setValue(name.getCustomerId());
				toAddAccountNameList.add(item);
			}
		}
	}
	private void _prepareDropdown() {
		//for product type
		if (openAccount == null)
			return;
		if (openAccount.getAccountTypeId() <= 0)
			return;
		productTypes = accountInfoControl.getProductTypes(openAccount.getAccountTypeId());
		if (productTypes == null || productTypes.isEmpty())
			openAccount.setProductTypeId(0);
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
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.AccountInfo, ownerCaseUserId);
		List<FieldsControlView> dialogFields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.AddAccountInfoDialog, ownerCaseUserId);
		
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
	public String mandateDialog(String name) {
		boolean isMandate = FieldsControlView.DEFAULT_MANDATE;
		FieldsControlView field = dialogFieldMap.get(name);
		if (field != null)
			isMandate = field.isMandate();
		return isMandate ? " *" : "";
	}
	public boolean isDialogDisable(String name) {
		FieldsControlView field = dialogFieldMap.get(name);
		if (field == null)
			return FieldsControlView.DEFAULT_READONLY;
		return field.isReadOnly();
	}
}
