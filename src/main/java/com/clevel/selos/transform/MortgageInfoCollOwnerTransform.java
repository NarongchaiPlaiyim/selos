package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.MortgageInfoCollOwner;
import com.clevel.selos.model.view.MortgageInfoCollOwnerView;

public class MortgageInfoCollOwnerTransform extends Transform {
	private static final long serialVersionUID = 2983544417144628154L;

	public MortgageInfoCollOwnerView transformToView(MortgageInfoCollOwner model) {
		MortgageInfoCollOwnerView view = new MortgageInfoCollOwnerView();
		view.setId(model.getId());
		Customer customer = model.getCustomer();
		if (customer != null) {
			view.setCustomerId(customer.getId());
			view.setCustomerName(customer.getDisplayName());
			
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
			if (customer.getJuristic() != null)
				view.setJuristic(true);
			else
				view.setJuristic(false);
		}
		return view;
	}

}
