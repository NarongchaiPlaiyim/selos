package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.PledgeInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class PledgeInfoDAO extends GenericDAO<PledgeInfo, Long> {
	private static final long serialVersionUID = 6172025605180104378L;

	@SuppressWarnings("unchecked")
	public List<PledgeInfo> findAllByWorkCaseId(long workCaseId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        return criteria.list();
	}
	public int countAllByWorkCaseId(long workCaseId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.setProjection(Projections.rowCount());
        Number number = (Number)criteria.uniqueResult();
        if (number == null)
        	return 0;
        else
        	return number.intValue();
	}
}
