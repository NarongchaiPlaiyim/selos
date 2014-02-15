package com.clevel.selos.dao.working;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.AgreementInfo;

public class AgreementInfoDAO extends GenericDAO<AgreementInfo, Long>{
	private static final long serialVersionUID = -2695969917345458442L;

	public AgreementInfoDAO() {
		
	}
	
	public AgreementInfo findByWorkCaseId(long workCaseId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        AgreementInfo agreement = (AgreementInfo) criteria.uniqueResult();
        return agreement;
    }
}
