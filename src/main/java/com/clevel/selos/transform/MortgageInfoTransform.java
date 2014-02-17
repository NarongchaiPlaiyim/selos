package com.clevel.selos.transform;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import com.clevel.selos.dao.master.MortgageLandOfficeDAO;
import com.clevel.selos.dao.master.MortgageOSCompanyDAO;
import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.MortgageInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.MortgageInfoView;

public class MortgageInfoTransform extends Transform {
	private static final long serialVersionUID = 4808917448140179209L;
	
	@Inject private MortgageOSCompanyDAO mortgageOSCompanyDAO;
	@Inject private MortgageLandOfficeDAO mortgageLandOfficeDAO;
	
	public MortgageInfoView transfromToView(MortgageInfo model) {
		MortgageInfoView view = new MortgageInfoView();
		if (model == null) {
			view.setMortgageAmount(new BigDecimal(0));
			view.setPoa(RadioValue.NA);
			view.setAttorneyRelation(AttorneyRelationType.NA);
		} else {
			view.setId(model.getId());
			view.setSigningDate(model.getMortgageSigningDate());
			if (model.getMortgageOSCompany() != null)
				view.setOsCompanyId(model.getMortgageOSCompany().getId());
			if (model.getMortgageLandOffice() != null)
				view.setLandOfficeId(model.getMortgageLandOffice().getId());
			if (model.getMortgageType() != null)
				view.setMortgageType(model.getMortgageType().getMortgage());
			view.setMortgageOrder(model.getMortgageOrder());
			//TODO Check about mortgage amount
			view.setMortgageAmount(new BigDecimal(0));
			view.setPoa(model.getAttorneyRequired());
			view.setAttorneyRelation(model.getAttorneyRelation());
			view.setModifyBy(model.getModifyBy());
			view.setModifyDate(model.getModifyDate());
			
			if (model.getCustomerAttorney() != null)
				view.setCustomerAttorneyId(model.getCustomerAttorney().getId());
		}
		return view;
	}
	
	public MortgageInfo createNewModel(MortgageInfoView view,User user,WorkCase workCase) {
		MortgageInfo model = new MortgageInfo();
		model.setMortgageType(null);
		model.setWorkCase(workCase);
		model.setCreateBy(user);
		model.setCreateDate(new Date());
		
		updateModelFromView(model, view, user);
		return model;
	}
	
	public void updateModelFromView(MortgageInfo model,MortgageInfoView view,User user) {
		model.setMortgageSigningDate(view.getSigningDate());
		if (view.getOsCompanyId() > 0)
			model.setMortgageOSCompany(mortgageOSCompanyDAO.findById(view.getOsCompanyId()));
		if (view.getLandOfficeId() > 0)
			model.setMortgageLandOffice(mortgageLandOfficeDAO.findById(view.getLandOfficeId()));
		model.setMortgageOrder(view.getMortgageOrder());
		model.setAttorneyRequired(view.getPoa());
		model.setAttorneyRelation(view.getAttorneyRelation());
		model.setModifyBy(user);
		model.setModifyDate(new Date());
	}

}
