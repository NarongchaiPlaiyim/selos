package com.clevel.selos.transform;

import java.math.BigDecimal;

import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.working.MortgageInfo;
import com.clevel.selos.model.view.MortgageInfoView;

public class MortgageInfoTransform extends Transform {
	private static final long serialVersionUID = 4808917448140179209L;

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
		}
		return view;
	}

}
