package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountPurpose;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BankAccountPurposeDAO extends GenericDAO<BankAccountPurpose, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BankAccountPurposeDAO() {
    }

    @Override
    public List<BankAccountPurpose> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<BankAccountPurpose> list = criteria.list();
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public BankAccountPurpose getDefaultProposeForPledge() {
    	Criteria criteria = createCriteria()
	        .add(Restrictions.eq("active", 1))
	        .add(Restrictions.eq("pledgeDefault", true));
    	List<BankAccountPurpose> purposes= criteria.list();
    	if (purposes != null && !purposes.isEmpty())
    		return purposes.get(0);
    	else
    		return null;
    }
}
