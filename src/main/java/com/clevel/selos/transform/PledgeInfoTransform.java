package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.PledgeInfo;
import com.clevel.selos.model.view.PledgeInfoView;

public class PledgeInfoTransform extends Transform {
	private static final long serialVersionUID = 1909028796575736180L;

	public PledgeInfoView transformToView(PledgeInfo model) {
		PledgeInfoView view = new PledgeInfoView();
		view.setId(model.getId());
		if (model.getPledgeType() != null)
			view.setPledgeType(model.getPledgeType().getMortgage());
		view.setSigningDate(model.getPledgeSigningDate());
		view.setPledgeAmount(model.getPledgeAmount());
		view.setModifyBy(model.getModifyBy());
		view.setModifyDate(model.getModifyDate());
		if (model.getOpenAccount() != null)
			view.setAccountNo(model.getOpenAccount().getAccountNumber());
		return view;
	}

}
