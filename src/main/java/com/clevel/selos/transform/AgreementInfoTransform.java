package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankBranchDAO;
import com.clevel.selos.dao.master.UserZoneDAO;
import com.clevel.selos.model.MortgageSignLocationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AgreementInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AgreementInfoView;

import javax.inject.Inject;
import java.util.Date;

public class AgreementInfoTransform extends Transform {

	private static final long serialVersionUID = 2168558013736588344L;
	@Inject private BankBranchDAO bankBranchDAO;
    @Inject private UserZoneDAO userZoneDAO;
    
    public AgreementInfoView transformToView(AgreementInfo model) {
    	AgreementInfoView view = new AgreementInfoView();
    	if (model == null)
    		return view;
    	
    	view.setId(model.getId());
		view.setLoanContractDate(model.getLoanContractDate());
		view.setSigningLocation(model.getSigningLocation());
		view.setComsNumber(model.getComsNumber());
		if (model.getConfirmed() == null)
			view.setConfirmed(RadioValue.NA);
		else
			view.setConfirmed(model.getConfirmed());
		switch (view.getSigningLocation()) {
			case BRANCH :
				if (model.getBankBranch() != null)
					view.setUpdLocation(model.getBankBranch().getId());
				break;
			case ZONE :
				if (model.getUserZone() != null)
					view.setUpdLocation(model.getUserZone().getId());
				break;
			default : //DO NOTHING
				break;
		}
		view.setFirstPaymentDate(model.getFirstPaymentDate());
		view.setPayDate(model.getPayDate());
		view.setRemark(model.getRemark());
		if (model.getModifyBy() != null)
			view.setModifyUser(model.getModifyBy().getDisplayName());
		else
			view.setModifyUser(null);
		return view;
	}
    public AgreementInfo creatAgreementInfo(AgreementInfoView view,WorkCase workCase,User user) {
    	AgreementInfo model = new AgreementInfo();
		model.setWorkCase(workCase);
		model.setCreateBy(user);
		model.setCreateDate(new Date());
		updateAgreementInfo(model, view,user);
		return model;
    }
    public void updateAgreementInfo(AgreementInfo model,AgreementInfoView view,User user) {
    	if (view == null) {
    		model.setSigningLocation(MortgageSignLocationType.NA);
    		return;
    	} 
    	model.setLoanContractDate(view.getLoanContractDate());
    	model.setSigningLocation(view.getSigningLocation());
    	model.setComsNumber(view.getComsNumber());
    	model.setConfirmed(view.getConfirmed());
    	
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
    	model.setFirstPaymentDate(view.getFirstPaymentDate());
    	model.setPayDate(view.getPayDate());
    	model.setRemark(view.getRemark());
    	
    	model.setModifyBy(user);
    	model.setModifyDate(new Date());
    }
    public void updateConfirmed(AgreementInfo model,AgreementInfoView view,User user) {
    	model.setConfirmed(view.getConfirmed());
    	model.setRemark(view.getRemark());
    	model.setModifyBy(user);
    	model.setModifyDate(new Date());
    }
}
