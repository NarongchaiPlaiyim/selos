package com.clevel.selos.dao.working;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.MortgageInfoCollSub;

public class MortgageInfoCollSubDAO extends GenericDAO<MortgageInfoCollSub, Long>{

	private static final long serialVersionUID = 7464400609523727441L;

	@SuppressWarnings("unchecked")
	public List<MortgageInfoCollSub> findAllByMortgageInfoId(long mortgageInfoId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("mortgageInfo.id", mortgageInfoId));
        return criteria.list();
	}
}
