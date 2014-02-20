package com.clevel.selos.dao.working;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.MortgageInfoCredit;

public class MortgageInfoCreditDAO extends GenericDAO<MortgageInfoCredit, Long>{

	private static final long serialVersionUID = -2950703549136416606L;
	@SuppressWarnings("unchecked")
	public List<MortgageInfoCredit> findAllByMortgageInfoId(long mortgageInfoId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("mortgageInfo.id", mortgageInfoId));
        return criteria.list();
	}
	
	public void deleteByMortgageInfoId(long mortgageInfoId) {
		String hql = "delete from MortgageInfoCredit where mortgageInfo.id=:id";
		getSession().createQuery(hql).setLong("id", mortgageInfoId).executeUpdate();
	}
}
