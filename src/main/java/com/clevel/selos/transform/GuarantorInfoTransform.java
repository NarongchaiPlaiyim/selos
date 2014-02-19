package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.GuarantorInfo;
import com.clevel.selos.model.view.GuarantorInfoView;

public class GuarantorInfoTransform extends Transform {
	private static final long serialVersionUID = 3517311808190408041L;

	public GuarantorInfoView transformToView(GuarantorInfo model) {
		GuarantorInfoView view = new GuarantorInfoView();
		if (model == null)
			return view;
		view.setId(model.getId());
		view.setSigningDate(model.getGuarantorSigningDate());
		view.setGuarantorAmount(model.getGuarantorAmount());
		view.setModifyBy(model.getModifyBy());
		view.setModifyDate(model.getModifyDate());
		//TODO About guarantor type
		if (model.getGuarantorType() != null)
			view.setGuarantorType(model.getGuarantorType().getMortgage());
		//TODO Guarantor Name
		view.setGuarantorName(null);
		return view;
	}
}
