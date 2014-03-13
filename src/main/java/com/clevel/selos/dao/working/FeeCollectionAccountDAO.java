package com.clevel.selos.dao.working;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.FeeCollectionAccount;

public class FeeCollectionAccountDAO  extends GenericDAO<FeeCollectionAccount,Long>{
	private static final long serialVersionUID = -4532574081276590679L;
	@Inject @SELOS
    private Logger log;
	
	@SuppressWarnings("unchecked")
	public List<FeeCollectionAccount> findAllByWorkCaseId(long workCaseId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
	}
}
