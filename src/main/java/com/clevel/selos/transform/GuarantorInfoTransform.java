package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CreditDetailSimpleView;
import com.clevel.selos.model.view.CustomerInfoSimpleView;
import com.clevel.selos.model.view.GuarantorInfoFullView;
import com.clevel.selos.model.view.GuarantorInfoView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuarantorInfoTransform extends Transform {
	private static final long serialVersionUID = 3517311808190408041L;

	@Inject private CustomerTransform customerTransform;
	@Inject private CreditDetailSimpleTransform creditDetailSimpleTransform;
	
	public GuarantorInfoView transformToView(GuarantorInfo model,long workCaseId) {
		GuarantorInfoView view = new GuarantorInfoView();
		if (model == null)
			return view;
		_processSimpleView(model, view,workCaseId);
		return view;
	}
	
	public GuarantorInfo createGuaratorInfo(User user,WorkCase workCase) {
		GuarantorInfo model = new GuarantorInfo();
		model.setCreateBy(user);
		model.setCreateDate(new Date());
		model.setWorkCase(workCase);
		updateModel(model, null, user);
		return model;
	}
	
	public void updateModel(GuarantorInfo model,GuarantorInfoView view,User user) {
		model.setModifyBy(user);
		model.setModifyDate(new Date());
		if (view == null)
			return;
		model.setGuarantorSigningDate(view.getSigningDate());
	}
	
	public GuarantorInfoFullView transformToFullView(GuarantorInfo model,long workCaseId) {
		GuarantorInfoFullView view = new GuarantorInfoFullView();
		if (model == null)
			return view;
		_processSimpleView(model, view,workCaseId);
		if (model.getNewGuarantorDetail() == null)
			return view;
		List<CustomerInfoSimpleView> customerViews = new ArrayList<CustomerInfoSimpleView>();
		List<CreditDetailSimpleView> creditViews = new ArrayList<CreditDetailSimpleView>();
		
		Customer customer = model.getNewGuarantorDetail().getGuarantorName();
		if (customer != null) {
			customerViews.add(customerTransform.transformToSimpleView(customer));
		}
		view.setCustomers(customerViews);
		
		List<NewGuarantorCredit> credits = model.getNewGuarantorDetail().getNewGuarantorCreditList();
		for (NewGuarantorCredit credit : credits) {
			if (credit.getExistingCreditDetail() != null) {
				creditViews.add(creditDetailSimpleTransform.transformToSimpleView(credit.getExistingCreditDetail()));
			} else if (credit.getNewCreditDetail() != null) {
				creditViews.add(creditDetailSimpleTransform.transformToSimpleView(credit.getNewCreditDetail()));
			}
		}
		view.setCredits(creditViews);
		return view;
	}
	
	private void _processSimpleView(GuarantorInfo model,GuarantorInfoView view,long workCaseId) {
		view.setId(model.getId());
		view.setWorkCaseId(workCaseId);
		view.setSigningDate(model.getGuarantorSigningDate());
		view.setModifyBy(model.getModifyBy());
		view.setModifyDate(model.getModifyDate());
		if (model.getGuarantorType() != null)
			view.setGuarantorType(model.getGuarantorType().getMortgage());
		if (model.getNewGuarantorDetail() != null) {
			NewGuarantorDetail detail = model.getNewGuarantorDetail();
			if (detail.getTotalLimitGuaranteeAmount() != null)
				view.setGuarantorAmount(detail.getTotalLimitGuaranteeAmount());
			if (detail.getGuarantorName() != null) {
				Customer customer = detail.getGuarantorName();
				StringBuilder builder = new StringBuilder();
				if (customer.getTitle() != null)
					builder.append(customer.getTitle().getTitleTh()).append(' ');
				builder.append(customer.getNameTh());
				if (!Util.isEmpty(customer.getLastNameTh()))
					builder.append(" ").append(customer.getLastNameTh());
				view.setGuarantorName(builder.toString());	
			}
		}
	}
}
