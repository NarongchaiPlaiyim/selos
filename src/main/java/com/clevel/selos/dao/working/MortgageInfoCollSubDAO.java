package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.MortgageInfoCollSub;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class MortgageInfoCollSubDAO extends GenericDAO<MortgageInfoCollSub, Long>{

	private static final long serialVersionUID = 7464400609523727441L;

	@SuppressWarnings("unchecked")
	public List<MortgageInfoCollSub> findAllByMortgageInfoId(long mortgageInfoId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("mortgageInfo.id", mortgageInfoId));
        return criteria.list();
	}
	
	public void deleteByMortgageInfoId(long mortgageInfoId) {
		String hql = "delete from MortgageInfoCollSub where mortgageInfo.id=:id";
		getSession().createQuery(hql).setLong("id", mortgageInfoId).executeUpdate();
	}
	
}
