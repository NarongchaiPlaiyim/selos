package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.db.working.OpenAccountName;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class OpenAccountNameDAO extends GenericDAO<OpenAccountName, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public OpenAccountNameDAO() {
    }

    public List<OpenAccountName> findByOpenAccount(OpenAccount openAccount) {
        log.info("findByOpenAccount : {}", openAccount);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("openAccount", openAccount));
        criteria.addOrder(Order.asc("id"));
        List<OpenAccountName> openAccountNameList = criteria.list();

        return openAccountNameList;
    }

    public List<OpenAccountName> findByCustomerId(long customerId) {
        log.info("findByCustomerId : {}", customerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer.id", customerId));
        criteria.addOrder(Order.asc("id"));
        List<OpenAccountName> openAccountNameList = criteria.list();

        return openAccountNameList;
    }
}
