package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.PledgeACDepView;
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

	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private String fromPage;
	private long pledgeId = -1;
	private User user;
	private BasicInfoView basicInfoView;
	private List<PledgeACDepView> deleteList;
	
	//Property
	private List<PledgeACDepView> pledgeACDepList;
	private PledgeACDepView pledgeACDepView;
	private int deletedRowId;
	private boolean addDialog = false;

    public PledgeDetail(){
    	
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
	
	public List<PledgeACDepView> getPledgeACDepList() {
		return pledgeACDepList;
	}
	public PledgeACDepView getPledgeACDepView() {
		return pledgeACDepView;
	}
	public void setPledgeACDepView(PledgeACDepView pledgeACDepView) {
		this.pledgeACDepView = pledgeACDepView;
	}
	public int getDeletedRowId() {
		return deletedRowId;
	}
	public void setDeletedRowId(int deletedRowId) {
		this.deletedRowId = deletedRowId;
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
		Map<String,Object> params =  FacesUtil.getParamMapFromFlash("pledgeParams");
		fromPage = (String)params.get("fromPage");
		pledgeId = Util.parseLong(params.get("pledgeId"),-1);
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
		addDialog = false;	
	}
	public void onAddPledgeACDep() {
		pledgeACDepList.add(pledgeACDepView);
		pledgeACDepView = null;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onUpdatePledgeACDep() {
		pledgeACDepView.setNeedUpdate(true);
		pledgeACDepView = null;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onDeletePledgeACDep() {
		if (deletedRowId < 0 || deletedRowId > pledgeACDepList.size())
			return;
		PledgeACDepView delete = pledgeACDepList.remove(deletedRowId);
		if (delete != null)
			deleteList.add(delete);
		deletedRowId = -1;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	public void onSavePledgeDetail() {
		
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
		//TODO Load pledge info by using workcase and pledgeId
		//TODO Load pledge AC 
		pledgeACDepList = new ArrayList<PledgeACDepView>();
		deleteList = new ArrayList<PledgeACDepView>();
		pledgeACDepView = null;
		deletedRowId = -1;
	}
}
