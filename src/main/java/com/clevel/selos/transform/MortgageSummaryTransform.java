package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.AgreementInfo;
import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.view.MortgageSummaryView;

public class MortgageSummaryTransform extends Transform {
    private static final long serialVersionUID = 1462353182914169301L;
    
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
}
