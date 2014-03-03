package com.clevel.selos.transform;

import java.util.Date;

import javax.inject.Inject;

import com.clevel.selos.dao.master.BankBranchDAO;
import com.clevel.selos.dao.master.UserZoneDAO;
import com.clevel.selos.model.MortgageConfirmedType;
import com.clevel.selos.model.MortgageSignLocationType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AgreementInfo;
import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.MortgageSummaryView;

public class MortgageSummaryTransform extends Transform {
    private static final long serialVersionUID = 1462353182914169301L;
    
    @Inject private BankBranchDAO bankBranchDAO;
    @Inject private UserZoneDAO userZoneDAO;
    
    public MortgageSummaryView transformToView(MortgageSummary model,AgreementInfo agreementModel) {
    	MortgageSummaryView view = new MortgageSummaryView();
    	if (model != null) {
    		view.setId(model.getId());
    		view.setModifyBy(model.getModifyBy());
    		view.setModifyDate(model.getModifyDate());
    	}
    	
    	if (agreementModel != null) {
    		view.setAgreementId(agreementModel.getId());
    		view.setLoanContractDate(agreementModel.getLoanContractDate());
    		view.setSigningLocation(agreementModel.getSigningLocation());
    		view.setComsNumber(agreementModel.getComsNumber());
    		if (agreementModel.getConfirmed() == null)
				view.setConfirmed(MortgageConfirmedType.NA);
			else
				view.setConfirmed(agreementModel.getConfirmed());
    		switch (view.getSigningLocation()) {
    			case BRANCH :
    				if (agreementModel.getBankBranch() != null)
    					view.setUpdLocation(agreementModel.getBankBranch().getId());
    				break;
    			case ZONE :
    				if (agreementModel.getUserZone() != null)
    					view.setUpdLocation(agreementModel.getUserZone().getId());
    				break;
    			default : //DO NOTHING
    				break;
    		}
    	}
    	return view;
    }
    public MortgageSummary createMortgageSummary(User user,WorkCase workCase) {
    	MortgageSummary model = new MortgageSummary();
    	model.setCreateBy(user);
    	model.setCreateDate(new Date());
		model.setWorkCase(workCase);
		updateMortgageSummary(model, user);
		return model;
    }
    public AgreementInfo creatAgreementInfo(MortgageSummaryView view,WorkCase workCase) {
    	AgreementInfo model = new AgreementInfo();
		model.setWorkCase(workCase);
		updateAgreementInfo(model, view);
		return model;
    }
    
    public void updateMortgageSummary(MortgageSummary model,User user) {
    	model.setModifyBy(user);
    	model.setModifyDate(new Date());
    }
    
    public void updateAgreementInfo(AgreementInfo model,MortgageSummaryView view) {
    	if (view == null) {
    		model.setSigningLocation(MortgageSignLocationType.NA);
    		return;
    	} 
    	model.setLoanContractDate(view.getLoanContractDate());
    	model.setSigningLocation(view.getSigningLocation());
    	model.setComsNumber(view.getComsNumber());
    	
    	if (view.getUpdLocation() <= 0) {
    		model.setUserZone(null);
			model.setBankBranch(null);
    	} else {
	    	switch (view.getSigningLocation()) {
	    		case BRANCH :
	    			model.setBankBranch(bankBranchDAO.findRefById(view.getUpdLocation()));
	    			model.setUserZone(null);
	    			break;
	    		case ZONE :
	    			model.setUserZone(userZoneDAO.findRefById(view.getUpdLocation()));
	    			model.setBankBranch(null);
	       			break;
	    		default :
	    			model.setUserZone(null);
	    			model.setBankBranch(null);
	    			break;
	    	}
    	}
    }
    public void updateConfirmed(MortgageSummary model,AgreementInfo agreementModel,MortgageSummaryView view,User user) {
    	updateMortgageSummary(model, user);
    	agreementModel.setConfirmed(view.getConfirmed());
    }
}
