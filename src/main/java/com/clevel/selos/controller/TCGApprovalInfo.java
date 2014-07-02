package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.TCGInfoControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.model.view.TCGInfoView;	
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@ViewScoped
@ManagedBean(name = "tcgApprovalInfo")
public class TCGApprovalInfo implements Serializable {

    @Inject
    @SELOS
    Logger log;
    
    // session
 	private boolean preRenderCheck = false;
 	private long workCaseId = -1;
 	private long stepId = -1;
 	private User user;
   
    private BasicInfoView basicInfoView;
    
    
    private TCGInfoView tcgInfoView;
    
    private boolean isEnableSubmit = true;
    private boolean isEnableApprove = true;

    @Inject
    private TCGInfoControl tcgInfoControl;
    
    @Inject
	private BasicInfoControl basicInfoControl;
    
    @Inject
	private UserAccessControl userAccessControl;
//    @Inject
//    UserDAO userDAO;

    public TCGApprovalInfo() {
    }
    
    public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;

		String redirectPage = null;
		log.info("preRender workCase Id = " + workCaseId);
		if (workCaseId > 0) {
			if (!userAccessControl.canUserAccess(Screen.TCGInfo, stepId)) {
				redirectPage = "/site/inbox.jsf";
			} else {
				return;
			}
		}
		_loadInitData();
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

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        log.info("Construct");
		HttpSession session = FacesUtil.getSession(false);
		if (session != null) {
			workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
			stepId = Util.parseLong(session.getAttribute("stepId"), -1);
			user = (User) session.getAttribute("user");
		}
		_loadInitData();
		this.setTcgInfoView(this.tcgInfoControl.getTCGInfoView(workCaseId));
		
		//Disable Button
		/*if (stepId == 0){
			this.isEnableSubmit = true;
			this.isEnableApprove = false;
		}else{
			this.isEnableSubmit = false;
			this.isEnableApprove = true;
		}*/
		_loadFieldControl();
    }
    
    public void onSaveTcgInfo() {
        log.info("onSaveTcgInfo ::: workCaseId  {}", workCaseId);
        log.info("onSaveTcgInfo ::: stepId  {}", stepId);
        this.tcgInfoControl.onSaveTCGInfo(tcgInfoView, workCaseId, user);
       
    }


    public void onCancelTcgInfo() {
        log.info("onCancelTcgInfo ::: ");

        onCreation();
    }
   
    public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}

	public Date getApproveDate() {
		return this.getTcgInfoView().getApproveDate();
	}
	
	public Date getLastUpdateDateTime() {
		//TODO 
		if (tcgInfoView.getModifyDate() != null){
			return tcgInfoView.getModifyDate();
		}
		return new Date();
	}
	public String getLastUpdateBy() {
		//TODO
		//return user.getDisplayName();
		if (tcgInfoView.getModifyBy() != null){
			return tcgInfoView.getModifyBy().getDisplayName();
		}		
		return "";
	}

	public TCGInfoView getTcgInfoView() {
		return tcgInfoView;
	}

	public void setTcgInfoView(TCGInfoView tcgInfoView) {
		this.tcgInfoView = tcgInfoView;
	}

	public boolean getIsEnableSubmit() {
		return isEnableSubmit;
	}

	public boolean getIsEnableApprove() {
		return isEnableApprove;
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
	@Inject MandatoryFieldsControl mandatoryFieldsControl;
	private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
	private void _loadFieldControl() {
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.TCGInfo);
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

