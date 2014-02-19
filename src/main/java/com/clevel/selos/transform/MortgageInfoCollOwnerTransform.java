package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.MortgageInfoCollOwner;
import com.clevel.selos.model.view.MortgageInfoCollOwnerView;
import com.clevel.selos.util.Util;

public class MortgageInfoCollOwnerTransform extends Transform {
	private static final long serialVersionUID = 2983544417144628154L;

	public MortgageInfoCollOwnerView transformToView(MortgageInfoCollOwner model) {
		MortgageInfoCollOwnerView view = new MortgageInfoCollOwnerView();
		view.setId(model.getId());
		view.setCustomerId(model.getId());
		Customer customer = model.getCustomer();
		StringBuilder builder = new StringBuilder();
		if (customer.getTitle() != null)
			builder.append(customer.getTitle().getTitleTh()).append(' ');
		builder.append(customer.getNameTh());
		if (!Util.isEmpty(customer.getLastNameTh()))
			builder.append(" ").append(customer.getLastNameTh());
		view.setCustomerName(builder.toString());
		
		if (customer.getIndividual() != null)
			view.setCitizenId(customer.getIndividual().getCitizenId());
		else if (customer.getJuristic() != null)
			view.setCitizenId(customer.getJuristic().getRegistrationId());
		else
			view.setCitizenId("-");
		view.setTmbCustomerId(customer.getTmbCustomerId());
		if (customer.getRelation() != null)
			view.setRelation(customer.getRelation().getDescription());
		view.setPOA(model.isPoa());
		view.setCanSelectPOA(true);
		return view;
	}

}
