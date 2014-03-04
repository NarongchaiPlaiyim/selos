package com.clevel.selos.dao.working;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.FeeCollectionDetail;

public class FeeCollectionDetailDAO  extends GenericDAO<FeeCollectionDetail,Long>{
	private static final long serialVersionUID = 4089215828984296155L;
	@Inject @SELOS
    private Logger log;
	
	@SuppressWarnings("unchecked")
	public List<FeeCollectionDetail> findAllByWorkCaseId(long workCaseId) {
		Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("paymentMethod.id"))
        	.addOrder(Order.asc("feeType.id"))
        	.addOrder(Order.asc("id"));
        return criteria.list();
	}
}
