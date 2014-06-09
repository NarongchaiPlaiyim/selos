package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.AccountInfoSummary;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class AccountInfoSummaryDAO extends GenericDAO<AccountInfoSummary, Long>{
	private static final long serialVersionUID = 2658245274551346468L;

	public AccountInfoSummary findByWorkCase(long workCaseId) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("workCase.id", workCaseId));
		AccountInfoSummary bapaInfo = (AccountInfoSummary) criteria.uniqueResult();
		return bapaInfo;
	}
}
