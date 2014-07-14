package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.PreDisbursementControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.model.view.PreDisbursementView;
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
@ManagedBean(name = "preDisbursement")
public class PreDisbursement implements Serializable {

    @Inject
    @SELOS
    Logger log;
    
    private boolean preRenderCheck = false;
 	private long workCaseId = -1;
 	private long stepId = -1;
 	private User user;
    
    private BasicInfoView basicInfoView;
    
    
    @Inject
    private PreDisbursementControl preDisbursementControl;
    
    @Inject
	private UserAccessControl userAccessControl;
    
    @Inject
	private BasicInfoControl basicInfoControl;
        
    private PreDisbursementView preDisbursementView = new PreDisbursementView();
    
//    @Inject
//    UserDAO userDAO;

    public PreDisbursement() {
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
			preDisbursementView = this.preDisbursementControl.getPreDisbursementView(workCaseId);
		}
		_loadInitData();
		_loadFieldControl();
    }
  
    public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;

		String redirectPage = null;
		log.info("preRender workCase Id = " + workCaseId);
		if (workCaseId > 0) {
			if (!userAccessControl.canUserAccess(Screen.PreDisbursement, stepId)) {
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

    public void onSavePreDisbursement() {
        this.preDisbursementControl.savePreDisbursement(preDisbursementView, workCaseId, user);
        this.onCreation();

    }
   
    public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}
	
	public Date getLastUpdateDateTime() {
		//TODO 
		if (preDisbursementView.getModifyDate() != null){
			return preDisbursementView.getModifyDate();
		}
		return new Date();
	}
	public String getLastUpdateBy() {
		//TODO
		if (preDisbursementView.getModifyBy() != null){
			return preDisbursementView.getModifyBy().getDisplayName();
		}		
		return "";
	}

	public PreDisbursementView getPreDisbursementView() {
		return preDisbursementView;
	}

	public void setPreDisbursementView(PreDisbursementView preDisbursementView) {
		this.preDisbursementView = preDisbursementView;
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
		List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.PreDisbursement);
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

