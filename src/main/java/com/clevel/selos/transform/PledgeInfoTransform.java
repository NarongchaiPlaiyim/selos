package com.clevel.selos.transform;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCollateralCredit;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.db.working.NewCollateralSubOwner;
import com.clevel.selos.model.db.working.OpenAccountName;
import com.clevel.selos.model.db.working.PledgeInfo;
import com.clevel.selos.model.view.CreditDetailSimpleView;
import com.clevel.selos.model.view.CustomerInfoSimpleView;
import com.clevel.selos.model.view.OpenAccountView;
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
		List<CustomerInfoSimpleView> customerViews = new ArrayList<CustomerInfoSimpleView>();
		List<CreditDetailSimpleView> creditViews = new ArrayList<CreditDetailSimpleView>();
		
		NewCollateralSub collateral = model.getNewCollateralSub();
		List<NewCollateralSubOwner> owners = collateral.getNewCollateralSubOwnerList();
		if (owners != null && !owners.isEmpty()) {
			for (NewCollateralSubOwner owner : owners) {
				customerViews.add(customerTransform.transformToSimpleView(owner.getCustomer()));
			}
		}
		view.setCustomers(customerViews);
		
		List<NewCollateralCredit> credits = collateral.getNewCollateralHead().getNewCollateral().getNewCollateralCreditList();
		if (credits != null && !credits.isEmpty()) {
			for (NewCollateralCredit credit : credits) {
				if (credit.getExistingCreditDetail() != null) {
					creditViews.add(creditDetailSimpleTransform.transformToSimpleView(credit.getExistingCreditDetail()));
				} else if (credit.getNewCreditDetail() != null) {
					creditViews.add(creditDetailSimpleTransform.transformToSimpleView(credit.getNewCreditDetail()));
				}
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
		if (model.getOpenAccount() != null) {
			view.setAccountNo(model.getOpenAccount().getAccountNumber());
			view.setNumberOfDep(model.getOpenAccount().getNumberOfDep());
			List<OpenAccountName> names = model.getOpenAccount().getOpenAccountNameList();
			StringBuilder builder = new StringBuilder();
			for (OpenAccountName name : names) {
				builder.append(name.getCustomer().getDisplayName());
				builder.append("<br/>");
			}
			if (builder.length() > 0)
				builder.setLength(builder.length()-5);
			view.setAccountName(builder.toString());
		}
		
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
