package com.clevel.selos.dao.working;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.GuarantorInfo;

public class GuarantorInfoDAO extends GenericDAO<GuarantorInfo, Long> {

	private static final long serialVersionUID = 7232186194076495887L;
	@SuppressWarnings("unchecked")
	public List<GuarantorInfo> findAllByWorkCaseId(long workCaseId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        return criteria.list();
	}
}
