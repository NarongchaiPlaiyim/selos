package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.AccountInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AccountInfoDAO extends GenericDAO<AccountInfo, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public AccountInfoDAO() {

    }

    public AccountInfo findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
        AccountInfo accountInfo = (AccountInfo) criteria.uniqueResult();
        return accountInfo;
    }
}
