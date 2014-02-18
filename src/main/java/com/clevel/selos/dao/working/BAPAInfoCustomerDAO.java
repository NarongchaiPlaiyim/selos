package com.clevel.selos.dao.working;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BAPAInfoCustomer;

public class BAPAInfoCustomerDAO extends GenericDAO<BAPAInfoCustomer, Long>{

	private static final long serialVersionUID = 4285548705975415599L;
	
	public BAPAInfoCustomerDAO() {
		
	}
	@SuppressWarnings("unchecked")
	public List<BAPAInfoCustomer> findByBAPAInfo(long bapaInfoId) {
		 Criteria criteria = createCriteria();
	     criteria.add(Restrictions.eq("bapaInfo.id", bapaInfoId));
	     return (List<BAPAInfoCustomer>) criteria.list();
	}
}
