package com.clevel.selos.transform;

import java.util.Date;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.PerfectionReview;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.PerfectionReviewView;

public class PerfectionReviewTransform extends Transform {
	
	private static final long serialVersionUID = -6279620092182470485L;

	public PerfectionReviewView transformToView(PerfectionReview model) {
		PerfectionReviewView view = new PerfectionReviewView();
		if (model == null)
			return view;
		view.setId(model.getId());
		view.setType(model.getType());
		view.setStatus(model.getStatus());
		view.setDate(model.getDate());
		view.setCompletedDate(model.getCompletedDate());
		view.setRemark(model.getRemark());
		view.setModifyDate(model.getModifyDate());
		if (model.getModifyBy() != null)
			view.setModifyUser(model.getModifyBy().getDisplayName());
		return view;
	}

	public PerfectionReview createNewModel(PerfectionReviewView view,User user,WorkCase workCase) {
		PerfectionReview model = new PerfectionReview();
		model.setType(view.getType());
		model.setWorkCase(workCase);
		model.setCreateDate(new Date());
		model.setCreateBy(user);
		updateModel(model, view, user);
		return model;
	}
	
	public void updateModel(PerfectionReview model,PerfectionReviewView view,User user) {
		model.setStatus(view.getStatus());
		model.setDate(view.getDate());
		model.setCompletedDate(view.getCompletedDate());
		model.setRemark(view.getRemark());
		model.setModifyBy(user);
		model.setModifyDate(new Date());
	}
}
