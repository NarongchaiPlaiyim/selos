package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.BAPAInfo;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BAPAInfoDAO extends GenericDAO<BAPAInfo, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BAPAInfoDAO() {
    }

    public BAPAInfo findByWorkCase(WorkCase workCase) {
        log.info("findByWorkCase : {}", workCase);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        criteria.addOrder(Order.asc("id"));
        BAPAInfo bapaInfo = (BAPAInfo) criteria.uniqueResult();

        return bapaInfo;
    }
}