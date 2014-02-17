package com.clevel.selos.dao.working;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.AttorneyType;
import com.clevel.selos.model.db.working.CustomerAttorney;

public class CustomerAttorneyDAO extends GenericDAO<CustomerAttorney, Long>{
	
	private static final long serialVersionUID = 6743036527309728033L;

	public CustomerAttorney findPOAByWorkCaseId(long workCaseId) {
	    Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("attorneyType",AttorneyType.POWER_OF_ATTORNEY));
        List<?> list = criteria.list();
        if (list != null && !list.isEmpty())
        	return (CustomerAttorney) list.get(0);
        else
        	return null;
	}
}
