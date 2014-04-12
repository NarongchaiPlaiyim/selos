package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.PledgeDetailControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.model.view.PledgeACDepView;
import com.clevel.selos.model.view.PledgeInfoFullView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name = "pledgeDetail")
public class PledgeDetail implements Serializable {
    private static final long serialVersionUID = 4686000947793273583L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	@Inject
	private PledgeDetailControl pledgeDetailControl;
	@Inject @NormalMessage
	private Message message;
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private long stageId = -1;
	private long pledgeId = -1;
	private BasicInfoView basicInfoView;
	private List<PledgeACDepView> deleteList;
	
	
	//Property
	private List<PledgeACDepView> pledgeACDepList;
	private PledgeACDepView pledgeACDepView;
	private PledgeInfoFullView pledgeInfoView;
	private int selectedRowId;
	private boolean addDialog = false;

    public PledgeDetail(){
    }
    public Date getLastUpdateDateTime() {
		return pledgeInfoView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (pledgeInfoView.getModifyBy() != null)
			return pledgeInfoView.getModifyBy().getDisplayName();
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
	
	public List<PledgeACDepView> getPledgeACDepList() {
		return pledgeACDepList;
	}
	public PledgeACDepView getPledgeACDepView() {
		return pledgeACDepView;
	}
	public int getSelectedRowId() {
		return selectedRowId;
	}
	public void setSelectedRowId(int selectedRowId) {
		this.selectedRowId = selectedRowId;
	}
	public int getNumberOfPledgeAC() {
		if (pledgeACDepList == null)
			return 0;
		else
			return pledgeACDepList.size();
	}
	public BigDecimal getTotalHoldAmount() {
		BigDecimal summ = new BigDecimal(0);
		if (pledgeACDepList != null) {
			for (PledgeACDepView pledgeAC : pledgeACDepList) {
				if (pledgeAC.getHoldAmount() == null)
					continue;
				summ = summ.add(pledgeAC.getHoldAmount());
			}
		}
		return summ;
	}
	public boolean isAddDialog() {
		return addDialog;
	}
	public PledgeInfoFullView getPledgeInfoView() {
		return pledgeInfoView;
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
		Map<String,Object> params =  FacesUtil.getParamMapFromFlash("pledgeParams");
		pledgeId = Util.parseLong(params.get("pledgeId"),-1);
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
				if (pledgeId <= 0) {
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
	
	public void onOpenAddPledgeACDepDialog() {
		pledgeACDepView = new PledgeACDepView();
		pledgeACDepView.setId(0);
		pledgeACDepView.setHoldAmount(new BigDecimal(0));
		pledgeACDepView.setDep("");
		
		addDialog = true;
	}
	public void onOpenUpdatePledgeACDepDialog() {
		pledgeACDepView = new PledgeACDepView();
		PledgeACDepView upd = null;
		if (selectedRowId >= 0 && selectedRowId < pledgeACDepList.size()) {
			upd = pledgeACDepList.get(selectedRowId);
		}
		if (upd == null) {
			pledgeACDepView.setId(0);
			pledgeACDepView.setHoldAmount(new BigDecimal(0));
			pledgeACDepView.setDep("");
		} else {
			pledgeACDepView.setId(upd.getId());
			pledgeACDepView.setHoldAmount(upd.getHoldAmount());
			pledgeACDepView.setDep(upd.getDep());
		}
		addDialog = false;	
	}
	public void onAddPledgeACDep() {
		if (!_validateACDep()) {
			
			RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
			return;
		}
		pledgeACDepList.add(pledgeACDepView);
		pledgeACDepView = null;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onUpdatePledgeACDep() {
		if (!_validateACDep()) {
			RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
			return;
		}
		PledgeACDepView upd = pledgeACDepList.get(selectedRowId);
		upd.setDep(pledgeACDepView.getDep());
		upd.setHoldAmount(pledgeACDepView.getHoldAmount());
		upd.setNeedUpdate(true);
		selectedRowId = -1;
		pledgeACDepView = null;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onDeletePledgeACDep() {
		if (selectedRowId < 0 || selectedRowId > pledgeACDepList.size())
			return;
		PledgeACDepView delete = pledgeACDepList.remove(selectedRowId);
		if (delete != null && delete.getId() > 0)
			deleteList.add(delete);
		selectedRowId = -1;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	public void onSavePledgeDetail() {
		pledgeDetailControl.savePledgeDetail(pledgeInfoView, pledgeACDepList,deleteList);
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onCancelPledge() {
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
		
		pledgeInfoView = pledgeDetailControl.getPledgeInfoFull(pledgeId);
		if (pledgeInfoView.getId() <= 0 || pledgeInfoView.getWorkCaseId() != workCaseId) {
			String redirectPage = "/site/mortgageSummary.jsf";
			try {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				ec.redirect(ec.getRequestContextPath()+redirectPage);
			} catch (IOException e) {
				log.error("Fail to redirect screen to "+redirectPage,e);
			}
		}
		List<PledgeACDepView> list = pledgeDetailControl.getPledgeACList(pledgeId);
		if (list == null)
			pledgeACDepList = new ArrayList<PledgeACDepView>();
		else
			pledgeACDepList = new ArrayList<PledgeACDepView>(list);
		deleteList = new ArrayList<PledgeACDepView>();
		pledgeACDepView = null;
		selectedRowId = -1;
	}
	
	private boolean _validateACDep() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean isError = false;
		// validate dep
		if (Util.isEmpty(pledgeACDepView.getDep())) {
			String msg = message.get("app.pledgeDetail.dep.validate");
			context.addMessage("dep", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,null));
			isError = true;
		}
		//validate hold amount
		if (pledgeACDepView.getHoldAmount().compareTo(BigDecimal.ZERO) <= 0) {
			String msg = message.get("app.pledgeDetail.holdAmount.validate.positive");
			context.addMessage("holdamount", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,null));
			isError = true;
		}
		if (isError)
			return false;
		
		BigDecimal newHoldAmount = new BigDecimal(0);
		newHoldAmount = newHoldAmount.add(pledgeACDepView.getHoldAmount());
		
		for (int i=0;i<pledgeACDepList.size();i++) {
			if (!addDialog) { //update dialog
				if (i == selectedRowId) //same row
					continue;
			}
			PledgeACDepView ac = pledgeACDepList.get(i);
			newHoldAmount = newHoldAmount.add(ac.getHoldAmount());
			if (ac.getDep().equals(pledgeACDepView.getDep())) {
				String msg = message.get("app.pledgeDetail.dep.validate.duplicate");
				context.addMessage("dep", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,null));
				return false;
			}
		}
		
		if (newHoldAmount.compareTo(pledgeInfoView.getPledgeAmount()) > 0) {
			String msg = message.get("app.pledgeDetail.holdAmount.validate.amount");
			context.addMessage("dep", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,null));
			return false;
		}
		return true;
	}
	
	/*
	 * Mandate and read-only
	 */
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private final HashMap<String, FieldsControlView> dialogFieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.PledgeDetail);
		List<FieldsControlView> dialogFields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.AddDepInfoDialog);
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
	public boolean isDialogDisable(String name) {
		FieldsControlView field = dialogFieldMap.get(name);
		if (field == null)
			return FieldsControlView.DEFAULT_READONLY;
		return field.isReadOnly();
	}
}
