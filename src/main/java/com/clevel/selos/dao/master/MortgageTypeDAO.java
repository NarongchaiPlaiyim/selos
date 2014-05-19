package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MortgageType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MortgageTypeDAO extends GenericDAO<MortgageType, Integer> {
	@Inject
    @SELOS
    Logger log;

    @Inject
    public MortgageTypeDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<MortgageType> findMortgageTypeForGuarantor() {
		Criteria criteria = createCriteria()
				.add(Restrictions.eq("active", 1))
				.add(Restrictions.eq("guarantorFlag", true))
				.addOrder(Order.asc("id"));
		return criteria.list();
    }
}
