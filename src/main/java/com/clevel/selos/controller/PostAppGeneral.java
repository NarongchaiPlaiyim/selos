package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.view.ReturnInfoView;
import com.clevel.selos.model.view.UserAccessView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ViewScoped
@ManagedBean(name = "postAppGeneral")
public class PostAppGeneral implements Serializable  {
	private static final long serialVersionUID = 8040267511396703345L;
	
	@Inject private GeneralPeopleInfoControl generalPeopleInfoControl;
	@Inject @NormalMessage
	private Message message;
	@Inject @SELOS
	private Logger log;
	@Inject
	private HeaderController headerController;
	@Inject
	private PostAppBusinessControl postAppBusinessControl;
	@Inject
	private UserAccessControl userAccessControl;
	@Inject
	private ReturnControl returnControl;
	@Inject
	private ReasonDAO reasonDAO;

	private long workCaseId = -1;
	private long stepId = -1;
	private long statusId = -1;
	private String wobNumber = "";
	private String queueName = "";
	
	//Submit02 Dialog value
	private String submit02_Remark;
	
	//Return 01 Dialog
	private List<ReturnInfoView> returnList;
	private ReturnInfoView selectedReturnInfo;
	private String return01_Remark;
	private int return01_SelectedReasonId;
	
	//Cancel01 Dialog
	private String cancel01_Remark;
	private int cancel01_SelectedReasonId;
	
	private List<SelectItem> returnReasonList;
	private List<SelectItem> cancelReasonList;
	
	private final HashSet<Integer> accessSet = new HashSet<Integer>();
	
	
	public String getCancel01_Remark() {
		return cancel01_Remark;
	}
	public void setCancel01_Remark(String cancel01_Remark) {
		this.cancel01_Remark = cancel01_Remark;
	}
	public String getReturn01_Remark() {
		return return01_Remark;
	}
	public void setReturn01_Remark(String return01_Remark) {
		this.return01_Remark = return01_Remark;
	}
	public void setReturn01_SelectedReasonId(int return01_SelectedReasonId) {
		this.return01_SelectedReasonId = return01_SelectedReasonId;
	}
	public int getReturn01_SelectedReasonId() {
		return return01_SelectedReasonId;
	}
	public String getSubmit02_Remark() {
		return submit02_Remark;
	}
	public void setSubmit02_Remark(String submit02_Remark) {
		this.submit02_Remark = submit02_Remark;
	}
	public int getCancel01_SelectedReasonId() {
		return cancel01_SelectedReasonId;
	}
	public void setCancel01_SelectedReasonId(int cancel01_SelectedReasonId) {
		this.cancel01_SelectedReasonId = cancel01_SelectedReasonId;
	}
	
	public List<SelectItem> getCancelReasonList() {
		return cancelReasonList;
	}
	public List<SelectItem> getReturnReasonList() {
		return returnReasonList;
	}
	public List<ReturnInfoView> getReturnList() {
		return returnList;
	}
	public void setReturnList(List<ReturnInfoView> returnList) {
		this.returnList = returnList;
	}
	public ReturnInfoView getSelectedReturnInfo() {
		return selectedReturnInfo;
	}
	public void setSelectedReturnInfo(ReturnInfoView selectedReturnInfo) {
		this.selectedReturnInfo = selectedReturnInfo;
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
			queueName = Util.parseString(session.getAttribute("queueName"), "");
			wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");
			statusId = Util.parseLong(session.getAttribute("statusId"), 0);
			
			List<UserAccessView> accessList = userAccessControl.getUserAccessList(stepId);
			for (UserAccessView access : accessList) {
				if (access.isAccessFlag())
					accessSet.add(access.getScreenId());
			}
		}
		returnReasonList = generalPeopleInfoControl.listReturnReasons();
		cancelReasonList = generalPeopleInfoControl.listCancelReasons();
		
		
	}
	
	public void onOpenReturnDialog() {
		
		//loading prelist
		returnList = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId,0,stepId);
		if (returnList == null)
			returnList = new ArrayList<ReturnInfoView>();
		log.debug("On Open Return Dialog ["+returnList.size()+"]");
		return01_Remark = "";
		return01_SelectedReasonId = -1;
		selectedReturnInfo = null;
	}
	public void onOpenReturnAddCodeDialog(boolean isNew) {
		if (!isNew && selectedReturnInfo != null) {
			return01_Remark = selectedReturnInfo.getReasonDetail();
			return01_SelectedReasonId = selectedReturnInfo.getReasonId();
		} else {
			return01_Remark = "";
			return01_SelectedReasonId = -1;
		}
	}
	public void onSaveReturnReason() {
		Reason reason = reasonDAO.findById(return01_SelectedReasonId);
		if (reason == null) {
			String msgStr = "Error : Cannot found return reason";
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,msgStr,msgStr);
			FacesContext.getCurrentInstance().addMessage(null,msg); 
			return;
		}
		log.debug("On Save Return Reason");
		if (selectedReturnInfo == null) {
			selectedReturnInfo = new ReturnInfoView();
			returnList.add(selectedReturnInfo);
		}
		
		selectedReturnInfo.setReturnCode(reason.getCode());
		selectedReturnInfo.setDescription(reason.getDescription());
		selectedReturnInfo.setReasonDetail(return01_Remark);
		selectedReturnInfo.setReasonId(reason.getId());
		selectedReturnInfo.setCanEdit(true);
		
		return01_Remark = "";
		return01_SelectedReasonId = -1;
		selectedReturnInfo = null;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onDeleteReturnReason() {
		if (selectedReturnInfo == null)
			return;
		returnList.remove(selectedReturnInfo);
		return01_Remark = "";
		return01_SelectedReasonId = -1;
		selectedReturnInfo = null;
	}
	
	/*
	 * 
	 */
	public boolean isPostAppStage() {
		return stepId / 1000 == 3; // 3xxx
	}
	
	public void onSubmitCase() {
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
		FacesUtil.redirect("/site/inbox_dev.jsf");
	}
	public void onSubmitCaseFail() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"TEST Error : Error","TEST Error : Error");
		FacesContext.getCurrentInstance().addMessage(null,msg); 
	}
	
	
	public void onSubmitCA() {
		try {
			postAppBusinessControl.submitCA(workCaseId, queueName,wobNumber, null, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onSubmitCAWithRemark() {
		try {
			postAppBusinessControl.submitCA(workCaseId, queueName,wobNumber, submit02_Remark, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	
	public void onReturnToBDM() {
		try {
			if (returnList == null || returnList.isEmpty()) {
				String message = "Please select at least one return reason";
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,message,message);
				FacesContext.getCurrentInstance().addMessage(null,msg);
				return;
			}
			postAppBusinessControl.returnToBDM(workCaseId, queueName,wobNumber, returnList, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	
	public void onReturnUW2() {
		try {
			if (returnList == null || returnList.isEmpty()) {
				String message = "Please select at least one return reason";
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,message,message);
				FacesContext.getCurrentInstance().addMessage(null,msg);
				return;
			}
			postAppBusinessControl.returnToUW2(workCaseId, queueName,wobNumber,returnList, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onReturnDataEntry() {
		try {
			if (returnList == null || returnList.isEmpty()) {
				String message = "Please select at least one return reason";
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,message,message);
				FacesContext.getCurrentInstance().addMessage(null,msg);
				return;
			}
			postAppBusinessControl.returnToDataEntry(workCaseId, queueName,wobNumber, returnList, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onReturnToContactCenter() {
		try {
			if (returnList == null || returnList.isEmpty()) {
				String message = "Please select at least one return reason";
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,message,message);
				FacesContext.getCurrentInstance().addMessage(null,msg);
				return;
			}
			postAppBusinessControl.returnToContactCenter(workCaseId, queueName,wobNumber, returnList, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onReturnToLARBC() {
		try {
			if (returnList == null || returnList.isEmpty()) {
				String message = "Please select at least one return reason";
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,message,message);
				FacesContext.getCurrentInstance().addMessage(null,msg);
				return;
			}
			postAppBusinessControl.returnToLARBC(workCaseId, queueName,wobNumber, returnList, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onCancelCA() {
		try {
			postAppBusinessControl.cancelCA(workCaseId, queueName,wobNumber, cancel01_SelectedReasonId, cancel01_Remark, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onCancelDisbursement() {
		try {
			postAppBusinessControl.cancelDisbursement(workCaseId, queueName,wobNumber, cancel01_SelectedReasonId, cancel01_Remark, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onRequestPriceReduction() {
		try {
			postAppBusinessControl.requestPriceReduction(workCaseId, queueName,wobNumber, submit02_Remark, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onGenerateAgreement() {
		try {
			postAppBusinessControl.generateAgreement(workCaseId, queueName,wobNumber, submit02_Remark, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onRegenerateAgreement() {
		try {
			postAppBusinessControl.regenerateAgreement(workCaseId, queueName,wobNumber, submit02_Remark, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	public void onDataEntryComplete() {
		try {
			postAppBusinessControl.dataEntryComplete(workCaseId, queueName,wobNumber, submit02_Remark, stepId);
			
			_manageComplete();
		} catch (Exception e) {
			_manageError(e);
		}
	}
	
	public boolean showSubmitCAWithRemark() {
		if (!headerController.checkButton("Submit CA"))
			return false;
		return !_notShowRemark();
	}
	public boolean showSubmitCAWithoutRemark() {
		if (!headerController.checkButton("Submit CA"))
			return false;
		return _notShowRemark();
	}
	
	/*
	 * Menu Management
	 */
	public boolean canAccessMenu(int screenValue) {
		return accessSet.contains(screenValue);
	}
	public boolean showGroupSummary() {
		return accessSet.contains(Screen.EXECUTIVE_SUMMARY.value()) || accessSet.contains(Screen.DECISION.value());
	}
	public boolean showGroupRegister() {
		return accessSet.contains(Screen.PostCustomerInfoSum.value()) || accessSet.contains(Screen.AccountInfo.value()) 
				|| accessSet.contains(Screen.ApproveDetailInfo.value()) || accessSet.contains(Screen.CollateralMortgageInfoSum.value())
				|| accessSet.contains(Screen.Disbursement.value());
	}
	public boolean showGroupInsurance() {
		return accessSet.contains(Screen.InsuranceInfo.value()) || accessSet.contains(Screen.BAInfo.value());
	}
	public boolean showGroupConfirmation() {
		return accessSet.contains(Screen.AgreementAndMortgageConfirm.value()) || accessSet.contains(Screen.AgreementSign.value()) 
				|| accessSet.contains(Screen.PledgeConfirm.value()) || accessSet.contains(Screen.PerfectionReview.value());
	}
	public String getDefaultOutcomeGroupSummary() {
		if (!showGroupSummary())
			return "";
		if (canAccessMenu(Screen.EXECUTIVE_SUMMARY.value()))
			return "exSummary";
		else
			return "decision";
	}
	public String getDefaultOutcomeGroupRegister() {
		if (!showGroupRegister())
			return "";
		if (canAccessMenu(Screen.CollateralMortgageInfoSum.value()))
			return "mortgageSummary";
		else if (canAccessMenu(Screen.AccountInfo.value()))
			return "accountInfo";
		else if (canAccessMenu(Screen.PostCustomerInfoSum.value()))
			return "postCustomerInfoSummary";
		else if (canAccessMenu(Screen.ApproveDetailInfo.value())) 
			return "approveDetailInformation";
		else 
			return "disbursement";
			
	}
	public String getDefaultOutcomeGroupInsurance() {
		if (!showGroupInsurance())
			return "";
		if (canAccessMenu(Screen.InsuranceInfo.value()))
			return "insuranceInfo";
		else
			return "baInfo";
	}
	public String getDefaultOutcomeGroupConfirmation() {
		if (!showGroupConfirmation())
			return "";
		if (canAccessMenu(Screen.PerfectionReview.value()))
			return "perfectionReview";
		else if (canAccessMenu(Screen.AgreementAndMortgageConfirm.value()))
			return "mortgageConfirm";
		else if (canAccessMenu(Screen.AgreementSign.value()))
			return "agreementSign";
		else
			return "pledgeConfirm";
	}
	
	
	
	private boolean _notShowRemark() {
		int stepIdI = (int) stepId;
		switch (stepIdI) {
		case 3006: //Inform Customer
			return true;
		case 3004 : //Request to get Customer Acceptance
			return (statusId == 90006 || //CA Approved by UW2
					statusId == 30001 || //Request to get  Customer acceptance  - Contact Center(#1)
					statusId == 20031 || //Price/Fee Reduction Approved
					statusId == 20030);	//Price/Fee Reduction Rejected
		case 3008 : //Check Doc
			return true;
		case 3011 : //Key in Data
			return statusId == 30008; //Key in Data
		case 3015 : //Request to Confirm Customer acceptance
			return true;
		case 3045 : //Review Perfection
			return statusId == 30043 || //Review Perfection of Agreement
				statusId == 20007; //Reply From BDM
		case 3023 : //Create/Update Customer Profile
			boolean openAcc = false;
			if (statusId == 30020) //Customer accepted the final Approval
				return openAcc;
			if (statusId == 20007) //Reply From BDM
				return openAcc;
		case 3037 : //Reviewed Signed Agreemen
			boolean pledgeReq = false;
			if (statusId == 30037) //Agreement Sign Completed
				return pledgeReq;
		}
		return false;
	}

	private void _manageComplete() {
		_clearAllInput();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
		FacesUtil.redirect("/site/inbox.jsf");
	}
	private void _manageError(Exception e) {
		String message = Util.getMessageException(e);
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,message,message);
		FacesContext.getCurrentInstance().addMessage(null,msg);
		_clearAllInput();
	}
	private void _clearAllInput() {
		submit02_Remark = null;
		return01_Remark = null;
		return01_SelectedReasonId = -1;
		cancel01_Remark = null;
		cancel01_SelectedReasonId = -1;
		selectedReturnInfo = null;
	}
}
