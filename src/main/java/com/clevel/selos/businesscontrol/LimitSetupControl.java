package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.LimitSetupView;
import com.clevel.selos.model.view.NewCreditDetailView;
import com.clevel.selos.transform.NewCreditDetailTransform;
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
    NewCreditDetailDAO newCreditDetailDAO;
    
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;
    
    @Inject
    public LimitSetupControl(){

    }
    
    public LimitSetupView getLimitSetupView( long workCaseId ){
    	LimitSetupView limitSetupView = new LimitSetupView();
    	WorkCase workCase = workCaseDAO.findById(workCaseId);
    	limitSetupView.setModifyBy(workCase.getModifyBy());
    	limitSetupView.setModifyDate(workCase.getModifyDate());
    	List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findApprovedNewCreditDetail(workCaseId);
    	List<NewCreditDetailView> newCreditDetailViewList = new ArrayList<NewCreditDetailView>();
    	for (NewCreditDetail newCreditDetail : newCreditDetailList){
    		NewCreditDetailView newCreditDetailView = newCreditDetailTransform.transformToView(newCreditDetail);
    		newCreditDetailViewList.add(newCreditDetailView);
    	}
    	limitSetupView.setNewCreditDetailViewList(newCreditDetailViewList);
    	return limitSetupView;
    }
    
    public void saveLimitSetup(LimitSetupView limitSetupView){
    	for (NewCreditDetailView newCreditDetailView : limitSetupView.getNewCreditDetailViewList()){
    		NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(newCreditDetailView.getId());
    		newCreditDetail.setSetupCompleted(newCreditDetailView.getIsSetupCompleted());
    		newCreditDetailDAO.save(newCreditDetail);
    	}
    }
 
}
