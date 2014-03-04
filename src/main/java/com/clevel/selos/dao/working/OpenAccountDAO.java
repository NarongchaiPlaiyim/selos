package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.OpenAccount;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class OpenAccountDAO extends GenericDAO<OpenAccount, Long> {
    private static final long serialVersionUID = 4685523653223504682L;
	@Inject
    @SELOS
    Logger log;

    public OpenAccountDAO() {
    }

    public List<OpenAccount> findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
        List<OpenAccount> openAccountList = criteria.list();

        return openAccountList;
    }
    
    public OpenAccount findByAccountNumber(String accountNumber) {
        log.info("findByAccountNumber : {}", accountNumber);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("accountNumber", accountNumber));
        OpenAccount openAccount = (OpenAccount) criteria.uniqueResult();

        return openAccount;
    }
    
    @SuppressWarnings("unchecked")
    public List<OpenAccount> findPledgeAccountByWorkCaseId(long workCaseId) {
    	Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("pledgeAccount", true));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
}
