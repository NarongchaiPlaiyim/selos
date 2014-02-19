package com.clevel.selos.dao.working;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.PledgeInfo;

public class PledgeInfoDAO extends GenericDAO<PledgeInfo, Long> {
	private static final long serialVersionUID = 6172025605180104378L;

	@SuppressWarnings("unchecked")
	public List<PledgeInfo> findAllByWorkCaseId(long workCaseId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        return criteria.list();
	}
}
