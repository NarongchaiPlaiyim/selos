package com.clevel.selos.dao.working;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.PerfectReviewType;
import com.clevel.selos.model.db.working.PerfectionReview;

public class PerfectionReviewDAO extends GenericDAO<PerfectionReview, Long>{
	
	private static final long serialVersionUID = -2232620230102772972L;

	@SuppressWarnings("unchecked")
	public List<PerfectionReview> findAllByWorkCaseId(long workCaseId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("type"));
        return criteria.list();
	}
	@SuppressWarnings("unchecked")
	public PerfectionReview getPerfectionReviewByType(long workCaseId,PerfectReviewType type) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("type",type));
        List<PerfectionReview> result = criteria.list();
        if (result == null || result.isEmpty())
        	return null;
        else
        	return (PerfectionReview)result.get(0);
	}

}
