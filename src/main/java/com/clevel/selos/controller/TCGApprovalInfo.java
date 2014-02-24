package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.TCGInfoControl;
import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGInfoView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ViewScoped
@ManagedBean(name = "tcgApprovalInfo")
public class TCGApprovalInfo implements Serializable {

    @Inject
    @SELOS
    Logger log;
    
    private long workCaseId = -1;
	private long stepId = -1;
	private User user;
   
    private BasicInfoView basicInfoView;
    
    
    private TCGInfoView tcgInfoView;
    
    private boolean isEnableSubmit = true;
    private boolean isEnableApprove = true;

    @Inject
    TCGInfoControl tcgInfoControl;
//    @Inject
//    UserDAO userDAO;

    public TCGApprovalInfo() {
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
		this.setTcgInfoView(this.tcgInfoControl.getTCGInfoView(workCaseId));
		
		//Disable Button
		if (stepId == 2222){
			this.isEnableSubmit = true;
			this.isEnableApprove = false;
		}else{
			this.isEnableSubmit = false;
			this.isEnableApprove = true;
		}
		
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
		return tcgInfoView.getCreateDate();
	}
	public String getLastUpdateBy() {
		//TODO
		//return user.getDisplayName();
		if (tcgInfoView.getModifyBy() != null){
			return tcgInfoView.getModifyBy().getDisplayName();
		}		
		return tcgInfoView.getCreateBy().getDisplayName();
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

}

