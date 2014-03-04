package com.clevel.selos.transform;

import java.util.Date;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AccountInfoSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AccountInfoSummaryView;

public class AccountInfoSummaryTransform extends Transform {
	private static final long serialVersionUID = 1362699054298979282L;
	public AccountInfoSummaryTransform() {
	}
	
	public AccountInfoSummaryView transformToView(AccountInfoSummary model) {
		AccountInfoSummaryView view = new AccountInfoSummaryView();
		if (model == null)
			return view;
		view.setId(model.getId());
		view.setModifyBy(model.getModifyBy());
		view.setModifyDate(model.getModifyDate());
		return view;
	}
	
	public AccountInfoSummary transformToNewModel(AccountInfoSummaryView view,User user, WorkCase workCase) {
		AccountInfoSummary model = new AccountInfoSummary();
		model.setCreateBy(user);
		model.setCreateDate(new Date());
		model.setWorkCase(workCase);
		
		updateModelFromView(model, view, user);
		return model;
	}
	public void updateModelFromView(AccountInfoSummary model,AccountInfoSummaryView view,User user) {
		model.setModifyBy(user);
		model.setModifyDate(new Date());
	}
}
