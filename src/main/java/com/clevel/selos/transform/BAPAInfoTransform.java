package com.clevel.selos.transform;

import com.clevel.selos.dao.master.InsuranceCompanyDAO;
import com.clevel.selos.model.BAPaymentMethodValue;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BAPAInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BAPAInfoView;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;

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
		if (model.getApplyBA() == null)
			view.setApplyBA(RadioValue.NA);
		else
			view.setApplyBA(model.getApplyBA());
		if (model.getPayToInsuranceCompany() == null)
			view.setPayToInsuranceCompany(RadioValue.NA);
		else
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
		if (view.getUpdInsuranceCompany() <= 0)
			model.setInsuranceCompany(null);
		else
			model.setInsuranceCompany(insuranceCompanyDAO.findById(view.getUpdInsuranceCompany()));
		model.setTotalExpense(view.getTotalExpense());
		model.setTotalLimit(view.getTotalLimit());
		model.setTotalPremium(view.getTotalPremium());
		
		model.setModifyBy(user);
		model.setModifyDate(new Date());
	}
}
