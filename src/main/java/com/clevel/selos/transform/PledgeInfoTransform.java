package com.clevel.selos.transform;

import java.math.BigDecimal;
import java.util.Date;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.PledgeInfo;
import com.clevel.selos.model.view.PledgeInfoFullView;
import com.clevel.selos.model.view.PledgeInfoView;

public class PledgeInfoTransform extends Transform {
	private static final long serialVersionUID = 1909028796575736180L;

	public PledgeInfoView transformToView(PledgeInfo model) {
		PledgeInfoView view = new PledgeInfoView();
		if (model == null)
			return view;
		_processSimpleView(model, view);
		return view;
	}
	
	public PledgeInfoFullView transformToFullView(PledgeInfo model) {
		PledgeInfoFullView view = new PledgeInfoFullView();
		if (model == null)
			return view;
		_processSimpleView(model, view);
		//TODO About credit and customer
		return view;
	}
	
	private void _processSimpleView(PledgeInfo model,PledgeInfoView view) {
		view.setId(model.getId());
		if (model.getPledgeType() != null)
			view.setPledgeType(model.getPledgeType().getMortgage());
		view.setSigningDate(model.getPledgeSigningDate());
		view.setPledgeAmount(model.getPledgeAmount());
		view.setModifyBy(model.getModifyBy());
		view.setModifyDate(model.getModifyDate());
		if (model.getOpenAccount() != null)
			view.setAccountNo(model.getOpenAccount().getAccountNumber());
	}
	
	public void updateModel(PledgeInfo model,PledgeInfoView view,BigDecimal totalHoldAmount,User user) {
		model.setModifyBy(user);
		model.setModifyDate(new Date());
		if (view == null)
			return;
		model.setPledgeSigningDate(view.getSigningDate());
		model.setTotalHoldAmount(totalHoldAmount);
	}
}
