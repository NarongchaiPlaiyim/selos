package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.PerfectionReviewDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.PerfectReviewStatus;
import com.clevel.selos.model.PerfectReviewType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.PerfectionReview;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.PerfectionReviewView;
import com.clevel.selos.transform.PerfectionReviewTransform;

@Stateless
public class PerfectionReviewControl extends BusinessControl{
	private static final long serialVersionUID = -6836003263670432758L;
	@Inject
    @SELOS
    private Logger log;
	@Inject private WorkCaseDAO workCaseDAO;
	@Inject private PerfectionReviewDAO perfectionReviewDAO;
	@Inject private PerfectionReviewTransform perfectionReviewTransform;
	
	public List<PerfectionReviewView> getPerfectionReviews(long workCaseId) {
		if (workCaseId <= 0)
			return Collections.emptyList();
		List<PerfectionReviewView> rtnDatas = new ArrayList<PerfectionReviewView>();
		List<PerfectionReview> list = perfectionReviewDAO.findAllByWorkCaseId(workCaseId);
		HashMap<PerfectReviewType, PerfectionReview> map = new HashMap<PerfectReviewType, PerfectionReview>();
		for (PerfectionReview data : list) {
			map.put(data.getType(), data);
		}
		
		for (PerfectReviewType type : PerfectReviewType.displayList()) {
			PerfectionReview model = map.get(type);
			if (model == null) {
				PerfectionReviewView view = new PerfectionReviewView();
				view.setType(type);
				view.setStatus(PerfectReviewStatus.NA);
				rtnDatas.add(view);
			} else {
				rtnDatas.add(perfectionReviewTransform.transformToView(model));
			}
		}
		return rtnDatas;
	}
	public PerfectionReviewView getPerfectionReview(long workCaseId,PerfectReviewType type) {
		PerfectionReview model = perfectionReviewDAO.getPerfectionReviewByType(workCaseId, type);
		PerfectionReviewView view = null;
		if (model == null) {
			view = new PerfectionReviewView();
			view.setType(type);
		} else {
			view = perfectionReviewTransform.transformToView(model);
		}
		return view;
	}
	public void updatePerfectionReview(PerfectionReviewView view,long workCaseId) {
		User user = getCurrentUser();
		
		if (view.getId() <= 0) {
			WorkCase workCase = workCaseDAO.findRefById(workCaseId);
			PerfectionReview model = perfectionReviewTransform.createNewModel(view, user, workCase);
			perfectionReviewDAO.save(model);
		} else {
			PerfectionReview model = perfectionReviewDAO.findById(view.getId());
			perfectionReviewTransform.updateModel(model, view, user);
			perfectionReviewDAO.persist(model);
		}
	}
}
