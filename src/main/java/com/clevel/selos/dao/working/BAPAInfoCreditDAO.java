package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BAPAInfoCredit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class BAPAInfoCreditDAO extends GenericDAO<BAPAInfoCredit,Long>{

	private static final long serialVersionUID = 287046812709235289L;
	public BAPAInfoCreditDAO() {
		
	}
	@SuppressWarnings("unchecked")
	public List<BAPAInfoCredit> findByBAPAInfo(long bapaInfoId) {
		 Criteria criteria = createCriteria();
	     criteria.add(Restrictions.eq("bapaInfo.id", bapaInfoId));
	     return (List<BAPAInfoCredit>) criteria.list();
	}
}
