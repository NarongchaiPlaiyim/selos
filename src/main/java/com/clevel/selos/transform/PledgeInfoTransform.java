package com.clevel.selos.transform;

import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CreditDetailSimpleView;
import com.clevel.selos.model.view.CustomerInfoSimpleView;
import com.clevel.selos.model.view.PledgeInfoFullView;
import com.clevel.selos.model.view.PledgeInfoView;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PledgeInfoTransform extends Transform {
	private static final long serialVersionUID = 1909028796575736180L;

	@Inject private CustomerTransform customerTransform;
	@Inject private CreditDetailSimpleTransform creditDetailSimpleTransform;

	public PledgeInfoView transformToView(PledgeInfo model,long workCaseId) {
		PledgeInfoView view = new PledgeInfoView();
		if (model == null)
			return view;
		_processSimpleView(model, view,workCaseId);
		return view;
	}
	
	public PledgeInfoFullView transformToFullView(PledgeInfo model,long workCaseId) {
		PledgeInfoFullView view = new PledgeInfoFullView();
		if (model == null)
			return view;
		_processSimpleView(model, view,workCaseId);
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
	
	private void _processSimpleView(PledgeInfo model,PledgeInfoView view,long workCaseId) {
		view.setId(model.getId());
		view.setWorkCaseId(workCaseId);
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
		if (!RadioValue.YES.equals(model.getConfirmed()))
			view.setConfirmed(RadioValue.NO);
		else
			view.setConfirmed(model.getConfirmed());
		
	}
	
	public void updateModel(PledgeInfo model,PledgeInfoView view,BigDecimal totalHoldAmount,User user) {
		model.setModifyBy(user);
		model.setModifyDate(new Date());
		if (view == null) {
			model.setConfirmed(RadioValue.NA);
			return;
		}
		model.setPledgeSigningDate(view.getSigningDate());
		model.setTotalHoldAmount(totalHoldAmount);
		model.setConfirmed(view.getConfirmed());
	}
	
	public void updateModelConfirmed(PledgeInfo model,PledgeInfoView view,User user) {
		model.setConfirmed(view.getConfirmed());
		model.setModifyBy(user);
		model.setModifyDate(new Date());
	}
}
