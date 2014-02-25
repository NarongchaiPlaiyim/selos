package com.clevel.selos.transform;

import javax.inject.Inject;

import com.clevel.selos.model.db.working.Address;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerAttorneyView;
import com.clevel.selos.model.view.MortgageInfoAttorneySelectView;

public class MortgageInfoAttorneySelectTransform extends Transform {
	private static final long serialVersionUID = 3863380190001069428L;
	@Inject private CustomerAttorneyTransform customerAttorneyTransform;
	
	public MortgageInfoAttorneySelectView transformToView(Customer model,Address address) {
		MortgageInfoAttorneySelectView view = new MortgageInfoAttorneySelectView();
		view.setCustomerId(model.getId());
		view.setCustomerName(model.getDisplayName());
		
		if (model.getIndividual() != null)
			view.setCitizenId(model.getIndividual().getCitizenId());
		else if (model.getJuristic() != null)
			view.setCitizenId(model.getJuristic().getRegistrationId());
		else
			view.setCitizenId("-");
		view.setTmbCustomerId(model.getTmbCustomerId());
		if (model.getRelation() != null)
			view.setRelation(model.getRelation().getDescription());
		if (model.getJuristic() != null)
			view.setJuristic(true);
		else
			view.setJuristic(false);
		CustomerAttorneyView attorneyView = customerAttorneyTransform.transformToView(model, address);
		view.setAttorneyDetail(attorneyView);
		return view;
	}
}
