package com.clevel.selos.transform;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import com.clevel.selos.dao.master.InsuranceCompanyDAO;
import com.clevel.selos.model.BAPaymentMethodValue;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BAPAInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BAPAInfoView;

public class BAPAInfoTransform extends Transform{

	private static final long serialVersionUID = -3013769533711553990L;
	
	@Inject private InsuranceCompanyDAO insuranceCompanyDAO;
	public BAPAInfoTransform() {
	}
	
	public BAPAInfoView transformToView(BAPAInfo model) {
		BAPAInfoView view = new BAPAInfoView();
		if (model == null) {
			view.setApplyBA(RadioValue.NA);
			view.setPayToInsuranceCompany(RadioValue.NA);
			view.setTotalExpense(new BigDecimal(0));
			view.setTotalLimit(new BigDecimal(0));
			view.setTotalPremium(new BigDecimal(0));
			return view;
		}
		
		view.setId(model.getId());
		view.setApplyBA(model.getApplyBA());
		view.setPayToInsuranceCompany(model.getPayToInsuranceCompany());
		view.setInsuranceCompany(model.getInsuranceCompany());
		if (model.getInsuranceCompany() != null)
			view.setUpdInsuranceCompany(model.getInsuranceCompany().getId());
		view.setTotalExpense(model.getTotalExpense());
		view.setTotalLimit(model.getTotalLimit());
		view.setTotalPremium(model.getTotalPremium());
		view.setModifyBy(model.getModifyBy());
		view.setModifyDate(model.getModifyDate());
		return view;
	}
	
	public BAPAInfo transformToNewModel(BAPAInfoView view,User user,WorkCase workCase) {
		Date now = new Date();
		
		BAPAInfo model = new BAPAInfo();
		model.setCreateBy(user);
		model.setCreateDate(now);
		model.setBaPaymentMethod(BAPaymentMethodValue.NA);
		model.setWorkCase(workCase);
		updateModelFromView(model, view, user);
		return model;
	}
	
	public void updateModelFromView(BAPAInfo model,BAPAInfoView view,User user) {
		model.setApplyBA(view.getApplyBA());
		model.setPayToInsuranceCompany(view.getPayToInsuranceCompany());
		model.setInsuranceCompany(insuranceCompanyDAO.findById(view.getUpdInsuranceCompany()));
		model.setTotalExpense(view.getTotalExpense());
		model.setTotalLimit(view.getTotalLimit());
		model.setTotalPremium(view.getTotalPremium());
		
		model.setModifyBy(user);
		model.setModifyDate(new Date());
	}
}
