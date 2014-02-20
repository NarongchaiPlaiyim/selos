package com.clevel.selos.transform;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.clevel.selos.dao.master.MortgageLandOfficeDAO;
import com.clevel.selos.dao.master.MortgageOSCompanyDAO;
import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.MortgageInfo;
import com.clevel.selos.model.db.working.MortgageInfoMortgage;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.MortgageInfoView;

public class MortgageInfoTransform extends Transform {
	private static final long serialVersionUID = 4808917448140179209L;
	
	@Inject private MortgageOSCompanyDAO mortgageOSCompanyDAO;
	@Inject private MortgageLandOfficeDAO mortgageLandOfficeDAO;
	
	public MortgageInfoView transformToView(MortgageInfo model) {
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
			if (model.getMortgageLandOffice() != null) {
				view.setLandOfficeId(model.getMortgageLandOffice().getId());
				view.setLandOfficeStr(model.getMortgageLandOffice().getName());
			}
			if (model.getMortgageTypeList() != null) {
				List<MortgageInfoMortgage> mortgageTypes = model.getMortgageTypeList();
				StringBuilder builder = new StringBuilder();
				for (MortgageInfoMortgage mortgageType : mortgageTypes) {
					builder.append(mortgageType.getMortgageType().getMortgage());
					builder.append(", ");
				}
				if (builder.length() > 0)
					builder.setLength(builder.length() -2);
				view.setMortgageType(builder.toString());
			}
			view.setMortgageOrder(model.getMortgageOrder());
			view.setMortgageAmount(model.getMortgageAmount());
			view.setPoa(model.getAttorneyRequired());
			view.setAttorneyRelation(model.getAttorneyRelation());
			view.setModifyBy(model.getModifyBy());
			view.setModifyDate(model.getModifyDate());
			
			if (model.getCustomerAttorney() != null)
				view.setCustomerAttorneyId(model.getCustomerAttorney().getId());
		}
		return view;
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
