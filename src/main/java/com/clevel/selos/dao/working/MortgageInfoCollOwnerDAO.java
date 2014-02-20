package com.clevel.selos.dao.working;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.MortgageInfoCollOwner;

public class MortgageInfoCollOwnerDAO extends GenericDAO<MortgageInfoCollOwner,Long>{
	
	private static final long serialVersionUID = -5357031545110513028L;

	@SuppressWarnings("unchecked")
	public List<MortgageInfoCollOwner> findAllByMortgageInfoId(long mortgageInfoId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("mortgageInfo.id", mortgageInfoId));
        return criteria.list();
	}

	public void deleteByMortgageInfoId(long mortgageInfoId) {
		String hql = "delete from MortgageInfoCollOwner where mortgageInfo.id=:id";
		getSession().createQuery(hql).setLong("id", mortgageInfoId).executeUpdate();
	}
}
