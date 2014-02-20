package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.view.CreditDetailSimpleView;
import com.clevel.selos.util.Util;

public class CreditDetailSimpleTransform extends Transform {

	private static final long serialVersionUID = -729472906447853270L;

	public CreditDetailSimpleView transformToSimpleView(NewCreditDetail model) {
		CreditDetailSimpleView view = new CreditDetailSimpleView();
		view.setId(model.getId());

		view.setAccountName(model.getAccountName());
		view.setAccountNo(model.getAccountNumber());
		view.setAccountStatus(model.getAccountSuf());
		view.setNewCredit(true);
		view.setProductProgram(model.getProductProgram().getName());
		view.setCreditFacility(model.getCreditType().getName());
		view.setLimit(model.getLimit());
		boolean hasAccountInfo = !(Util.isEmpty(model.getAccountName()) || Util.isEmpty(model.getAccountNumber()));
		view.setHasAccountInfo(hasAccountInfo);
		return view;
	}
	
	public CreditDetailSimpleView transformToSimpleView(ExistingCreditDetail model) {
		CreditDetailSimpleView view = new CreditDetailSimpleView();
		view.setId(model.getId());

		view.setAccountName(model.getAccountName());
		view.setAccountNo(model.getAccountNumber());
		view.setAccountStatus(model.getAccountSuf());
		view.setNewCredit(false);
		view.setProductProgram(model.getProductProgram());
		view.setCreditFacility(model.getCreditType());
		view.setLimit(model.getLimit());
		boolean hasAccountInfo = !(Util.isEmpty(model.getAccountName()) || Util.isEmpty(model.getAccountNumber()));
		view.setHasAccountInfo(hasAccountInfo);
		return view;
	}
}
