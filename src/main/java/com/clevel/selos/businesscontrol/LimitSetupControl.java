package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.ProposeCreditInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ProposeCreditInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.LimitSetupView;
import com.clevel.selos.model.view.ProposeCreditInfoDetailView;
import com.clevel.selos.transform.ProposeLineTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LimitSetupControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    BPMInterface bpmInterface;
  
    
    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    ProposeCreditInfoDAO newCreditDetailDAO;
    
    @Inject
    ProposeLineTransform newCreditDetailTransform;
    
    @Inject
    public LimitSetupControl(){

    }
    
    public LimitSetupView getLimitSetupView( long workCaseId ){
    	LimitSetupView limitSetupView = new LimitSetupView();
    	WorkCase workCase = workCaseDAO.findById(workCaseId);
    	limitSetupView.setModifyBy(workCase.getModifyBy());
    	limitSetupView.setModifyDate(workCase.getModifyDate());
    	List<ProposeCreditInfo> newCreditDetailList = newCreditDetailDAO.findApprovedNewCreditDetail(workCaseId);
    	List<ProposeCreditInfoDetailView> newCreditDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
    	for (ProposeCreditInfo newCreditDetail : newCreditDetailList){
            ProposeCreditInfoDetailView newCreditDetailView = newCreditDetailTransform.transformProposeCreditToView(newCreditDetail);
    		newCreditDetailViewList.add(newCreditDetailView);
    	}
    	limitSetupView.setNewCreditDetailViewList(newCreditDetailViewList);
    	return limitSetupView;
    }
    
    public void saveLimitSetup(LimitSetupView limitSetupView){
    	for (ProposeCreditInfoDetailView newCreditDetailView : limitSetupView.getNewCreditDetailViewList()){
            ProposeCreditInfo newCreditDetail = newCreditDetailDAO.findById(newCreditDetailView.getId());
    		newCreditDetail.setSetupCompleted(newCreditDetailView.getSetupCompleted());
    		newCreditDetailDAO.save(newCreditDetail);
    	}
    }
 
}
