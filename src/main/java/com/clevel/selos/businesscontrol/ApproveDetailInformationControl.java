package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.ApproveDetailInformationView;
import com.clevel.selos.model.view.NewCreditDetailView;
import com.clevel.selos.transform.NewCreditDetailTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ApproveDetailInformationControl extends BusinessControl {
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
    public ApproveDetailInformationControl(){

    }
    
    public ApproveDetailInformationView getApproveDetailInformationView( long workCaseId ){
    	ApproveDetailInformationView approveDetailInformationView = new ApproveDetailInformationView();
    	WorkCase workCase = workCaseDAO.findById(workCaseId);
    	approveDetailInformationView.setModifyBy(workCase.getModifyBy());
    	approveDetailInformationView.setModifyDate(workCase.getModifyDate());
    	List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findApprovedNewCreditDetail(workCaseId);
    	List<NewCreditDetailView> newCreditDetailViewList = new ArrayList<NewCreditDetailView>();
    	for (NewCreditDetail newCreditDetail : newCreditDetailList){
    		NewCreditDetailView newCreditDetailView = newCreditDetailTransform.transformToView(newCreditDetail);
    		newCreditDetailViewList.add(newCreditDetailView);
    	}
    	approveDetailInformationView.setNewCreditDetailViewList(newCreditDetailViewList);
    	return approveDetailInformationView;
    }
    
    public void saveApproveDetailInformationView(ApproveDetailInformationView approveDetailInformationView){
    	for (NewCreditDetailView newCreditDetailView : approveDetailInformationView.getNewCreditDetailViewList()){
    		NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(newCreditDetailView.getId());
    		//newCreditDetail.setSetupCompleted(newCreditDetailView.getIsSetupCompleted());
    		newCreditDetailDAO.save(newCreditDetail);
    	}
    }
 
}
