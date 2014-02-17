package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.MortgageInfoCollOwnerView;
import com.clevel.selos.util.Util;

public class MortgageInfoCollOwnerTransform extends Transform {
	private static final long serialVersionUID = 2983544417144628154L;

	public MortgageInfoCollOwnerView transformToView(Customer model,long ownerId) {
		MortgageInfoCollOwnerView view = new MortgageInfoCollOwnerView();
		view.setId(ownerId);
		view.setCustomerId(model.getId());
		StringBuilder builder = new StringBuilder();
		if (model.getTitle() != null)
			builder.append(model.getTitle().getTitleTh()).append(' ');
		builder.append(model.getNameTh());
		if (!Util.isEmpty(model.getLastNameTh()))
			builder.append(" ").append(model.getLastNameTh());
		view.setCustomerName(builder.toString());
		
		if (model.getIndividual() != null)
			view.setCitizenId(model.getIndividual().getCitizenId());
		else if (model.getJuristic() != null)
			view.setCitizenId(model.getJuristic().getRegistrationId());
		else
			view.setCitizenId("-");
		view.setTmbCustomerId(model.getTmbCustomerId());
		if (model.getRelation() != null)
			view.setRelation(model.getRelation().getDescription());
		view.setPOA(ownerId > 0);
		view.setCanSelectPOA(true);
		return view;
	}

}
