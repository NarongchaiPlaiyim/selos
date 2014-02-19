package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.InsuranceInfoControl;
import com.clevel.selos.businesscontrol.PreDisbursementControl;
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
import com.clevel.selos.model.view.PreDisbursementView;
import com.clevel.selos.model.view.TCGDetailView;
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
@ManagedBean(name = "preDisbursement")
public class PreDisbursement implements Serializable {

    @Inject
    @SELOS
    Logger log;
    
    private long workCaseId = -1;
	private long stepId = -1;
	private User user;
    
    private BasicInfoView basicInfoView;
    
    
    @Inject
    private PreDisbursementControl preDisbursementControl;
        
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
			//preDisbursementView = this.preDisbursementControl.getPreDisbursementView(workCaseId);
		}

    }

    public void onRadioChange(){
    	if (this.preDisbursementView.getApprovedType() == 0){
    		this.preDisbursementView.setApprovedType(1);
    	}else{
    		this.preDisbursementView.setApprovedType(0);
    	}
    }
    

    public void onSaveTcgInfo() {
        //log.info("onSaveTcgInfo ::: ModeForDB  {}", modeForDB);
        

    }


    public void onCancelTcgInfo() {
        //modeForDB = ModeForDB.CANCEL_DB;
        log.info("onCancelTcgInfo ::: ");

        onCreation();
    }
   
    public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}
	
	public Date getLastUpdateDateTime() {
		//TODO 
		return new Date();
	}
	public String getLastUpdateBy() {
		//TODO
		return user.getDisplayName();
	}

	public PreDisbursementView getPreDisbursementView() {
		return preDisbursementView;
	}

	public void setPreDisbursementView(PreDisbursementView preDisbursementView) {
		this.preDisbursementView = preDisbursementView;
	}

}

