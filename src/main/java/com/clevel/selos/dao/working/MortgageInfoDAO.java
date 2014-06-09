package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.MortgageInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class MortgageInfoDAO extends GenericDAO<MortgageInfo, Long>{
	
	private static final long serialVersionUID = 6431663676888293805L;

	@SuppressWarnings("unchecked")
	public List<MortgageInfo> findAllByWorkCaseId(long workCaseId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("mortgageOrder"));
        return criteria.list();
	}
}
