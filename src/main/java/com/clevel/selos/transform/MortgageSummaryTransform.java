package com.clevel.selos.transform;

import java.util.Date;

import com.clevel.selos.model.MortgageConfirmedType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.MortgageSummaryView;

public class MortgageSummaryTransform extends Transform {
    private static final long serialVersionUID = 1462353182914169301L;

    public MortgageSummaryView transformToView(MortgageSummary model) {
    	MortgageSummaryView view = new MortgageSummaryView();
    	if (model != null) {
    		view.setId(model.getId());
    		view.setModifyBy(model.getModifyBy());
    		view.setModifyDate(model.getModifyDate());
    		view.setConfirmed(model.getConfirmed());
    	}
    	return view;
    }
    public MortgageSummary createMortgageSummary(MortgageSummaryView view,User user,WorkCase workCase) {
    	MortgageSummary model = new MortgageSummary();
    	model.setCreateBy(user);
    	model.setCreateDate(new Date());
		model.setWorkCase(workCase);
		model.setConfirmed(MortgageConfirmedType.NA);
		updateMortgageSummary(model, view,user);
		return model;
    }
    
    public void updateMortgageSummary(MortgageSummary model,MortgageSummaryView view,User user) {
    	model.setModifyBy(user);
    	model.setModifyDate(new Date());
    	if (view != null)
    		model.setConfirmed(view.getConfirmed());
    }
}
