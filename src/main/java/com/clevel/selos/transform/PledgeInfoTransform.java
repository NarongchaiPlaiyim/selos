package com.clevel.selos.transform;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.OpenAccountCredit;
import com.clevel.selos.model.db.working.OpenAccountName;
import com.clevel.selos.model.db.working.PledgeInfo;
import com.clevel.selos.model.view.CreditDetailSimpleView;
import com.clevel.selos.model.view.CustomerInfoSimpleView;
import com.clevel.selos.model.view.PledgeInfoFullView;
import com.clevel.selos.model.view.PledgeInfoView;

public class PledgeInfoTransform extends Transform {
	private static final long serialVersionUID = 1909028796575736180L;
	@Inject private CustomerTransform customerTransform;
	@Inject private CreditDetailSimpleTransform creditDetailSimpleTransform;
	public PledgeInfoView transformToView(PledgeInfo model) {
		PledgeInfoView view = new PledgeInfoView();
		if (model == null)
			return view;
		_processSimpleView(model, view);
		return view;
	}
	
	public PledgeInfoFullView transformToFullView(PledgeInfo model) {
		PledgeInfoFullView view = new PledgeInfoFullView();
		if (model == null)
			return view;
		_processSimpleView(model, view);
		//TODO About credit and customer
		List<CustomerInfoSimpleView> customerViews = new ArrayList<CustomerInfoSimpleView>();
		List<CreditDetailSimpleView> creditViews = new ArrayList<CreditDetailSimpleView>();
		
		List<OpenAccountName> accountNames = model.getOpenAccount().getOpenAccountNameList();
		for (OpenAccountName accountName : accountNames) {
			if (accountName.isFromPledge())
				customerViews.add(customerTransform.transformToSimpleView(accountName.getCustomer()));
		}
		view.setCustomers(customerViews);
		
		List<OpenAccountCredit> accountCredits = model.getOpenAccount().getOpenAccountCreditList();
		for (OpenAccountCredit accountCredit : accountCredits) {
			if (!accountCredit.isFromPledge())
				continue;
			if (accountCredit.getExistingCreditDetail() != null) {
				creditViews.add(creditDetailSimpleTransform.transformToSimpleView(accountCredit.getExistingCreditDetail()));
			} else if (accountCredit.getNewCreditDetail() != null) {
				creditViews.add(creditDetailSimpleTransform.transformToSimpleView(accountCredit.getNewCreditDetail()));
			}
		}
		view.setCredits(creditViews);
		return view;
	}
	
	private void _processSimpleView(PledgeInfo model,PledgeInfoView view) {
		view.setId(model.getId());
		if (model.getPledgeType() != null)
			view.setPledgeType(model.getPledgeType().getMortgage());
		view.setSigningDate(model.getPledgeSigningDate());
		view.setPledgeAmount(model.getPledgeAmount());
		view.setModifyBy(model.getModifyBy());
		view.setModifyDate(model.getModifyDate());
		if (model.getOpenAccount() != null)
			view.setAccountNo(model.getOpenAccount().getAccountNumber());
	}
	
	public void updateModel(PledgeInfo model,PledgeInfoView view,BigDecimal totalHoldAmount,User user) {
		model.setModifyBy(user);
		model.setModifyDate(new Date());
		if (view == null)
			return;
		model.setPledgeSigningDate(view.getSigningDate());
		model.setTotalHoldAmount(totalHoldAmount);
	}
}
